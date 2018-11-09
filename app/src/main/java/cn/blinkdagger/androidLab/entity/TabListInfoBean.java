package cn.blinkdagger.androidLab.entity;

import java.util.List;

/**
 * 类描述：动态替换底部导航Tab图标文字的实体类
 * 创建人：ls
 * 创建时间：2018/3/7
 * 修改人：
 * 修改时间：
 * 修改备注：
 */

public class TabListInfoBean {

    private boolean updateEnable;//是否启用
    private int tabVersionCode;//内部版本号
    private List<TabItem> tabItems;//
    private int downloadUrl;//下载地址
    private int fileMd5;//下载的文件的Md5值
    private int remark;//描述

}
