package cn.blinkdagger.androidLab.tools;

import android.os.Environment;

import cn.blinkdagger.androidLab.utils.DeviceUtil;

/**
 * @Author ls
 * @Date 2019/1/28
 * @Description Android 内外部缓存文件夹工具
 * @Version
 */
public class StorageManager {

    /**
     * 获得SD卡根目录
     */
    public static String getSDPath() {
        if (DeviceUtil.isSDPresent()) {
            return Environment.getExternalStorageDirectory().toString();
        }
        return null;
    }
}
