package cn.blinkdagger.androidLab.utils

import android.os.Build
import android.os.Environment
import android.text.TextUtils
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.util.*

/**
 * 类描述：手机型号相关
 * 创建人：ls
 * 创建时间：2018/2/28
 * 修改人：
 * 修改时间：
 * 修改备注：
 */
object RomUtil {
    // MIUI标识
    private const val KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code"
    private const val KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name"
    private const val KEY_MIUI_INTERNAL_STORAGE = "ro.miui.internal.storage"
    // Flyme标识
    private const val KEY_FLYME_ID_FALG_KEY = "ro.build.display.id"
    private const val KEY_FLYME_ID_FALG_VALUE_KEYWORD = "Flyme"
    private const val KEY_FLYME_ICON_FALG = "persist.sys.use.flyme.icon"
    private const val KEY_FLYME_SETUP_FALG = "ro.meizu.setupwizard.flyme"
    private const val KEY_FLYME_PUBLISH_FALG = "ro.flyme.published"
    /**
     * 检测MIUI
     *
     * @return
     */
    val isMIUI: Boolean
        get() {
            val device = Build.MANUFACTURER
            println("Build.MANUFACTURER = $device")
            return if (device == "Xiaomi") {
                val prop = Properties()
                try {
                    prop.load(FileInputStream(File(Environment.getRootDirectory(), "build.prop")))
                } catch (e: IOException) {
                    e.printStackTrace()
                    return false
                }
                prop.getProperty(KEY_MIUI_VERSION_CODE, null) != null || prop.getProperty(KEY_MIUI_VERSION_NAME, null) != null || prop.getProperty(KEY_MIUI_INTERNAL_STORAGE, null) != null
            } else {
                false
            }
        }

    /**
     * 检测Flyme
     *
     * @return
     */
    val isFlyme: Boolean
        get() {
            var buildProperties: BuildProperties? = null
            try {
                buildProperties = BuildProperties.newInstance()
                if (buildProperties.containsKey(KEY_FLYME_ICON_FALG) || buildProperties.containsKey(KEY_FLYME_SETUP_FALG) || buildProperties.containsKey(KEY_FLYME_PUBLISH_FALG)) {
                    return true
                }
                if (buildProperties.containsKey(KEY_FLYME_ID_FALG_KEY)) {
                    val romName = buildProperties.getProperty(KEY_FLYME_ID_FALG_KEY)
                    if (!TextUtils.isEmpty(romName) && romName.contains(KEY_FLYME_ID_FALG_VALUE_KEYWORD)) {
                        return true
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return false
        }

    /**
     * 检测EMUI
     *
     * @return
     */
    val isEMUI: Boolean
        get() {
            val file = File(Environment.getRootDirectory(), "build.prop")
            if (file.exists()) {
                val properties = Properties()
                var fis: FileInputStream? = null
                try {
                    fis = FileInputStream(file)
                    properties.load(fis)
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    if (fis != null) {
                        try {
                            fis.close()
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                }
                return properties.containsKey("ro.build.hw_emui_api_level")
            }
            return false
        }

    private class BuildProperties private constructor() {
        private val properties: Properties
        fun containsKey(key: Any?): Boolean {
            return properties.containsKey(key)
        }

        fun containsValue(value: Any?): Boolean {
            return properties.containsValue(value)
        }

        fun entrySet(): Set<Map.Entry<Any, Any>> {
            return properties.entries
        }

        fun getProperty(name: String?): String {
            return properties.getProperty(name)
        }

        fun getProperty(name: String?, defaultValue: String?): String {
            return properties.getProperty(name, defaultValue)
        }

        val isEmpty: Boolean
            get() = properties.isEmpty

        fun keys(): Enumeration<Any> {
            return properties.keys()
        }

        fun keySet(): Set<Any> {
            return properties.keys
        }

        fun size(): Int {
            return properties.size
        }

        fun values(): Collection<Any> {
            return properties.values
        }

        companion object {
            @Throws(IOException::class)
            fun newInstance(): BuildProperties {
                return BuildProperties()
            }
        }

        init {
            properties = Properties()
            // 读取系统配置信息build.prop类
            properties.load(FileInputStream(File(Environment.getRootDirectory(), "build.prop")))
        }
    }
}