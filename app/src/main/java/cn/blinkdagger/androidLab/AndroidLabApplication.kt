package cn.blinkdagger.androidLab

import android.app.Application
import com.orhanobut.logger.LogLevel
import com.orhanobut.logger.Logger

/**
 * Created by d on 2016/11/16.
 */
class AndroidLabApplication : Application() {

    companion object {
        lateinit var instance: AndroidLabApplication
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        //初始化日志系统
        val level = if (BuildConfig.DEBUG) LogLevel.FULL else LogLevel.NONE
        Logger.init("KaiStart") // default PRETTYLOGGER or use just init()
                .methodCount(3) // default 2
                .hideThreadInfo() // default shown
                .logLevel(level) // default LogLevel.FULL,Use LogLevel.NONE for the release versions.
                .methodOffset(2) // default 0
        //初始化异常处理的捕获
//        if(ProcessUtil.isOnMainProcess(this)) {
//            CrashHandler.getInstance()
//                    .init(this)
//                    .setSaveLogFile(true)
//                    .setSendLogFile(false);
//        }
    }

}