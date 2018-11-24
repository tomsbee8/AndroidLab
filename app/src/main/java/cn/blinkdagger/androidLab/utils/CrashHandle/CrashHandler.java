package cn.blinkdagger.androidLab.utils.CrashHandle;

import android.content.Context;
import android.os.Environment;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.Toast;

import cn.blinkdagger.androidLab.utils.DeviceUtil;
import cn.blinkdagger.androidLab.utils.TimeUtil;
import cn.blinkdagger.androidLab.utils.AppUtil;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by d on 2016/11/16.
 */

public class CrashHandler implements Thread.UncaughtExceptionHandler {

    public static final String TAG = "CrashHandler";
    public final String DEFAULT_FOLDER_NAME = "crashLog";

    private Context mContext;
    //系统默认的UncaughtException处理类
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    //是否保存日志文件
    private boolean saveLogFile = false;
    //是否发送日志到服务器
    private boolean sendLogFile = false;
    //日志信息
    private HashMap<String, String> logInfoMap;
    //保存日志信息的文件夹名字
    private String logFileFolderName;

    private static volatile CrashHandler INSTANCE = null;


    public static CrashHandler getInstance() {
        if (INSTANCE == null) {
            synchronized (CrashHandler.class) {
                if (INSTANCE == null) {
                    INSTANCE = new CrashHandler();
                }
            }
        }
        return INSTANCE;
    }

    private CrashHandler() {
    }

    //初始化
    public CrashHandler init(Context context) {
        mContext = context;
        //获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        //设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
        return INSTANCE;
    }

    //设置是否可以保存日志文件
    public CrashHandler setSaveLogFile(boolean saveLogFile) {
        this.saveLogFile = saveLogFile;
        return INSTANCE;
    }

    //设置是否可以保存日志文件到指定文件夹
    public CrashHandler setSaveLogFile(boolean saveLogFile, String folderName) {
        this.saveLogFile = saveLogFile;
        this.logFileFolderName = folderName;
        return INSTANCE;
    }

    //设置是否可以发送日志文件
    public CrashHandler setSendLogFile(boolean sendLogFile) {
        this.sendLogFile = sendLogFile;
        return INSTANCE;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {

        if (throwable == null) {
            mDefaultHandler.uncaughtException(thread, throwable);
        } else {
            handleException(thread,throwable);
        }
        Logger.t(TAG).e(throwable, "UncaughtException is caught by CrashHandler!\n");

    }

    private void handleException(Thread thread,Throwable throwable) {
        //异常是否发生在UI线程
        boolean isInMainThread = thread.getId() == mContext.getMainLooper().getThread().getId();

        if(isInMainThread){
            //使用Toast来显示异常信息
            new Thread() {
                @Override
                public void run() {
                    Looper.prepare();
                    Toast.makeText(mContext, "很抱歉,程序出现异常", Toast.LENGTH_LONG).show();
                    Looper.loop();
                }
            }.start();
        }

        if (saveLogFile) {
            StringBuffer pathBuffer = new StringBuffer(Environment.getExternalStorageDirectory().toString());
            pathBuffer.append(File.separator);
            if (!TextUtils.isEmpty(logFileFolderName)) {
                pathBuffer.append(logFileFolderName);
            } else {
                pathBuffer.append(DEFAULT_FOLDER_NAME);
            }
            pathBuffer.append("/");
            String path = pathBuffer.toString();
            saveCrashLogToFile(path, throwable);
        }
        if (sendLogFile) {
            sendCrashLogToServer();
        }
    }

    //保存异常日志到文件中
    private void saveCrashLogToFile(String path, Throwable ex) {
        if (logInfoMap == null) {
            logInfoMap = new HashMap<String, String>();
            collectLogInfo(logInfoMap);
        }
        if (logInfoMap.entrySet().size() > 0) {
            StringBuffer sb = new StringBuffer();
            for (Map.Entry<String, String> entry : logInfoMap.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                sb.append(key + " : " + value + "\n");
            }
            Writer writer = new StringWriter();
            PrintWriter printWriter = new PrintWriter(writer);
            ex.printStackTrace(printWriter);
            Throwable cause = ex.getCause();
            while (cause != null) {
                cause.printStackTrace(printWriter);
                cause = cause.getCause();
            }
            printWriter.close();
            String result = writer.toString();
            sb.append(result);
            try {
                Long timestamp = System.currentTimeMillis();
                String time = TimeUtil.getCuerentTimeString();
                String fileName = "crash-" + time + "-" + Long.toHexString(timestamp) + ".log";
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    File dir = new File(path);
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }
                    File logFile = new File(dir, fileName);
                    FileOutputStream fos = new FileOutputStream(logFile, true);
                    fos.write(sb.toString().getBytes("UTF-8"));
                    fos.close();
                }
            } catch (Exception e) {
                Logger.t(TAG).e("an error occured while saving log file ...", e);
            }
        }
    }

    //发送异常日志到服务器
    private void sendCrashLogToServer() {
        if (logInfoMap == null) {
            logInfoMap = new HashMap<>();
            collectLogInfo(logInfoMap);
        }
    }

    //收集异常信息
    private void collectLogInfo(HashMap<String, String> map) {
        if (map == null) {
            return;
        }
        map.put("时间", TimeUtil.getCuerentTimeString());
        map.put("手机型号", DeviceUtil.getPhoneModel());
        map.put("手机IMEI", DeviceUtil.getPhoneIMEI(mContext));
        map.put("手机系统API", DeviceUtil.getAndroidVersionName());
        map.put("APP版本号", AppUtil.getVersionCode(mContext) + "");
        map.put("APP版本名称", AppUtil.getVersionName(mContext));
    }
}
