package cn.blinkdagger.androidLab.ui.activity;

import android.support.v4.view.ViewPager;

import java.util.ArrayList;
import java.util.List;

import cn.blinkdagger.androidLab.base.BaseActivity;
import cn.blinkdagger.androidLab.R;
import cn.blinkdagger.androidLab.ui.adapter.CardFragmentPagerAdapter;
import cn.blinkdagger.androidLab.ui.adapter.CardPagerAdapter;
import cn.blinkdagger.androidLab.widget.HorizontalStepView;
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
    private CardFragmentPagerAdapter mFragmentCardAdapter;

    private List<String> stepList =new ArrayList<String>(){{
        add("青铜");
        add("白银");
        add("黄金");
        add("铂金");
        add("钻石");
        add("大师");
        add("王者");
    }};

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
        horizontalStepView.setStepTextList(stepList);
        horizontalStepView.setActivedPosition(4);
        initViewPager();
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


    /**
     * 通过以步骤设置卡片式ViewPager:
     * 1.
     * 2. 通过CardView 设置MaxCardElevation控制卡片之间的间距
     */
    private void initViewPager(){
        mCardAdapter = new CardPagerAdapter();
        mCardAdapter.addCardItem(new CardItem(stepList.get(0), R.string.text_1));
        mCardAdapter.addCardItem(new CardItem(stepList.get(1), R.string.text_1));
        mCardAdapter.addCardItem(new CardItem(stepList.get(2), R.string.text_1));
        mCardAdapter.addCardItem(new CardItem(stepList.get(3), R.string.text_1));
        mCardAdapter.addCardItem(new CardItem(stepList.get(4), R.string.text_1));
        mCardAdapter.addCardItem(new CardItem(stepList.get(5), R.string.text_1));
        mCardAdapter.addCardItem(new CardItem(stepList.get(6), R.string.text_1));
        //mViewPager.setAdapter(mCardAdapter);

        mFragmentCardAdapter = new CardFragmentPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mFragmentCardAdapter);
    }

    /**
     * 通过以步骤设置卡片式ViewPager:
     * 1. 通过ViewPager 设置ClipPadding为False和Padding控制卡片离屏幕左右间距
     * 2. 通过ViewPager 设置PageMargin控制卡片之间的间距
     */
    private void setUpViewPager(){
        mViewPager.setPageMargin(getResources().getDimensionPixelSize(R.dimen.card_padding));
        mViewPager.setAdapter(mFragmentCardAdapter);
    }
}
