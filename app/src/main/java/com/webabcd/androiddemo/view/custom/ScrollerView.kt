package com.webabcd.androiddemo.view.custom

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Scroller

class ScrollerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : androidx.appcompat.widget.AppCompatTextView(context, attrs, defStyleAttr) {

    private var mLastX = 0F
    private var mLastY = 0F

    private var mScroller: Scroller = Scroller(context, AccelerateDecelerateInterpolator())

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                this.performClick()
                mLastX = event.x
                mLastY = event.y
            }
            MotionEvent.ACTION_MOVE -> {
                val offsetX = event.x - mLastX
                val offsetY = event.y - mLastY
                (parent as ViewGroup).scrollBy(-offsetX.toInt(), -offsetY.toInt())
            }
            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                val viewGroup = parent as ViewGroup
                mScroller.startScroll(
                    viewGroup.scrollX,
                    viewGroup.scrollY,
                    -viewGroup.scrollX,
                    -viewGroup.scrollY,
                    1000
                )
                Log.d("yyz",
                    "viewGroup.scrollX: " + viewGroup.scrollX + "\n" +
                        "viewGroup.scrollY: " + viewGroup.scrollY + "\n" +
                        "-viewGroup.scrollX: " + -viewGroup.scrollX + "\n" +
                        "-viewGroup.scrollY: " + -viewGroup.scrollY
                )
                invalidate()
            }
        }
        return true
    }

    override fun computeScroll() {
        super.computeScroll()
        if (mScroller.computeScrollOffset()) {
            (parent as ViewGroup).scrollTo(mScroller.currX, mScroller.currY)
            invalidate()
        }
    }
}
