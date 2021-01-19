package cn.blinkdagger.androidLab.extend

/**
 * @Author ls
 * @Date  2020/12/14
 * @Description
 * @Version
 */
inline fun ktTry(block: () -> Unit) {
    try {
        block()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

inline fun <T1, T2> ifNotNull(value1: T1?, value2: T2?, bothNotNull: (t1: T1, t2: T2) -> Unit) {
    if (value1 != null && value2 != null) {
        bothNotNull(value1, value2)
    }
}

inline fun <T1, T2, T3> ifNotNull(value1: T1?, value2: T2?, value3: T3?, bothNotNull: (t1: T1, t2: T2, t3: T3) -> Unit) {
    if (value1 != null && value2 != null && value3 != null) {
        bothNotNull(value1, value2, value3)
    }
}

inline fun ifNotNullOrBlank(value1: String?, value2: String?, bothNotNullOrBlank: (t1: String, t2: String) -> Unit) {
    if (!value1.isNullOrBlank() && !value2.isNullOrBlank()) {
        bothNotNullOrBlank(value1, value2)
    }
}

inline fun ifNotNullOrBlank(value1: String?, value2: String?, value3: String?, bothNotNullOrBlank: (t1: String, t2: String, t3: String) -> Unit) {
    if (!value1.isNullOrBlank() && !value2.isNullOrBlank() && !value3.isNullOrBlank()) {
        bothNotNullOrBlank(value1, value2, value3)
    }
}

inline fun <T1> T1?.checkAndThen(check: (T1) -> Boolean, then: (T1) -> Unit) {
    if (this != null && check(this)) {
        then(this)
    }
}

fun findFirstNotEmptyString(vararg params: String?) : String? {
    return params.first{ value ->
        !value.isNullOrEmpty()
    }
}

