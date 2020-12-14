package cn.blinkdagger.androidLab.http

/**
 * @Author ls
 * @Date  2020/12/14
 * @Description
 * @Version
 */
class CustomException(msg: String? = "获取数据失败") : Exception(){
    val errorMsg  = msg
}