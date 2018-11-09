package cn.blinkdagger.androidLab.entity;

/**
 * 类描述：
 * 创建人：ls
 * 创建时间：2018/1/24
 * 修改人：
 * 修改时间：
 * 修改备注：
 */

public class MainItem {

    private int itemIcon;
    private String itemName;

    public MainItem(int itemIcon, String itemName) {
        this.itemIcon = itemIcon;
        this.itemName = itemName;
    }

    public int getItemIcon() {
        return itemIcon;
    }

    public void setItemIcon(int itemIcon) {
        this.itemIcon = itemIcon;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
}
