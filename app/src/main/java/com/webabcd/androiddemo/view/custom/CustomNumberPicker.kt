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
private const val DEFAULT_ITEM_COUNT = 5
class CustomNumberPicker : LinearLayout {
    private var gap = 30

    private var mMaxValue = 12
    private var mMinValue = 0

    private var mInitOffset: Int = 0
    private var mCurrentScrollOffset = 0
    private val mTextPaint = Paint()
    private var mTouchSlop = 0
    private val mNumbers = arrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)
    private val mWidth = 400
    private val x = mWidth / 2

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : super(context, attributeSet, defStyleAttr){
        init()
        initPaint()
    }

    private fun init() {
        setWillNotDraw(false)
        mTouchSlop = ViewConfiguration.get(context).scaledTouchSlop
        isVerticalFadingEdgeEnabled = true
    }

    private fun initPaint() {
        mTextPaint.apply {
            isAntiAlias = true
            color = Color.BLACK
            textSize = 50f
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        var y = mCurrentScrollOffset
        for (num in mNumbers) {
            canvas.drawText(num.toString(), x.toFloat(), y.toFloat(), mTextPaint)
            y += DEFAULT_ITEM_HEIGHT + gap
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        mInitOffset = DEFAULT_ITEM_HEIGHT + gap.also { mCurrentScrollOffset = it }
        Log.d("yyz", "#onLayout mInitOffset: $mInitOffset")
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(measureWidth(suggestedMinimumWidth, widthMeasureSpec),
            measureHeight(suggestedMinimumHeight, heightMeasureSpec))
    }

    private fun measureHeight(defaultHeight: Int, heightMeasureSpec: Int): Int {
        var defaultH = 0
        val specMode = MeasureSpec.getMode(heightMeasureSpec)
        val specSize = MeasureSpec.getSize(heightMeasureSpec)
        Log.d("yyz", "#measureHeight specMode: $specMode, specSize: $specSize")
        when (specMode) {
            MeasureSpec.EXACTLY -> defaultH = specSize
            MeasureSpec.UNSPECIFIED -> defaultH = defaultHeight.coerceAtLeast(specSize)
            MeasureSpec.AT_MOST -> defaultH = DEFAULT_ITEM_COUNT * DEFAULT_ITEM_HEIGHT + paddingTop + paddingBottom
        }
        return defaultH
    }

    private fun measureWidth(defaultWidth: Int, widthMeasureSpec: Int): Int {
        var defaultW = 0
        val specMode = MeasureSpec.getMode(widthMeasureSpec)
        val specSize = MeasureSpec.getSize(widthMeasureSpec)
        Log.d("yyz", "#measureWidth specMode: $specMode, specSize: $specSize")
        when (specMode) {
            MeasureSpec.EXACTLY -> defaultW = specSize
            //Should be replaced with Kotlin function
            //MeasureSpec.UNSPECIFIED -> defaultW = Math.max(defaultWidth, specSize)
            MeasureSpec.UNSPECIFIED -> defaultW = defaultWidth.coerceAtLeast(specSize)
            MeasureSpec.AT_MOST -> defaultW = mTextPaint.measureText(mMaxValue.toString()).toInt() + paddingStart + paddingEnd
        }
        return defaultW
    }
    //coerceAtLeast definition:
    //public fun Int.coerceAtLeast(minimumValue: Int): Int {
    //    return if (this < minimumValue) minimumValue else this
    //}
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
        Log.d("yyz", "y: $y, mCurrentScrollOffset: $mCurrentScrollOffset, mInitOffset: $mInitOffset, mCurrentScrollOffset - mInitOffset: ${mCurrentScrollOffset - mInitOffset}")
        if (y > 0) {
            if (mCurrentScrollOffset - mInitOffset > DEFAULT_ITEM_HEIGHT + gap) {
                decreaseNum()
            }
        } else {
            //y < 0是往上翻,向下滚动,mCurrentScrollOffset(手指滑动距离,上+下-)减小(+->-)
            //if (mCurrentScrollOffset - mInitOffset > DEFAULT_ITEM_HEIGHT + TEXT_GAP) {
            if (mInitOffset - mCurrentScrollOffset > DEFAULT_ITEM_HEIGHT + gap) {
                increaseNum()
            }
        }
    }

    private fun increaseNum() {
        val start = mNumbers[0] //#1
        for (i in 0 until mNumbers.size - 1) {
            mNumbers[i] = mNumbers[i + 1]
            //mNumbers[mNumbers.size - 1] = mNumbers[0]
            mNumbers[mNumbers.size - 1] = start
        }
        //#increaseNum mNumbers.contentToString(): [13, 14, 2, 3, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 13]
        //#increaseNum mNumbers.contentToString(): [14, 2, 3, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 14]
        //#1优化后#increaseNum mNumbers.contentToString(): [2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 1, 1]
        Log.d("yyz", "#increaseNum mNumbers.contentToString(): ${mNumbers.contentToString()}")
    }


    private fun decreaseNum() {
        val end = mNumbers[mNumbers.size - 1]
        for (i in mNumbers.size - 1 downTo 1) { //含1
            mNumbers[i] = mNumbers[i - 1]
            mNumbers[0] = end
        }
        //?2:前2会重复#decreaseNum mNumbers.contentToString(): [13, 13, 14, 15, 16, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12]
        Log.d("yyz", "#decreaseNum mNumbers.contentToString(): ${mNumbers.contentToString()}")
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

    fun setMaxValue(maxValue: Int) {
        mMaxValue = maxValue
    }

    fun setMinValue(minValue: Int) {
        mMinValue = minValue
    }
}