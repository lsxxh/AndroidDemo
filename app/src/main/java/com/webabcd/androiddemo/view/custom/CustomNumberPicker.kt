package com.webabcd.androiddemo.view.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.ViewConfiguration
import android.widget.LinearLayout

private const val DEFAULT_ITEM_HEIGHT = 80
private const val TEXT_GAP = 30

class CustomNumberPicker : LinearLayout {
    private var mInitOffset: Int = 0
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
        mTextPaint.apply {
            isAntiAlias = true
            color = Color.BLACK
            textSize = 50f
        }
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

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        mInitOffset = DEFAULT_ITEM_HEIGHT + TEXT_GAP.also { mCurrentScrollOffset = it }
        Log.d("yyz", "#onLayout mInitOffset: $mInitOffset")
    }

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
                //(this.parent as ViewGroup).scrollBy(0, y - mLastY)
                scrollBy(0, y - mLastY)
                Log.d("yyz", "(offset) y-mLastY: ${y - mLastY}")
                invalidate()
                mLastX = x
                mLastY = y
            }
        }
        return true
    }

    override fun scrollBy(x: Int, y: Int) {
        super.scrollBy(x, y)
        if (y > 0) {
            if (mCurrentScrollOffset - mInitOffset > DEFAULT_ITEM_HEIGHT + TEXT_GAP) {
                decreaseNum()
            }
        } else {
            if (mCurrentScrollOffset - mInitOffset > DEFAULT_ITEM_HEIGHT + TEXT_GAP) {
                increaseNum()
            }
        }
    }

    private fun increaseNum() {
        for (i in 0 until mNumbers.size - 1) {
            mNumbers[i] = mNumbers[i + 1]
            mNumbers[mNumbers.size - 1] = mNumbers[0]
        }
    }

    private fun decreaseNum() {
        val end = mNumbers[mNumbers.size - 1]
        for (i in mNumbers.size - 1 downTo 1) { //含1
            mNumbers[i] = mNumbers[i - 1]
            mNumbers[0] = end
        }
    }

    /* override fun computeScroll() {
        Log.d("yyz", "#computeScroll")
        // 先判断scroller滚动是否完成
            val scroller = Scroller(context, null)
            if (scroller.computeScrollOffset()) {
              // 这里调用View的scrollTo()完成实际的滚动
              scrollTo( scroller.currX, scroller .currY)
                Log.d("yyz", "#scrollTo (${scroller.currX}, ${scroller.currY})")
              // 必须调用该方法，否则不一定能看到滚动效果
              invalidate();
            }
        super.computeScroll()
    }*/

    override fun getTopFadingEdgeStrength(): Float {
        return 1f
    }

    override fun getBottomFadingEdgeStrength(): Float {
        return 1f
    }
}