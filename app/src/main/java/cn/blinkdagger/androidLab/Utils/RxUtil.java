package cn.blinkdagger.androidLab.Utils;


import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * Created by codeest on 2016/8/3.
 */
public class RxUtil {

    /**
     * 倒计时（默认减1）
     */
    public static Flowable<Integer> countDown(int time) {
        if (time < 0) time = 0;

        final int countTime = time;
        return Flowable.interval(0, 1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<Long, Integer>() {
                    @Override
                    public Integer apply(@NonNull Long increaseTime) throws Exception {
                        return countTime - increaseTime.intValue();
                    }
                })
                .take(countTime + 1);

    }

}
