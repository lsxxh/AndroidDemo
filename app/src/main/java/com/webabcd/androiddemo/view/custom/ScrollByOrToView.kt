package com.webabcd.androiddemo.view.custom

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewGroup

class ScrollByOrToView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : androidx.appcompat.widget.AppCompatTextView(context, attrs, defStyleAttr) {

    private var mLastX = 0F
    private var mLastY = 0F

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
                (parent as ViewGroup).scrollTo(0, 0)
            }
        }
        return true
    }
}
