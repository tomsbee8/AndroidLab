package cn.blinkdagger.androidLab.entity;

/**
 * @Author ls
 * @Date 2018/11/12
 * @Description
 * @Version
 */
public class CardItem {
    private String mTitle;
    private int mTextResource;


    public CardItem(String title, int text) {
        mTitle = title;
        mTextResource = text;
    }

    public int getText() {
        return mTextResource;
    }

    public String getTitle() {
        return mTitle;
    }
}
