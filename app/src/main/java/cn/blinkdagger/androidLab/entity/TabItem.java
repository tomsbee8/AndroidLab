package cn.blinkdagger.androidLab.entity;

/**
 * 类描述：底部导航栏图标文字
 * 创建人：ls
 * 创建时间：2018/3/7
 * 修改人：
 * 修改时间：
 * 修改备注：
 */

public class TabItem {

    private String tabId;//Tab的唯一标识
    private int tabIndex;//Tab的唯一索引
    private String tabNormalIconName;//未选中状态图标名称
    private String tabCheckedIconName;//选中状态图标名称
    private String tabText;// 文字
    private String tabNormalTextColor;//未选中状态文字颜色
    private String tabCheckedTextColor;//选中状态文字颜色

}
