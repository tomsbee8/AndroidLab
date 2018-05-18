package cn.blinkdagger.androidLab.Entity;

/**
 * 类描述：
 * 创建人：ls
 * 创建时间：2018/5/14
 * 修改人：
 * 修改时间：
 * 修改备注：
 */

public class CategoryItem {

    private String categoryId;
    private String categoryName;
    private String categoryBg;

    public CategoryItem(String categoryId, String categoryName, String categoryBg) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.categoryBg = categoryBg;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryBg() {
        return categoryBg;
    }

    public void setCategoryBg(String categoryBg) {
        this.categoryBg = categoryBg;
    }
}
