package cn.blinkdagger.androidLab.tools

import android.content.Context
import android.os.Environment
import android.os.Looper
import android.text.TextUtils
import android.widget.Toast
import cn.blinkdagger.androidLab.utils.AppUtil
import cn.blinkdagger.androidLab.utils.DeviceUtil
import cn.blinkdagger.androidLab.utils.TimeUtil
import com.orhanobut.logger.Logger
import java.io.*
import java.util.*

/**
 * @Author ls
 * @Date 2016/11/28
 * @Description Android 内外部缓存文件夹工具
 * @Version
 */
class CrashHandler private constructor() : Thread.UncaughtExceptionHandler {

    companion object {
        const val TAG = "CrashHandler"
        const val DEFAULT_FOLDER_NAME = "crashLog"

        val INSTANCE: CrashHandler by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            CrashHandler()
        }
    }

    private var mContext: Context? = null
    //系统默认的UncaughtException处理类
    private var mDefaultHandler: Thread.UncaughtExceptionHandler? = null
    //是否保存日志文件
    private var saveLogFile = false
    //是否发送日志到服务器
    private var sendLogFile = false
    //日志信息
    private var logInfoMap: HashMap<String, String>? = null
    //保存日志信息的文件夹名字
    private var logFileFolderName: String? = null

    //初始化
    fun init(context: Context?): CrashHandler? {
        mContext = context
        //获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        //设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this)
        return INSTANCE
    }

    //设置是否可以保存日志文件
    fun setSaveLogFile(saveLogFile: Boolean): CrashHandler? {
        this.saveLogFile = saveLogFile
        return INSTANCE
    }

    //设置是否可以保存日志文件到指定文件夹
    fun setSaveLogFile(saveLogFile: Boolean, folderName: String?): CrashHandler? {
        this.saveLogFile = saveLogFile
        logFileFolderName = folderName
        return INSTANCE
    }

    //设置是否可以发送日志文件
    fun setSendLogFile(sendLogFile: Boolean): CrashHandler? {
        this.sendLogFile = sendLogFile
        return INSTANCE
    }

    override fun uncaughtException(thread: Thread, throwable: Throwable) {
        if (throwable == null) {
            mDefaultHandler!!.uncaughtException(thread, throwable)
        } else {
            handleException(thread, throwable)
        }
        Logger.t(TAG).e(throwable, "UncaughtException is caught by CrashHandler!\n")
    }

    private fun handleException(thread: Thread, throwable: Throwable) { //异常是否发生在UI线程
        val isInMainThread = thread.id == mContext!!.mainLooper.thread.id
        if (isInMainThread) { //使用Toast来显示异常信息
            object : Thread() {
                override fun run() {
                    Looper.prepare()
                    Toast.makeText(mContext, "很抱歉,程序出现异常", Toast.LENGTH_LONG).show()
                    Looper.loop()
                }
            }.start()
        }
        if (saveLogFile) {
            val pathBuffer = StringBuffer(Environment.getExternalStorageDirectory().toString())
            pathBuffer.append(File.separator)
            if (!TextUtils.isEmpty(logFileFolderName)) {
                pathBuffer.append(logFileFolderName)
            } else {
                pathBuffer.append(DEFAULT_FOLDER_NAME)
            }
            pathBuffer.append("/")
            val path = pathBuffer.toString()
            saveCrashLogToFile(path, throwable)
        }
        if (sendLogFile) {
            sendCrashLogToServer()
        }
    }

    //保存异常日志到文件中
    private fun saveCrashLogToFile(path: String, ex: Throwable) {
        if (logInfoMap == null) {
            logInfoMap = HashMap()
            collectLogInfo(logInfoMap)
        }
        if (logInfoMap!!.entries.size > 0) {
            val sb = StringBuffer()
            for ((key, value) in logInfoMap!!) {
                sb.append("$key : $value\n")
            }
            val writer: Writer = StringWriter()
            val printWriter = PrintWriter(writer)
            ex.printStackTrace(printWriter)
            var cause = ex.cause
            while (cause != null) {
                cause.printStackTrace(printWriter)
                cause = cause.cause
            }
            printWriter.close()
            val result = writer.toString()
            sb.append(result)
            try {
                val timestamp = System.currentTimeMillis()
                val time = TimeUtil.cuerentTimeString
                val fileName = "crash-" + time + "-" + java.lang.Long.toHexString(timestamp) + ".log"
                if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
                    val dir = File(path)
                    if (!dir.exists()) {
                        dir.mkdirs()
                    }
                    val logFile = File(dir, fileName)
                    val fos = FileOutputStream(logFile, true)
                    fos.write(sb.toString().toByteArray(charset("UTF-8")))
                    fos.close()
                }
            } catch (e: Exception) {
                Logger.t(TAG).e("an error occured while saving log file ...", e)
            }
        }
    }

    //发送异常日志到服务器
    private fun sendCrashLogToServer() {
        if (logInfoMap == null) {
            logInfoMap = HashMap()
            collectLogInfo(logInfoMap)
        }
    }

    //收集异常信息
    private fun collectLogInfo(map: HashMap<String, String>?) {
        if (map == null) {
            return
        }
        mContext?.let {context ->
            map["时间"] = TimeUtil.cuerentTimeString
            map["手机型号"] = DeviceUtil.phoneModel
            map["手机IMEI"] = DeviceUtil.getPhoneIMEI(context)
            map["手机系统API"] = DeviceUtil.androidVersionName
            map["APP版本号"] = AppUtil.getVersionCode(context).toString() + ""
            map["APP版本名称"] = AppUtil.getVersionName(context) ?: "unKnown"
        }
    }
}