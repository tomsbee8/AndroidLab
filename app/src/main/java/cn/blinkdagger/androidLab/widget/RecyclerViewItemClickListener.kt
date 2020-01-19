package cn.blinkdagger.androidLab.widget

import android.content.Context
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnItemTouchListener

/**
 * {@see http://stackoverflow.com/questions/24471109/recyclerview-onclick/26196831#26196831}
 */
class RecyclerViewItemClickListener(context: Context?, private val mListener: OnItemClickListener) : OnItemTouchListener {
    private val mGestureDetector: GestureDetector
    private var childView: View? = null
    private var childViewPosition = 0

    interface OnItemClickListener {
        fun onItemClick(view: View?, position: Int)
        fun onItemLongPress(childView: View?, position: Int)
    }

    override fun onInterceptTouchEvent(view: RecyclerView, e: MotionEvent): Boolean {
        childView = view.findChildViewUnder(e.x, e.y)
        childViewPosition = view.getChildAdapterPosition(childView!!)
        return childView != null && mGestureDetector.onTouchEvent(e)
    }

    override fun onTouchEvent(recyclerView: RecyclerView, motionEvent: MotionEvent) {}
    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
    protected inner class GestureListener : SimpleOnGestureListener() {
        override fun onSingleTapUp(event: MotionEvent): Boolean {
            if (childView != null) {
                mListener.onItemClick(childView, childViewPosition)
            }
            return true
        }

        override fun onLongPress(event: MotionEvent) {
            if (childView != null) {
                mListener.onItemLongPress(childView, childViewPosition)
            }
        }

        override fun onDown(event: MotionEvent): Boolean { // Best practice to always return true here.
// http://developer.android.com/training/gestures/detector.html#detect
            return true
        }
    }

    init {
        mGestureDetector = GestureDetector(context, GestureListener())
    }
}