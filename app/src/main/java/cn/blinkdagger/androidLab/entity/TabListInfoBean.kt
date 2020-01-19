package cn.blinkdagger.androidLab.entity

/**
 * 类描述：动态替换底部导航Tab图标文字的实体类
 * 创建人：ls
 * 创建时间：2018/3/7
 * 修改人：
 * 修改时间：
 * 修改备注：
 */
class TabListInfoBean {
    private val updateEnable //是否启用
            = false
    private val tabVersionCode //内部版本号
            = 0
    private val tabItems //
            : List<TabItem>? = null
    private val downloadUrl //下载地址
            = 0
    private val fileMd5 //下载的文件的Md5值
            = 0
    private val remark //描述
            = 0
}