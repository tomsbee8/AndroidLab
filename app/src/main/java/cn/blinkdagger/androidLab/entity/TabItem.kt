package cn.blinkdagger.androidLab.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * 类描述：底部导航栏图标文字
 * 创建人：ls
 * 创建时间：2018/3/7
 * 修改人：
 * 修改时间：
 * 修改备注：
 */
@Parcelize
class TabItem(
        val tabId: String? = null, //Tab的唯一标识
        val tabIndex : Int = 0, //Tab的唯一索引
        val tabNormalIconName: String? = null, //未选中状态图标名称
        val tabCheckedIconName: String? = null, //选中状态图标名称
        val tabText: String? = null, // 文字
        val tabNormalTextColor: String? = null, //未选中状态文字颜色
        val tabCheckedTextColor: String? = null, //选中状态文字颜色
) : Parcelable