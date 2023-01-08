package com.webabcd.androiddemo.view.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.ViewConfiguration
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Scroller

private const val DEFAULT_ITEM_HEIGHT = 50
private const val TEXT_GAP = 10

class CustomNumberPicker : LinearLayout {
    private var mCurrentScrollOffset = 0
    private val mTextPaint = Paint()
    private var mTouchSlop: Int
    private val mNumbers = arrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16)
    private val mWidth = 400
    private val x = mWidth / 2

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : super(context, attributeSet, defStyleAttr){
        setWillNotDraw(false)
        mTextPaint.isAntiAlias = true
        mTextPaint.color = Color.BLACK
        mTextPaint.textSize = 50f
        mTouchSlop = ViewConfiguration.get(context).scaledTouchSlop
        isVerticalFadingEdgeEnabled = true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        var y = mCurrentScrollOffset
        for (num in mNumbers) {
            canvas.drawText(num.toString(), x.toFloat(), y.toFloat(), mTextPaint)
            y += DEFAULT_ITEM_HEIGHT + TEXT_GAP
        }
    }

//    override fun computeScroll() {
//        Log.d("yyz", "#computeScroll")
//        // 先判断scroller滚动是否完成
//            val scroller = Scroller(context, null)
//            if (scroller.computeScrollOffset()) {
//              // 这里调用View的scrollTo()完成实际的滚动
//              scrollTo( scroller.currX, scroller .currY)
//                Log.d("yyz", "#scrollTo (${scroller.currX}, ${scroller.currY})")
//              // 必须调用该方法，否则不一定能看到滚动效果
//              invalidate();
//            }
//        super.computeScroll()
//    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return true
    }
    private var mLastX = 0
    private var mLastY = 0
    override fun onTouchEvent(event: MotionEvent): Boolean {

        Log.d("yyz", "action: ${event.action}")
        val x = event.x.toInt()
        val y = event.y.toInt()
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                mLastX = x
                mLastY = y
            }
            MotionEvent.ACTION_MOVE -> {
                Log.d("yyz", "y: $y, mLastY: $mLastY")
                mCurrentScrollOffset += y - mLastY
                (this.parent as ViewGroup).scrollBy(0, y - mLastY)
                invalidate()
                mLastX = x
                mLastY = y
            }
        }
        return true
    }

    override fun getTopFadingEdgeStrength(): Float {
        return 1f
    }

    override fun getBottomFadingEdgeStrength(): Float {
        return 1f
    }
}