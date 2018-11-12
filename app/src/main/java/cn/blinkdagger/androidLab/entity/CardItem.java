package cn.blinkdagger.androidLab.entity;

/**
 * @Author ls
 * @Date 2018/11/12
 * @Description
 * @Version
 */
public class CardItem {
    private int mTextResource;
    private int mTitleResource;

    public CardItem(int title, int text) {
        mTitleResource = title;
        mTextResource = text;
    }

    public int getText() {
        return mTextResource;
    }

    public int getTitle() {
        return mTitleResource;
    }
}
