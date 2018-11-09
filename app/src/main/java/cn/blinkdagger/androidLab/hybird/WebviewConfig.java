package cn.blinkdagger.androidLab.hybird;

/**
 * @Author ls
 * @Date 2018/10/23
 * @Description
 * @Version
 */
public class WebviewConfig {

    // 根据本地调试开关打开Chrome调试
    private static boolean debugEnable = true;

    // 是否允许长按图片监听
    private static boolean imageLongClick = true;




    public static boolean isDebugEnable() {
        return debugEnable;
    }

    public static void setDebugEnable(boolean debugEnable) {
        WebviewConfig.debugEnable = debugEnable;
    }
}
