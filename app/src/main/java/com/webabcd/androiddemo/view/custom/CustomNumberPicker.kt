package com.webabcd.androiddemo.view.custom

import android.content.Context
import android.graphics.*
import android.graphics.Color.parseColor
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.ViewConfiguration
import android.widget.LinearLayout

private const val DEFAULT_ITEM_HEIGHT = 80
private const val DEFAULT_ITEM_COUNT = 5
private const val TEXT_SIZE = 50f
//private const val MIDDLE_TEXT_SIZE = 50f
private const val MIDDLE_TEXT_SCALE = 1.5f

//Const 'val' has type 'Drawable'. Only primitives and String are allowed
//private const val MIDDLE_HIGHLIGHT_DRAWABLE = Drawable()

//Const 'val' should not have a delegate
private val BACKGROUND_MIDDLE_COLOR_MIDDLE_DEFAULT by lazy { parseColor("#99FFD700") } //#FFD700: <!--金色 -->
private val BACKGROUND_MIDDLE_COLOR_START_DEFAULT by lazy { parseColor("#00000000")}
private val BACKGROUND_MIDDLE_COLOR_END_DEFAULT by lazy { parseColor("#00000000")}

class CustomNumberPicker : LinearLayout {
    private val isMiddleGradient = true
    private lateinit var mMiddleGradientRect: Rect
    private lateinit var mBackgroundGradient: LinearGradient
    private var gap = 30
    private val middleItemIndex = (DEFAULT_ITEM_COUNT - 1) / 2

    private var mMaxValue = 12
    private var mMinValue = 0

    private var mInitOffset: Int = 0
    private var mCurrentScrollOffset = 0
    private val mTextPaint = Paint()
    private var mMiddleTextPaint = Paint()
    private var mTouchSlop = 0
    private val mNumbers = arrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)

    private var mWidth = 200
    private var mHeight = 800
    //private val x = mWidth / 2

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : super(context, attributeSet, defStyleAttr){
        init()
        initPaint()
    }

    private fun init() {
        setWillNotDraw(false)
        setPadding(20, 0, 20, 0)
        mTouchSlop = ViewConfiguration.get(context).scaledTouchSlop
        isVerticalFadingEdgeEnabled = true
    }

    private fun initPaint() {
        mTextPaint.apply {
            isAntiAlias = true
            color = Color.BLACK
            textSize = 50f
        }

        Log.d("yyz", "mHeight: $mHeight") //yyz: mHeight: 800说明调用在onMeasure之前

        mMiddleTextPaint.apply {
            isAntiAlias = true
            color = Color.BLACK
            textSize = TEXT_SIZE * MIDDLE_TEXT_SCALE
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val x = mWidth / 2
        var y = mCurrentScrollOffset
        val gap = (mHeight - DEFAULT_ITEM_HEIGHT * DEFAULT_ITEM_COUNT) / (DEFAULT_ITEM_COUNT - 1)
        /*for (num in mNumbers) {
            canvas.drawText(num.toString(), x.toFloat(), y.toFloat(), mTextPaint)
            y += DEFAULT_ITEM_HEIGHT + gap
        }*/
        for (index in mNumbers.indices) {
            if (index != middleItemIndex) {
                canvas.drawText(mNumbers[index].toString(), x.toFloat(), y.toFloat(), mTextPaint)
            } else {
                //Avoid object allocations during draw/layout operations (preallocate and reuse instead)
                /*val backgroundGradient = LinearGradient(0f, y.toFloat(), mWidth.toFloat(), y.toFloat(),
                    intArrayOf(BACKGROUND_MIDDLE_COLOR_START_DEFAULT, BACKGROUND_MIDDLE_COLOR_MIDDLE_DEFAULT, BACKGROUND_MIDDLE_COLOR_END_DEFAULT),
                    null, Shader.TileMode.CLAMP
                )*/
                canvas.drawText(mNumbers[index].toString(), x.toFloat(), y.toFloat(), mMiddleTextPaint)
                val textWidth = mMiddleTextPaint.measureText(mNumbers[index].toString()).toInt()

                if (isMiddleGradient) {
                    mMiddleTextPaint.shader = mBackgroundGradient
                    canvas.drawRect(mMiddleGradientRect, mMiddleTextPaint)
                }
            }
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
        updateRect()
        initGradient()
    }

    private fun updateRect() {
        Log.d("yyz", "#updateRect measuredHeight: $measuredHeight") //yyz: #updateRect measuredHeight: 682(不变)
        if (isMiddleGradient) {
            mMiddleGradientRect = Rect(0, top + measuredHeight / 2 - DEFAULT_ITEM_HEIGHT / 2,
                measuredWidth, top + measuredHeight / 2 + DEFAULT_ITEM_HEIGHT / 2)
        }
    }

    private fun initGradient() {
        if (!isMiddleGradient) return
        val y = measuredHeight / 2
        mBackgroundGradient = LinearGradient(0f, y.toFloat(), measuredWidth.toFloat(), y.toFloat(),
            intArrayOf(BACKGROUND_MIDDLE_COLOR_START_DEFAULT, BACKGROUND_MIDDLE_COLOR_MIDDLE_DEFAULT, BACKGROUND_MIDDLE_COLOR_END_DEFAULT),
            null, Shader.TileMode.CLAMP
        )
    }

    private fun measureHeight(defaultHeight: Int, heightMeasureSpec: Int): Int {
        var defaultH = 0
        val specMode = MeasureSpec.getMode(heightMeasureSpec)
        val specSize = MeasureSpec.getSize(heightMeasureSpec)
        Log.d("yyz", "#measureHeight specMode: $specMode, specSize: $specSize")
        when (specMode) {
            MeasureSpec.EXACTLY -> defaultH = specSize
            MeasureSpec.UNSPECIFIED -> defaultH = defaultHeight.coerceAtLeast(specSize)
            MeasureSpec.AT_MOST -> defaultH = DEFAULT_ITEM_COUNT * DEFAULT_ITEM_HEIGHT + (DEFAULT_ITEM_COUNT - 1) * gap + paddingTop + paddingBottom
        }
        mHeight = defaultH
        Log.d("yyz", "defaultH: $defaultH")
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
        mWidth = defaultW
        Log.d("yyz", "defaultW: $defaultW")
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