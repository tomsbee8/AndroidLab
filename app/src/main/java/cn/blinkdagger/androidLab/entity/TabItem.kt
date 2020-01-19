package cn.blinkdagger.androidLab.entity

/**
 * 类描述：底部导航栏图标文字
 * 创建人：ls
 * 创建时间：2018/3/7
 * 修改人：
 * 修改时间：
 * 修改备注：
 */
class TabItem {
    private val tabId //Tab的唯一标识
            : String? = null
    private val tabIndex //Tab的唯一索引
            = 0
    private val tabNormalIconName //未选中状态图标名称
            : String? = null
    private val tabCheckedIconName //选中状态图标名称
            : String? = null
    private val tabText // 文字
            : String? = null
    private val tabNormalTextColor //未选中状态文字颜色
            : String? = null
    private val tabCheckedTextColor //选中状态文字颜色
            : String? = null
}