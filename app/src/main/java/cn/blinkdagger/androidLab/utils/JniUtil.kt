package cn.blinkdagger.androidLab.utils

/**
 * @Author ls
 * @Date 2018/12/11
 * @Description
 * @Version
 */
object JniUtil {
    /**
     * 将用C++代码实现，在android代码中调用的方法：获取当前app的包名
     *
     * @return
     */
    external fun stringFromJNI(): String?

    external fun addFromJNI(a: Int, b: Int): Int

    /**
     * 加载so库或jni库，在使用到该库之前加载就行，不一定非要写在这个类内
     * 系统自己会判断扩展名是dll还是so,这里加载libJNI_ANDROID_TEST.so
     */
    init {
        System.loadLibrary("JNI_ANDROID_SAMPLE")
    }
}