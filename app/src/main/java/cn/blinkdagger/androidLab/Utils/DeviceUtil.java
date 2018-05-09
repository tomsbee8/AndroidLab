package cn.blinkdagger.androidLab.Utils;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

/**
 * Created by d on 2016/11/16.
 */

public class DeviceUtil {

    /**
     * 获取手机IMEI码
     * @return
     */
    public  static String getPhoneIMEI(Context context){
        TelephonyManager mTm = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
        String imei = mTm.getDeviceId();
        return imei;
    }
    /**
     * 获取手机型号
     */
    public  static String getPhoneModel(){

        return Build.MODEL;
    }
    /**
     * 获取手机厂商
     */
    public  static String getPhoneManufacturer(){

        return Build.MANUFACTURER;
    }

    /**
     * 获取当前手机系统API的版本号
     * @return
     */
    public static int getAndroidVersionCode() {

        return android.os.Build.VERSION.SDK_INT;
    }
    /**
     * 获取当前手机系统API的版本名称
     * @return
     */
    public static String getAndroidVersionName() {

        return android.os.Build.VERSION.SDK;
    }


    /**
     * 是否安装包名为packageName的App
     */
    public static boolean getPackageName(Context context, String packageName) {
        if (TextUtils.isEmpty(packageName)) {
            return false;
        }
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        return intent != null;
    }

    /**
     *
     */


}
