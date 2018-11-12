package cn.blinkdagger.androidLab.UI.adapter;

import android.support.v7.widget.CardView;

/**
 * @Author ls
 * @Date 2018/11/12
 * @Description
 * @Version
 */
public interface CardAdapter {

    int MAX_ELEVATION_FACTOR = 2;

    float getBaseElevation();

    CardView getCardViewAt(int position);

    int getCount();
}
