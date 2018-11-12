package cn.blinkdagger.androidLab.UI.activity;

import android.support.v4.view.ViewPager;

import java.util.ArrayList;

import cn.blinkdagger.androidLab.Base.BaseActivity;
import cn.blinkdagger.androidLab.R;
import cn.blinkdagger.androidLab.UI.adapter.CardPagerAdapter;
import cn.blinkdagger.androidLab.Widget.HorizontalStepView;
import cn.blinkdagger.androidLab.entity.CardItem;

/**
 * @Author ls
 * @Date 2018/11/9
 * @Description
 * @Version
 */
public class StepViewActivity extends BaseActivity {

    private HorizontalStepView horizontalStepView;
    private ViewPager mViewPager;
    private CardPagerAdapter mCardAdapter;
    private int index = 1;

    @Override
    protected int getLayout() {
        return R.layout.activity_step_view;
    }

    @Override
    protected boolean useToolbar() {
        return true;
    }

    @Override
    protected void initView() {
        horizontalStepView = findViewById(R.id.step_view);
        mViewPager = findViewById(R.id.viewPager);
        horizontalStepView.setStepTextList(new ArrayList<String>() {
            {
                add("Start");
                add("V1");
                add("V2");
                add("V3");
                add("V4");
                add("V5");
                add("V6");
                add("End");
            }
        });
        horizontalStepView.setActivedPosition(4);

        mCardAdapter = new CardPagerAdapter();
        mCardAdapter.addCardItem(new CardItem(R.string.title_1, R.string.text_1));
        mCardAdapter.addCardItem(new CardItem(R.string.title_2, R.string.text_1));
        mCardAdapter.addCardItem(new CardItem(R.string.title_3, R.string.text_1));
        mCardAdapter.addCardItem(new CardItem(R.string.title_4, R.string.text_1));
        mCardAdapter.addCardItem(new CardItem(R.string.title_1, R.string.text_1));
        mCardAdapter.addCardItem(new CardItem(R.string.title_2, R.string.text_1));
        mCardAdapter.addCardItem(new CardItem(R.string.title_3, R.string.text_1));
        mCardAdapter.addCardItem(new CardItem(R.string.title_4, R.string.text_1));
        mViewPager.setAdapter(mCardAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                horizontalStepView.scrollTo(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    protected void initData() {
    }
}
