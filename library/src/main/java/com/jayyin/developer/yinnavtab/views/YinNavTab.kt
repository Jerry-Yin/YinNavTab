package com.jayyin.developer.yinnavtab.views

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import com.jayyin.developer.yinnavtab.R
import kotlin.collections.ArrayList


/**
 * @description custom tab button
 * @author JerryYin
 * @create 2018-06-11 11:13
 * @version 1.1.0
 **/
class YinNavTab : LinearLayout {

    companion object {
        val MODE_NORMAL_TEXT = 0        //文字 + 下标
        val MODE_DRAWABLE = 1           //图片 + 文字
    }

    private val TAG = "YinNavTab.ktt"
    private var c: Context

    /** Views */
    private var mChildViews = ArrayList<TabChildView>()

    /**Values*/
    //默认水平方向
    private var mOrientation = LinearLayout.HORIZONTAL
    private var mCurMode = MODE_NORMAL_TEXT    //tab view mode :  0 -- normal text  || 1 -- drawable

    private var mDrawableIds = ArrayList<Int>()
    private var mDrawableSelectedIds = ArrayList<Int>()

    private var imageWidth: Float? = null
    private var imageHeight: Float? = null
    private var imgPaddingLeft: Float? = null
    private var imgPaddingRight: Float? = null

    //默认长度=3
    private var size = 3
    //    private var titles: List<String>? = listOf("Tab", "Tab", "Tab", "Tab", "Tab")
    private var titles = ArrayList<String>()
    private var textSize: Float = 10.0f

    private var color: Int? = Color.parseColor("#7e7d7d")
    private var colorSelected: Int? = Color.parseColor("#00b0ff")

    //默认标签line高度2dp
    private var indicatorsHeight: Float = 5F
    //默认宽度match_parent
    private var indicatorsWidth: Float = 100F
    private var indicatorsVisible = true

    private var indicatorPaddingLeft: Float? = null
    private var indicatorPaddingRight: Float? = null

    //anim asst
    private var mIsAnimoScale = false
    private var scaleValue = 5.0f

    private var mCanScroller = false    //default : can not be scroll
    private var mCanClick = false

    private var mIsScrolling = false

    private var mCurIndex = 0
    private var mOnClickListener: OnClickListener? = null
    private var mOnScrollerListener: YinNavTab.OnScrollerListener? = null


    constructor(c: Context) : super(c) {
        this.c = c
        initDefault()
        initView(c, null)
    }

    constructor(c: Context, attrs: AttributeSet) : super(c, attrs) {
        this.c = c
        initDefault()
        initView(c, attrs)
    }

    fun initDefault() {
        initDefaultTitlesDrawables()
    }

    fun initDefaultTitlesDrawables() {
        if (mDrawableIds.size > 0)
            mDrawableIds.clear()
        if (mDrawableSelectedIds.size > 0)
            mDrawableSelectedIds.clear()
        if (titles.size > 0)
            titles.clear()
        for (i in 0..size - 1) {
            if (i == 0) {
                titles.add("Home")
                mDrawableIds.add(R.drawable.ic_home_black_24dp)
                mDrawableSelectedIds.add(R.drawable.ic_home_blue_24dp)

            } else if (i == 1) {
                titles.add("Activity")
                mDrawableIds.add(R.drawable.ic_apps_black_24dp)
                mDrawableSelectedIds.add(R.drawable.ic_apps_blue_24dp)
            } else if (i == 2) {
                titles.add("Setting")
                mDrawableIds.add(R.drawable.ic_settings_black_24dp)
                mDrawableSelectedIds.add(R.drawable.ic_settings_blue_24dp)
            } else {
                titles.add("More" + i)
                mDrawableIds.add(R.drawable.ic_pets_black_24dp)
                mDrawableSelectedIds.add(R.drawable.ic_pets_blue_24dp)
            }
        }
    }

    @SuppressLint("Recycle")
    private fun initView(c: Context, attrs: AttributeSet?) {
        Log.d(TAG, "initView")
        Log.d(TAG, "childCount：" + this.childCount.toString())

        val typeArray = c.obtainStyledAttributes(attrs, R.styleable.YinNavTab)
        if (typeArray != null) {
            mCurMode = typeArray.getInteger(R.styleable.YinNavTab_mode, mCurMode)
            mOrientation = typeArray.getInteger(R.styleable.YinNavTab_orientation, mOrientation)
            color = typeArray.getColor(R.styleable.YinNavTab_color, color!!)
            colorSelected = typeArray.getColor(R.styleable.YinNavTab_colorSelected, colorSelected!!)
            indicatorsVisible = typeArray.getBoolean(R.styleable.YinNavTab_indicatorsVisible, indicatorsVisible)
            indicatorsHeight = typeArray.getDimension(R.styleable.YinNavTab_indicatorHeight, indicatorsHeight)
            indicatorsWidth = typeArray.getDimension(R.styleable.YinNavTab_indicatorWidth, indicatorsWidth)
            indicatorPaddingLeft = typeArray.getDimension(R.styleable.YinNavTab_indicatorPaddingLeft, 10.0f)
            indicatorPaddingRight = typeArray.getDimension(R.styleable.YinNavTab_indicatorPaddingRight, 10.0f)
            scaleValue = typeArray.getFloat(R.styleable.YinNavTab_scaleValue, scaleValue)
            size = typeArray.getInt(R.styleable.YinNavTab_tabSize, size)
            textSize = typeArray.getDimension(R.styleable.YinNavTab_textSize, textSize)
            imageHeight = typeArray.getDimension(R.styleable.YinNavTab_imageHeight, 78.0f)
            imageWidth = typeArray.getDimension(R.styleable.YinNavTab_imageWidth, 78.0f)
            imgPaddingLeft = typeArray.getDimension(R.styleable.YinNavTab_imgPaddingLeft, 10.0f)
            imgPaddingRight = typeArray.getDimension(R.styleable.YinNavTab_imgPaddingRight, 10.0f)
            mIsAnimoScale = typeArray.getBoolean(R.styleable.YinNavTab_isScale, false)
        }

        if (size != mDrawableIds.size) {
            initDefaultTitlesDrawables()
        }
        this.orientation = mOrientation

        resetView()
    }


    fun resetView(){
        this.orientation = mOrientation
        if (this.childCount > 0) {
            this.removeAllViews()
            mChildViews.clear()
            Log.d(TAG, " removed & now childCount：" + this.childCount.toString())
        }

        for (i in 0..size - 1) {
            val childView = TabChildView(c, mCurMode)
            //mode
            childView.setMode(mCurMode)
            //set drawable
            if (mOrientation == LinearLayout.VERTICAL) {
                childView.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1.0f)
                childView.setViewOrientation(HORIZONTAL)
            } else if (mOrientation == LinearLayout.HORIZONTAL) {
                childView.layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f)
                childView.setViewOrientation(VERTICAL)
            }

            if (mCurMode == MODE_DRAWABLE) {
                indicatorsVisible = false
                try {
                    childView.setDrawable(mDrawableIds[i])
                } catch (e: IndexOutOfBoundsException) {
                    childView.setDrawable(R.drawable.ic_pets_black_24dp)
                }
                childView.setImageSize(imageWidth!!, imageHeight!!, imgPaddingLeft, imgPaddingRight)
            }
            childView.setText(titles[i], textSize, color!!)
            if (mCurMode == MODE_NORMAL_TEXT) {
                childView.setIndicatorColor(color!!)
                childView.setIndicatorVisibility(indicatorsVisible)
                childView.setIndicatorSize(indicatorsWidth, indicatorsHeight)
                childView.setIndicatorPadding(indicatorPaddingLeft!!, indicatorPaddingRight!!)
            }

            mChildViews.add(childView)
            mCurIndex = 0
            if (mOnClickListener != null) {
                setOnClickListener(mOnClickListener!!)
            }
        }

        refreshColor(0)
        for (v in mChildViews)
            this.addView(v)
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        Log.d(TAG, "omMeasure")
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
//        initView(c)
        Log.d(TAG, "omDraw")
    }


    private fun refreshColor(index: Int) {
        for (i in 0..mChildViews.size - 1) {
            if (i == index) {
                //selected view
                mChildViews[i].setTextColor(colorSelected)
                mChildViews[i].setIndicatorVisibility(true)
                mChildViews[i].setIndicatorColor(colorSelected)
                mChildViews[i].setDrawable(mDrawableSelectedIds[i])
                if (mIsAnimoScale) {
                    mChildViews[i].setTextSize(textSize.plus(scaleValue))
                }
            } else {
                // unSelected views
                mChildViews[i].setTextColor(color)
                mChildViews[i].setIndicatorVisibility(false)
                mChildViews[i].setDrawable(mDrawableIds[i])
                if (mIsAnimoScale) {
                    mChildViews[i].setTextSize(textSize)
                }
            }

            if (mIsAnimoScale && index > 0 && index < mChildViews.size - 1) {
                if (i == index - 1 || i == index + 1) {
                    mChildViews[i].setTextSize(textSize.plus(scaleValue / 2))
                }
            }
        }
    }

    fun setOnClickListener(listener: OnClickListener) {
        this.mCanClick = true
        this.mOnClickListener = listener
        for (i in 0..mChildViews.size - 1) {
            mChildViews[i].setOnClickListener(object : View.OnClickListener {
                override fun onClick(p0: View?) {
                    if (p0 != null) {
                        listener.onClick(p0, i)
                        mCurIndex = i
                        refreshColor(i)
                    }
                }
            })
        }
    }

    fun setOnScrollerListener(listener: OnScrollerListener) {
        this.mCanScroller = true
        this.mOnScrollerListener = listener
    }

    var posX: Float = 0.toFloat()
    var posY: Float = 0.toFloat()
    var curPosX: Float = 0.toFloat()
    var curPosY: Float = 0.toFloat()
    var minLength = 25
    //    var min = ViewConfiguration.get(getContext()).getScaledTouchSlop()    // = 20
    var isMove = false

    //拦截触摸事件
    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                posX = event.x
                posY = event.y
                Log.d(TAG, "posX=" + posX)
                Log.d(TAG, "posY=" + posY)
            }
            MotionEvent.ACTION_MOVE -> {
                curPosX = event.x
                curPosY = event.y
                Log.d(TAG, "curPosX-move=" + curPosX)
                Log.d(TAG, "curPosY-move=" + curPosY)
                isMove = true
            }
            MotionEvent.ACTION_UP -> {
                if (mOrientation == LinearLayout.VERTICAL) {
                    var sy = curPosY - posY
                    Log.d(TAG, "sy = " + sy)
                    if (isMove && !curPosY.equals(0.toFloat()) && Math.abs(sy) > minLength) {
                        mIsScrolling = true
                        if (sy > 0) {
                            //向下滑動
                            Log.d(TAG, "向下滑动")
                            scrollerRefresh(true)
                        } else if (sy < 0) {
                            //向上滑动
                            Log.d(TAG, "向上滑动")
                            scrollerRefresh(false)
                        }
                        return true
                    }
                } else if (mOrientation == LinearLayout.HORIZONTAL) {
                    var sx = curPosX - posX
                    Log.d(TAG, "curPosX1=" + curPosX)
                    Log.d(TAG, "curPosY1=" + curPosY)
                    Log.d(TAG, "sx = " + sx)
                    if (isMove && !curPosX.equals(0.toFloat()) && Math.abs(sx) > minLength) {
                        mIsScrolling = true
                        if (sx > 0) {
                            //向下滑動
                            Log.d(TAG, "向右滑动")
                            scrollerRefresh(true)
                        } else if (sx < 0) {
                            //向上滑动
                            Log.d(TAG, "向左滑动")
                            scrollerRefresh(false)
                        }
                        return true
                    }
                }
                resetPosition()
            }
        }
        Log.d(TAG, "scrolling=" + mIsScrolling)
        return false
    }

    fun resetPosition(){
        posX = 0.0f
        posY = 0.0f
        curPosX = 0.0f
        curPosY = 0.0f
        mIsScrolling = false
        isMove = false
    }

    private fun scrollerRefresh(add: Boolean) {
        Log.d(TAG, "mcurindex0 =" + mCurIndex)
        var needRefresh = false
        if (add) {
            if (mCurIndex < mChildViews.size - 1) {
                mCurIndex++
                needRefresh = true
            }
        } else {
            if (mCurIndex > 0) {
                mCurIndex--
                needRefresh = true
            }
        }
        if (needRefresh && mCanScroller) {
            refreshColor(mCurIndex)
            if (mOnScrollerListener != null)
                mOnScrollerListener!!.onScroller(mChildViews[mCurIndex], mCurIndex)
        }
        Log.d(TAG, "mcurindex1 =" + mCurIndex)
        resetPosition()
    }


    /***--- 属性设置 --******/

    fun indicatorsVisible(show: Boolean) {
        this.indicatorsVisible = show
        resetView()
    }

    fun setIndicatorSize(width: Float, height: Float) {
        this.indicatorsWidth = width
        this.indicatorsHeight = height
        resetView()
    }

    fun setIndicatorPadding(paddingLeft: Float, paddingRight: Float) {
        this.indicatorPaddingLeft = paddingLeft
        this.indicatorPaddingRight = paddingRight
        resetView()
    }


    /**
     *
     * LinearLayout.HORIZONTAL
     * LinearLayout.VERTICAL
     */
    fun setTabOrientation(o: Int) {
        this.mOrientation = o
        resetView()
    }

    fun setColor(color: Int) {
        this.color = color
//        this.invalidate()
        resetView()
    }

    fun setSelectedColor(color: Int) {
        this.colorSelected = color
        resetView()
    }

    fun setTitles(titles: ArrayList<String>) {
        this.titles = titles
        this.size = titles.size
        resetView()
    }

    fun setTitle(title: String, index: Int) {
        if (size > 0 && index <= size - 1) {
            titles[index] = title
            resetView()
        }
    }

    fun setDrawables(drawableIds: ArrayList<Int>, drawableSelectedIds: ArrayList<Int>) {
        if (drawableIds.size > 0 &&
            drawableSelectedIds.size > 0 &&
            drawableIds.size == drawableSelectedIds.size &&
            drawableIds.size == size) {
            mDrawableIds.clear()
            mDrawableSelectedIds.clear()
            for (i in 0..size - 1) {
                mDrawableIds.add(drawableIds[i])
                mDrawableSelectedIds.add(drawableSelectedIds[i])
            }
            resetView()
        }
    }

    fun setDrawableSize(position: Int, width: Float, height: Float, paddingLeft: Float?, paddingRight: Float?) {
        if(mDrawableIds.size > position)
            mChildViews.get(position).setImageSize(width, height, paddingLeft, paddingRight)
    }

    fun canScroller(can: Boolean) {
        this.mCanScroller = can
    }

    fun getMode(): Int {
        return mCurMode
    }

    fun setMode(mode: Int) {
        this.mCurMode = mode
        resetView()
    }

    interface OnClickListener : View.OnClickListener {

        override fun onClick(p0: View?){}

        fun onClick(view: View, position: Int)
    }

    interface OnScrollerListener {
        fun onScroller(view: View, position: Int)
    }
}