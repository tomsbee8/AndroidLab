package cn.blinkdagger.androidLab.extend

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat

/**
 * @Author ls
 * @Date  2020/12/14
 * @Description
 * @Version
 */
fun View?.bg(id: Int) {
    this?.context?.let {
        this.background = ContextCompat.getDrawable(it, id)
    }
}

fun View?.gone() {
    this?.visibility = View.GONE
}

fun View?.visible() {
    this?.visibility = View.VISIBLE
}

fun View?.invisible() {
    this?.visibility = View.INVISIBLE
}

fun TextView?.color(id: Int) {
    this?.context?.let {
        this.setTextColor(ContextCompat.getColor(it, id))
    }
}

fun TextView?.topDrawable(id: Int) {
    this?.setCompoundDrawablesWithIntrinsicBounds(0, id, 0, 0)
}

fun TextView?.rightDrawable(id: Int) {
    this?.setCompoundDrawablesWithIntrinsicBounds(0, 0, id, 0)
}

fun TextView?.rightDrawable(drawable: Drawable) {
    this?.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null)
}

fun TextView?.leftDrawable(id: Int) {
    this?.setCompoundDrawablesWithIntrinsicBounds(id, 0, 0, 0)
}

fun TextView?.noDrawable() {
    this?.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
}

fun View?.noPadding() {
    this?.setPadding(0, 0, 0, 0)
}

fun View?.bottomPadding(bottom: Int) {
    this?.setPadding(0, 0, 0, bottom)
}

fun View?.topPadding(top: Int) {
    this?.setPadding(0, top, 0, 0)
}

fun TextView?.bgResource(id: Int) {
    this?.setBackgroundResource(id)
}

fun TextView?.text(id: Int) {
    this?.context?.let {
        text = it.getString(id)
    }
}

fun TextView?.text(text: String?) {
    this?.text = text.orEmpty()
}
