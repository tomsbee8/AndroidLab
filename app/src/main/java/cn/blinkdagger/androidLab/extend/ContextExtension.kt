package cn.blinkdagger.androidLab.extend

import android.app.Activity
import android.app.Service
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import org.jetbrains.anko.internals.AnkoInternals

/**
 * @Author ls
 * @Date  2021/1/19
 * @Description
 * @Version
 */

inline fun <reified T : Activity> Activity.internalStartActivity(vararg params: Pair<String, Any>) {
    AnkoInternals.internalStartActivity(this, T::class.java, params)
}

inline fun <reified T : Activity> Fragment.internalStartActivity(vararg params: Pair<String, Any>) {
    this.context?.let { context ->
        AnkoInternals.internalStartActivity(context, T::class.java, params)
    }
}

inline fun <reified T : Activity> Activity.internalStartActivity() {
    AnkoInternals.internalStartActivity(this, T::class.java, emptyArray())
}

inline fun <reified T : Activity> Fragment.internalStartActivity() {
    this.context?.let { context ->
        AnkoInternals.internalStartActivity(context, T::class.java, emptyArray())
    }
}

inline fun <reified T : Service> Activity.internalStartService(vararg params: Pair<String, Any>) {
    AnkoInternals.internalStartService(this, T::class.java, params)
}

inline fun <reified T : Service> Activity.internalStartService() {
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

fun Context?.findActivity(): Activity? {
    return when (this) {
        is Activity -> this
        is ContextWrapper -> this.baseContext?.findActivity()
        else -> null
    }
}

