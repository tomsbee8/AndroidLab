package cn.blinkdagger.androidLab.base

import androidx.lifecycle.*
import cn.blinkdagger.androidLab.entity.BaseResponse
import kotlinx.coroutines.*

/**
 * @Author ls
 * @Date  2020/10/13
 * @Description
 * @Version
 */
open class BaseViewModel : ViewModel(), LifecycleObserver {

    private val errorLiveData by lazy { MutableLiveData<Exception>() }

    private val completeLiveData by lazy { MutableLiveData<Int>() }

    //运行在UI线程的协程
    fun launchUI(block: suspend CoroutineScope.() -> Unit) = viewModelScope.launch {
        try {
            withTimeout(30000) {
                block()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            errorLiveData.value = e
        } finally {
            completeLiveData.value = 200
        }
    }

    fun launchUpload(block: suspend CoroutineScope.() -> Unit) = viewModelScope.launch {
        try {
            block()
        } catch (e: Exception) {
            e.printStackTrace()
            errorLiveData.value = e
        } finally {
            completeLiveData.value = 200
        }
    }


    @ExperimentalCoroutinesApi
    fun <T1, T2> launchUI(
            block1: suspend CoroutineScope.() -> BaseResponse<T1>,
            block2: suspend CoroutineScope.() -> BaseResponse<T2>,
            handle: (t1: T1? , t2: T2?) -> Unit
    ) = viewModelScope.launch {
        try {
            val liveData1 = MutableLiveData<T1>()
            val liveData2 = MutableLiveData<T2>()
            withTimeout(30000) {
                val rs1 = async {
                    liveData1.value = block1().data
                    return@async liveData1
                }
                val rs2 = async {
                    liveData2.value = block2().data
                    return@async liveData2
                }
                handle(rs1.await().value, rs2.await().value)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            errorLiveData.value = e
        } finally {
            completeLiveData.value = 200
        }
    }

    /**
     * 请求失败，出现异常
     */
    fun getErrorLiveData(): LiveData<Exception> {
        return errorLiveData
    }

    /**
     * 请求完成，在此处做一些关闭操作
     */
    fun getCompleteLiveData(): LiveData<Int> {
        return completeLiveData
    }
}