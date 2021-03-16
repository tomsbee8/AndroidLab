package cn.blinkdagger.androidLab.extend

import java.util.ArrayList

/**
 * @Author ls
 * @Date  2021/3/11
 * @Description
 * @Version
 */

 fun <T> MutableList<T>.swap(index1: Int, index2: Int) {
    if(index1 in this.indices && index2 in this.indices) {
        val tmp = this[index1]
        this[index1] = this[index2]
        this[index2] = tmp
    }
}

inline fun <T> ArrayList<T>?.notEmpty(block: (ArrayList<T>) -> Unit) {
    if (this != null && this.isNotEmpty()) block(this)
}
inline fun <T> ArrayList<T>?.takeIfEmpty(block: () -> Unit): ArrayList<T>? {
    return if (this.isNullOrEmpty()) {
        block()
        this
    } else null
}
inline fun <T> ArrayList<T>?.takeIfNotEmpty(block: (ArrayList<T>) -> Unit): ArrayList<T>? {
    return if (this != null && this.isNotEmpty()) {
        block(this)
        this
    } else null
}

/**
 * Returns the first element matching the given [predicate] after position [start], or `null` if element was not found.
 */
inline fun <T> MutableList<T>.firstOrNullAfter(start: Int, predicate: (T) -> Boolean): T? {
    if (isEmpty()) return null

    val size = this.size
    if (start > size - 1) return null

    (start until size).forEach {
        val element = this[it]
        if (predicate(element)) return element
    }

    return null
}

/**
 * Returns the first element matching the given [predicate] after position [start] index, or -1 if element was not found.
 */
inline fun <T> MutableList<T>.firstIndexOfAfter(start: Int, predicate: (T) -> Boolean): Int {
    if (isEmpty()) return -1

    val size = this.size
    if (start > size - 1) return -1

    (start until size).forEach {
        val element = this[it]
        if (predicate(element)) return it
    }

    return -1
}




