package cn.blinkdagger.androidLab.utils

import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

/**
 * Created by codeest on 2016/8/3.
 */
object RxUtil {
    /**
     * 倒计时（默认减1）
     */
    @JvmStatic
    fun countDown(time: Int): Flowable<Int> {
        var time = time
        if (time < 0) time = 0
        val countTime = time
        return Flowable.interval(0, 1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .map { increaseTime -> countTime - increaseTime.toInt() }
                .take(countTime + 1.toLong())
    }
}