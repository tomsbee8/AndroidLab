package cn.blinkdagger.androidLab.UI.Activity;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import cn.blinkdagger.androidLab.Base.BaseActivity;
import cn.blinkdagger.androidLab.R;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * 类描述：
 * 创建人：ls
 * 创建时间：2018/3/8
 * 修改人：
 * 修改时间：
 * 修改备注：
 */

public class ActiveTabSampleActivity extends BaseActivity {


    private ImageView tabIV;

    @Override
    protected int getLayout() {
        return R.layout.activity_active_tab_layout;
    }

    @Override
    protected void initViewAndData() {
        tabIV = findViewById(R.id.tab_iv);

        final String imageUrl = "";
            Observable<Drawable> drawableObservable = Observable.create(new ObservableOnSubscribe<Drawable>() {
                @Override
                public void subscribe(ObservableEmitter<Drawable> e) throws Exception {
                    RequestOptions options = new RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.ALL);
                   Glide.with(ActiveTabSampleActivity.this)
                            .load(imageUrl)
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@android.support.annotation.Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    return false;
                                }
                            }).submit();

                }
            });



    }
}
