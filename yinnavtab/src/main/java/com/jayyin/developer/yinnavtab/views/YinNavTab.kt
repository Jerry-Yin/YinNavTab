package com.jayyin.developer.yinnavtab.views

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.widget.LinearLayout
import android.widget.TextView
import com.jayyin.developer.yinnavtab.R
import java.util.*


/**
 * @description custom tab button
 * @author JerryYin
 * @create 2018-06-11 11:13
 * @version 1.0.2
 **/
class YinNavTab : LinearLayout {

    private val TAG = "YinNavTab.ktt"
    private var c: Context

//    private var textView: TextView? = null
//    private var line: View? = null

//    private var parentLayout: LinearLayout? = null
//    private var childLayout: RelativeLayout? = null


    //画笔
//    private var paint: Paint = Paint()
    //默认水平方向
    private var mOrientation = LinearLayout.HORIZONTAL
    private var mChildViews = ArrayList<LinearLayout>()

    //默认长度=3
    private var size = 3
    //    private var titles: List<String>? = listOf("Tab", "Tab", "Tab", "Tab", "Tab")
    private var titles = ArrayList<String>()
    private var textSize: Float? = 10.0f

    private var color: Int? = Color.parseColor("#7e7d7d")
    private var colorSelected: Int? = Color.parseColor("#00b0ff")

    //默认标签line高度2dp
    private var indicatorsHeight: Float = 5F
    //默认宽度match_parent
    private var indicatorsWidth: Float = 100F
    private var indicatorsVisible = true

    //anim asst
    private var mIsAnimoScale = false
    private var scaleValue = 5.0f

    private var mCanScroller = false    //default : can not be scroll

    private var mIsScrolling = false

    private var mCurIndex = 0
    private var mOnClickListener: OnClickListener? = null
    private var mOnScrollerListener: OnScrollerListener? = null


    constructor(c: Context) : super(c) {
        this.c = c
        initView(c, null)
    }

    constructor(c: Context, attrs: AttributeSet) : super(c, attrs) {
        this.c = c
        initView(c, attrs)
    }


    @SuppressLint("Recycle")
    private fun initView(c: Context, attrs: AttributeSet?) {
        Log.d(TAG, "initView")
        Log.d(TAG, "childCount：" + this.childCount.toString())

        if (this.childCount > 0) {
            this.removeAllViews()
            mChildViews.clear()
            Log.d(TAG, " removed & now childCount：" + this.childCount.toString())
        }

        val typeArray = c.obtainStyledAttributes(attrs, R.styleable.YinNavTab)
        if (typeArray != null) {
            color = typeArray.getColor(R.styleable.YinNavTab_color, color!!)
            colorSelected = typeArray.getColor(R.styleable.YinNavTab_colorSelected, colorSelected!!)
            indicatorsVisible = typeArray.getBoolean(R.styleable.YinNavTab_indicatorsVisible, indicatorsVisible)
            indicatorsHeight = typeArray.getDimension(R.styleable.YinNavTab_indicatorHeight, indicatorsHeight)
            indicatorsWidth = typeArray.getDimension(R.styleable.YinNavTab_indicatorWidth, indicatorsWidth)
            scaleValue = typeArray.getFloat(R.styleable.YinNavTab_scaleValue, scaleValue)
            size = typeArray.getInt(R.styleable.YinNavTab_tabSize, 3)
            textSize = typeArray.getDimension(R.styleable.YinNavTab_titleSize, 12.0f)
            mIsAnimoScale = typeArray.getBoolean(R.styleable.YinNavTab_isScale, false)
            titles.clear()
            for (i in 0..size - 1) {
                titles.add("Tab" + i)
            }
            //default: horizontal
//            mOrientation = typeArray.getInt(R.styleable.YinNavTab_orientation, LinearLayout.HORIZONTAL)
        }

        mOrientation = this.orientation
        if (mOrientation == LinearLayout.VERTICAL)
        //VERTICAL DO NOT SHOW INDICATORS
            indicatorsVisible = false

        for (i in 0..size - 1) {

            val childView = LinearLayout(c)
            if (mOrientation == LinearLayout.VERTICAL) {
                childView.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1.0f)
            } else if (mOrientation == LinearLayout.HORIZONTAL) {
                childView.layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f)
            }
            childView.orientation = LinearLayout.VERTICAL
            childView.gravity = Gravity.CENTER_HORIZONTAL

            //文字
            val text = TextView(c)
            text.text = titles!![i]
            if (textSize != null)
                text.textSize = textSize!!
            text.setTextColor(color!!)
            text.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 0.9f)
            text.gravity = Gravity.CENTER
            text.visibility = View.VISIBLE

            //指示器
            val indicators = View(c)
            indicators.layoutParams = LinearLayout.LayoutParams(indicatorsWidth.toInt(), indicatorsHeight.toInt())
            indicators.setBackgroundColor(color!!)
            if (!indicatorsVisible) {
//                indicators.visibility = View.INVISIBLE
                indicators.visibility = View.GONE
            }


            childView.addView(text)
            childView.addView(indicators)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                childView.background = resources.getDrawable(R.drawable.ripple_bgd)
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

//        initTouchEvent()
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
            var text = (mChildViews[i].getChildAt(0) as TextView)
            var indicator = (mChildViews[i].getChildAt(1))
            if (i == index) {
                text.setTextColor(colorSelected!!)
                if (indicatorsVisible) {
                    indicator.visibility = View.VISIBLE
                    indicator.setBackgroundColor(colorSelected!!)
                }
                if (mIsAnimoScale) {
                    text.textSize = textSize!!.plus(scaleValue)
                }
            } else {
                text.setTextColor(color!!)
                indicator.visibility = View.GONE
                if (mIsAnimoScale) {
                    text.textSize = textSize!!
                }
            }

            if (mIsAnimoScale && index > 0 && index < mChildViews.size - 1) {
                if (i == index - 1 || i == index + 1) {
                    text.textSize = textSize!!.plus(scaleValue / 2)
                }
            }
        }
    }

    fun setOnClickListener(listener: OnClickListener) {
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

    fun setOnScrollerListener(listener:OnScrollerListener){
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
                Log.d(TAG, "posX="+posX)
                Log.d(TAG, "posY="+posY)

            }
            MotionEvent.ACTION_MOVE -> {
                curPosX = event.x
                curPosY = event.y
                Log.d(TAG, "curPosX-move="+curPosX)
                Log.d(TAG, "curPosY-move="+curPosY)
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
                    Log.d(TAG, "curPosX1="+curPosX)
                    Log.d(TAG, "curPosY1="+curPosY)
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
                posX = 0.0f
                posY = 0.0f
                curPosX = 0.0f
                curPosY = 0.0f
                mIsScrolling = false
                isMove = false
            }
        }
        Log.d(TAG, "scrolling="+mIsScrolling)
        return false
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


    }


    /***--- 属性设置 --******/

    fun indicatorsVisible(show: Boolean) {
        this.indicatorsVisible = show
//        this.invalidate()
        initView(c, null)
    }

    fun setIndicatorWidth(width: Float) {
        this.indicatorsWidth = width
        initView(c, null)
    }


    /**
     *
     * LinearLayout.HORIZONTAL
     * LinearLayout.VERTICAL
     */
    fun setTabOrientation(o: Int) {
        this.mOrientation = o
        initView(c, null)
    }

    fun setColor(color: Int) {
        this.color = color
//        this.invalidate()
        initView(c, null)
    }

    fun setSelectedColor(color: Int) {
        this.colorSelected = color
//        this.invalidate()
        initView(c, null)
    }

    fun setTitles(titles: ArrayList<String>) {
        this.titles = titles
        this.size = titles.size
        initView(c, null)
//        this.invalidate()
    }

    fun canScroller(can: Boolean){
        this.mCanScroller = can
    }

    interface OnClickListener : View.OnClickListener {

        override fun onClick(p0: View?) {
        }

        fun onClick(view: View, position: Int)
    }

    interface OnScrollerListener {
        fun onScroller(view: View, position: Int)
    }
}