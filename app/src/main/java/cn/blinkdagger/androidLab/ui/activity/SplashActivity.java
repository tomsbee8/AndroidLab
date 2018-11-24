package cn.blinkdagger.androidLab.ui.activity;

import android.content.Intent;
import android.view.KeyEvent;

import com.orhanobut.logger.Logger;

import org.reactivestreams.Subscription;

import cn.blinkdagger.androidLab.base.BaseActivity;
import cn.blinkdagger.androidLab.R;
import cn.blinkdagger.androidLab.utils.RxUtil;
import cn.blinkdagger.androidLab.utils.SystemBarUtil;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.ResourceSubscriber;

public class SplashActivity extends BaseActivity {

    private final int COUNT_DOWN_SECONDS = 3;
    protected CompositeDisposable mCompositeSubscription;

    protected void unSubscribe() {
        if (mCompositeSubscription != null) {
            mCompositeSubscription.dispose();
        }
    }

    protected void addSubscrebe(Disposable disposable) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeDisposable();
        }
        mCompositeSubscription.add(disposable);
    }


    @Override
    protected int getLayout() {
        return R.layout.activity_splash;
    }

    @Override
    protected boolean useToolbar() {
        return false;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void setStatusBar() {
        SystemBarUtil.setImmersiveMode(this);
    }

    @Override
    protected void initData() {
        jumpToMain();
    }

    private void jumpToMain() {
        Disposable disposable = RxUtil.countDown(COUNT_DOWN_SECONDS)
                .doOnSubscribe(new Consumer<Subscription>() {
                    @Override
                    public void accept(@NonNull Subscription disposable) throws Exception {
                        Logger.i("开始倒计时");
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new ResourceSubscriber<Integer>() {
                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        Logger.i("计时完成");
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                        SplashActivity.this.finish();
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Logger.i("当前计时：" + integer);
                    }
                });
        addSubscrebe(disposable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unSubscribe();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
