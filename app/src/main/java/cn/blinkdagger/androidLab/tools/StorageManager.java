package cn.blinkdagger.androidLab.tools;

import android.os.Environment;

import java.io.File;

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
    private static String getExternalStoragePath() {
        if (DeviceUtil.isSDPresent()) {
            return Environment.getExternalStorageDirectory().toString();
        }
        return null;
    }


    public static class Builder {

        StringBuilder filePath;

        public Builder() {
            String sdRootPath = getExternalStoragePath();
            if (sdRootPath == null) {
                throw new NullPointerException("SD card is unavailable , please ensure SD Card is present ");
            } else {
                filePath = new StringBuilder(getExternalStoragePath());
            }
        }

        public void newFolder(String folderName) {
            filePath.append(File.separator).append(folderName);
        }

        public void newFile(String fileName) {
            filePath.append(File.separator).append(fileName);
        }

        public String getPath() {
            return filePath.toString();
        }
    }
}
