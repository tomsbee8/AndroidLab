import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * @Author ls
 * @Date  2019-10-31
 * @Description
 * @Version
 */
class StickyRecyclerView : RecyclerView {

    private val CLICK_DISTANCE = 3f

    /**
     * 悬停的View
     */
    private var mStickyView: View? = null

    /**
     * touchDown时ScrollView滚动的坐标
     */
    private var mTouchDownY = java.lang.Float.MIN_VALUE

    /**
     * 从touchDown到touchUp,ScrollView滚动的距离
     */
    private var mScrollDistanceY = 0f

    /**
     * 当前是否为悬停状态
     */
    private var isCurrentSticky = false

    /**
     * 滚动时候悬停监听
     */
    private var mCallback: ScrollCallback? = null

    /**
     * 滚动时候悬停状态监听
     */
    var onStateChangeListener: ScrollStateCallback? = null

    constructor(context: Context) : super(context) {
        addListener()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        addListener()
    }

    private fun addListener() {
        addOnScrollListener(object : OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                mCallback?.onScrolled()
            }
        })
    }


    /**
     * 设置悬停交互功能
     * @param stickyView 需要选悬停的控件View
     * @param stickyPosition 悬停PlaceHolder的位置position
     *
     */
    fun setUpViewSticky(stickyView: View, stickyPosition: Int) {
        this.mStickyView = stickyView

        this.mStickyView?.let { it ->
            // 监听onTouch事件有两方面的考虑，scrollView的滚动与点击事件
            it.setOnTouchListener(object : OnTouchListener {
                override fun onTouch(v: View, event: MotionEvent): Boolean {

                    when (event.action) {
                        MotionEvent.ACTION_DOWN// 记录下当前的滚动位置
                        -> mTouchDownY = this@StickyRecyclerView.scrollY.toFloat()
                        MotionEvent.ACTION_MOVE// ScrollView滚动的过程中，记录滚动的差值
                        -> {
                            val disY = Math.abs(this@StickyRecyclerView.scrollY - mTouchDownY)
                            // 如果当前差值大于之前记录差值，将差值替换
                            if (disY > mScrollDistanceY) {
                                mScrollDistanceY = disY
                            }
                        }
                    }

                    val translateY = v.translationY
                    // touch的坐标都是相对于本控件的，所以需要做一次转换
                    // 构建一个新的MotionEvent事件
                    val newEvent = MotionEvent.obtain(event)
                    newEvent.setLocation(newEvent.x, newEvent.y + translateY)
                    this@StickyRecyclerView.dispatchTouchEvent(newEvent)
                    newEvent.recycle()

                    // 如果是滚动，那么不让其触发onClick 事件
                    if (mScrollDistanceY > CLICK_DISTANCE && event.action == MotionEvent.ACTION_UP) {
                        mScrollDistanceY = 0f
                        return true
                    }
                    return false
                }
            })

            mCallback = object : ScrollCallback {
                override fun onScrolled() {
                    layoutManager?.findViewByPosition(stickyPosition)?.let { placeHolderView ->
                        // 首先计算移动的距离
                        val translationY = Math.max(0, placeHolderView.top - this@StickyRecyclerView.scrollY)

                        val sticky = translationY == 0
                        if (sticky != isCurrentSticky) {
                            onStateChangeListener?.onStickyStateChanged(sticky)
                            isCurrentSticky = sticky
                        }
                        // 移动
                        mStickyView?.translationY = translationY.toFloat()
                    }
                }
            }
        }
    }

    /**
     * 重设悬停PlaceHolder的位置position
     * @param stickyPosition 悬停PlaceHolder的位置position
     */
    fun notifyStickyPositionChanged(stickyPosition: Int) {
        if (mStickyView == null) {
            return
        }

        layoutManager?.findViewByPosition(stickyPosition)?.let { placeHolderView ->
            mCallback = object : ScrollCallback {
                override fun onScrolled() {
                    // 首先计算移动的距离
                    val translationY = Math.max(0, placeHolderView.top - this@StickyRecyclerView.scrollY)

                    val sticky = translationY == 0
                    if (sticky != isCurrentSticky) {
                        onStateChangeListener?.onStickyStateChanged(sticky)
                        isCurrentSticky = sticky
                    }
                    // 移动
                    mStickyView?.translationY = translationY.toFloat()
                }
            }
            mCallback?.onScrolled()
        }
    }

    private interface ScrollCallback {
        /**
         * 监听recycler 滚动
         */
        fun onScrolled()
    }

    interface ScrollStateCallback {

        /**
         * 监听悬停状态改变
         * @param sticky 是否已经悬停
         */
        fun onStickyStateChanged(sticky : Boolean)
    }

}