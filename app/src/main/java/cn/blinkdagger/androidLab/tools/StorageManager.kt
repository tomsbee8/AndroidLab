package cn.blinkdagger.androidLab.tools

import android.os.Environment
import cn.blinkdagger.androidLab.utils.DeviceUtil
import java.io.File

/**
 * @Author ls
 * @Date 2019/1/28
 * @Description Android 内外部缓存文件夹工具
 * @Version
 */
object StorageManager {
    /**
     * 获得SD卡根目录
     */
    private val externalStoragePath: String?
        private get() = if (DeviceUtil.isSDPresent) {
            Environment.getExternalStorageDirectory().toString()
        } else null

    class Builder {
        var filePath: StringBuilder? = null
        fun newFolder(folderName: String?) {
            filePath!!.append(File.separator).append(folderName)
        }

        fun newFile(fileName: String?) {
            filePath!!.append(File.separator).append(fileName)
        }

        val path: String
            get() = filePath.toString()

        init {
            val sdRootPath = externalStoragePath
            filePath = if (sdRootPath == null) {
                throw NullPointerException("SD card is unavailable , please ensure SD Card is present ")
            } else {
                StringBuilder(externalStoragePath?:"")
            }
        }
    }
}