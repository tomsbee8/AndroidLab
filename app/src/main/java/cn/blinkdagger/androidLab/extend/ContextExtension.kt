package cn.blinkdagger.androidLab.extend

import android.app.Activity
import android.app.Service
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.os.Parcelable
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import cn.blinkdagger.androidLab.utils.DensityUtil
import cn.blinkdagger.androidLab.utils.DeviceUtil
import org.jetbrains.anko.internals.AnkoInternals

/**
 * @Author ls
 * @Date  2021/1/19
 * @Description
 * @Version
 */

inline fun <reified T : Activity> Activity.launchActivity(vararg params: Pair<String, Any>) {
    AnkoInternals.internalStartActivity(this, T::class.java, params)
}

inline fun <reified T : Activity> Fragment.launchActivity(vararg params: Pair<String, Any>) {
    this.context?.let { context ->
        AnkoInternals.internalStartActivity(context, T::class.java, params)
    }
}

inline fun <reified T : Activity> Activity.launchActivity() {
    AnkoInternals.internalStartActivity(this, T::class.java, emptyArray())
}

inline fun <reified T : Activity> Fragment.launchActivity() {
    this.context?.let { context ->
        AnkoInternals.internalStartActivity(context, T::class.java, emptyArray())
    }
}

inline fun <reified T : Service> Activity.launchService(vararg params: Pair<String, Any>) {
    AnkoInternals.internalStartService(this, T::class.java, params)
}

inline fun <reified T : Service> Activity.launchService() {
    AnkoInternals.internalStartService(this, T::class.java, emptyArray())
}

inline fun ComponentActivity.launchActivityForResult(intent: Intent, crossinline onActivityResult: (ActivityResult) -> Unit) {
    val activityLauncher = this.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        onActivityResult(result)
    }
    activityLauncher.launch(intent)
}

inline fun <reified T : Activity> ComponentActivity.launchActivityForResult(vararg params: Pair<String, Any>, crossinline onActivityResult: (ActivityResult) -> Unit) {
    val intent = AnkoInternals.createIntent(this, T::class.java, params)
    this.launchActivityForResult(intent, onActivityResult)
}

inline fun Fragment.launchActivityForResult(intent: Intent, crossinline onActivityResult: (ActivityResult) -> Unit) {
    val activityLauncher = this.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        onActivityResult(result)
    }
    activityLauncher.launch(intent)
}

inline fun <reified T : Activity> Fragment.launchActivityForResult(vararg params: Pair<String, Any>, crossinline onActivityResult: (ActivityResult) -> Unit) {
    this.context?.let { context ->
        val intent = AnkoInternals.createIntent(context, T::class.java, params)
        this.launchActivityForResult(intent, onActivityResult)
    }
}

inline fun ComponentActivity.requestPermission( permission: String, crossinline onPermissionResult: (Boolean) -> Unit) {
    val permissionRequest = this.registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
        onPermissionResult(result)
    }
    permissionRequest.launch(permission)
}

inline fun ComponentActivity.requestMultiplePermissions(vararg permissions: String, crossinline onPermissionResult: (Map<String,Boolean>) -> Unit) {
    val permissionRequest = this.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->
        onPermissionResult(result)
    }
    permissionRequest.launch(permissions)
}

private fun Context?.aliveActivity(): Context? {
    return this?.findContextActivity()?.let {
        if (it.isFinishing || it.isDestroyed) {
            return null
        } else {
            this
        }
    } ?: this
}

fun Context?.findContextActivity(): Activity? {
    return when (this) {
        is Activity -> this
        is ContextWrapper -> this.baseContext?.findContextActivity()
        else -> null
    }
}


fun Activity?.intExtra(key: String, defaultValue: Int = 0): Int {
    return this?.intent?.getIntExtra(key, defaultValue)?: defaultValue
}
fun Activity?.shortExtra(key: String, defaultValue: Short = 0): Short {
    return this?.intent?.getShortExtra(key, defaultValue)?: defaultValue
}
fun Activity?.longExtra(key: String, defaultValue: Long = 0): Long {
    return this?.intent?.getLongExtra(key, defaultValue)?: defaultValue
}
fun Activity?.boolExtra(key: String, defaultValue: Boolean = false): Boolean {
    return this?.intent?.getBooleanExtra(key, defaultValue)?: defaultValue
}
fun Activity?.stringExtra(key: String, defaultValue: String = ""): String {
    return this?.intent?.getStringExtra(key) ?: defaultValue
}
fun Activity?.intListExtra(key: String, defaultValue: ArrayList<Int> = arrayListOf()): ArrayList<Int> {
    return this?.intent?.getIntegerArrayListExtra(key) ?: defaultValue
}
fun Activity?.stringListExtra(key: String, defaultValue: ArrayList<String> = arrayListOf()): ArrayList<String> {
    return this?.intent?.getStringArrayListExtra(key) ?: defaultValue
}
fun <T : Parcelable> Activity?.arrayListExtra(key: String, defaultValue: ArrayList<T> = arrayListOf()): ArrayList<T> {
    return this?.intent?.getParcelableArrayListExtra<T>(key)?: defaultValue
}

fun Fragment?.intExtra(key: String, defaultValue: Int = 0): Int {
    return this?.arguments?.getInt(key)?: defaultValue
}
fun Fragment?.shortExtra(key: String, defaultValue: Short = 0): Short {
    return this?.arguments?.getShort(key)?: defaultValue
}
fun Fragment?.longExtra(key: String, defaultValue: Long = 0): Long {
    return this?.arguments?.getLong(key, defaultValue)?: defaultValue
}
fun Fragment?.boolExtra(key: String, defaultValue: Boolean = false): Boolean {
    return this?.arguments?.getBoolean(key, defaultValue)?: defaultValue
}
fun Fragment?.stringExtra(key: String, defaultValue: String = ""): String {
    return this?.arguments?.getString(key, defaultValue)?: defaultValue
}
fun Fragment?.intListExtra(key: String, defaultValue: ArrayList<Int> = arrayListOf()): ArrayList<Int> {
    return this?.arguments?.getIntegerArrayList(key) ?: defaultValue
}
fun Fragment?.stringListExtra(key: String, defaultValue: ArrayList<String> = arrayListOf()): ArrayList<String> {
    return this?.arguments?.getStringArrayList(key) ?: defaultValue
}
fun < T : Parcelable> Fragment?.arrayListExtra(key: String, defaultValue: ArrayList<T> = arrayListOf()): ArrayList<T> {
    return this?.arguments?.getParcelableArrayList<T>(key) ?: defaultValue
}

fun Context?.dp2px(dp: Int): Int = dp2px(dp.toFloat())
fun Context?.dp2px(dp: Float): Int = this?.aliveActivity()?.let { DensityUtil.dp2px(it, dp) }?: 0
fun Context?.sp2px(sp: Int): Int = sp2px(sp.toFloat())
fun Context?.sp2px(sp: Float): Int = this?.aliveActivity()?.let { DensityUtil.sp2px(it, sp) }?: 0
fun Fragment?.dp2px(dp: Int): Int = dp2px(dp.toFloat())
fun Fragment?.dp2px(dp: Float): Int = this?.activity?.aliveActivity()?.dp2px(dp)?: 0
fun Fragment?.sp2px(sp: Int): Int = sp2px(sp.toFloat())
fun Fragment?.sp2px(sp: Float): Int = this?.activity?.aliveActivity()?.sp2px(sp)?: 0





