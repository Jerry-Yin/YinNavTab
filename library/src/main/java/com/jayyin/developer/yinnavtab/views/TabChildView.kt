package com.jayyin.developer.yinnavtab.views

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.*
import com.jayyin.developer.yinnavtab.R

/**
 * @description
 * @author JerryYin
 * @create 2018-12-18 10:11
 **/
class TabChildView : LinearLayout {

    companion object {
        val TAG = "TabChildView.KT"
        val MODE_NORMAL_TEXT = 0        //文字 + 下标
        val MODE_DRAWABLE = 1      //图片 + 文字
    }

    private var mContext: Context? = null

    private var mImageView: ImageView? = null
    private var mTextView: TextView? = null
    private var mIndicator: RelativeLayout? = null
    private var mPaddingView: View? = null

    //默认竖直方向
    private var mOrientation = LinearLayout.VERTICAL
    private var mCurMode = MODE_NORMAL_TEXT

    private var mDrawable: Drawable? = null
    private var imageWidth: Float? = null
    private var imageHeight: Float? = null
    private var imgPaddingLeft: Float? = null
    private var imgPaddingRight: Float? = null


    private var text: String? = null
    private var textSize: Float? = null

    //默认标签line高度2dp
    private var indicatorsHeight: Float? = null
    private var indicatorsWidth: Float? = null
    private var indicatorsVisible = true           //无论哪种模式，下标都可以被手动隐藏
    //    private var indicatorPaddingLeft = 5
    private var indicatorPaddingLeft: Float? = null
    private var indicatorPaddingRight: Float? = null

    private var color = Color.parseColor("#7e7d7d")
    private var indicatorColor = Color.parseColor("#00b0ff")

    private var mOnClickListener: View.OnClickListener? = null


    constructor(c: Context, mode: Int?) : super(c) {
        mContext = c
        if (mode != null)
            this.mCurMode = mode
        initView(c, null)
    }

    constructor(c: Context, attrs: AttributeSet) : super(c, attrs) {
        mContext = c
//        if (mode != null)
//            this.mCurMode = mode
        initView(c, attrs)
    }


    @SuppressLint("Recycle")
    private fun initView(c: Context, attrs: AttributeSet?) {
        Log.d(TAG, "initView...  MODE= " + mCurMode)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            this.background = ContextCompat.getDrawable(c, R.drawable.ripple_bgd)
        }
        this.isClickable = true

        //generate object
        if (mTextView == null)
            mTextView = TextView(c)
        if (mImageView == null)
            mImageView = ImageView(c)
        if (mIndicator == null)
            mIndicator = RelativeLayout(c)


        //set default size of all mode
        when (mCurMode) {
            MODE_NORMAL_TEXT -> {
                textSize = 10.0f
                if (mOrientation == VERTICAL) {
                    indicatorsWidth = 80.0f
                    indicatorsHeight = 8.0f
                } else {
                    indicatorsWidth = 12.0f
                    indicatorsHeight = 12.0f
                }
            }

            MODE_DRAWABLE -> {
                if (mDrawable == null) {
                    mDrawable = ContextCompat.getDrawable(c, R.drawable.ic_home_black_24dp)
                }
                imageWidth = 78.0f
                imageHeight = 78.0f
                textSize = 5.0f
                indicatorsWidth = 0f
                indicatorsHeight = 0f
            }
        }

        //get attrs
        val typeArray = c.obtainStyledAttributes(attrs, R.styleable.TabChildView)
        if (typeArray != null) {
            imageHeight = typeArray.getDimension(R.styleable.TabChildView_imageHeight, 60.0f)
            imageWidth = typeArray.getDimension(R.styleable.TabChildView_imageWidth, 60.0f)
            imgPaddingLeft = typeArray.getDimension(R.styleable.TabChildView_imgPaddingLeft, 10.0f)
            imgPaddingRight = typeArray.getDimension(R.styleable.TabChildView_imgPaddingRight, 10.0f)
            text = typeArray.getString(R.styleable.TabChildView_text)
            textSize = typeArray.getDimension(R.styleable.TabChildView_textSize, 10.0f)
            color = typeArray.getColor(R.styleable.TabChildView_color, color)
//            colorSelected = typeArray.getColor(R.styleable.TabChildView_colorSelected, colorSelected!!)
            indicatorsVisible = typeArray.getBoolean(R.styleable.TabChildView_indicatorsVisible, indicatorsVisible)
            indicatorsHeight = typeArray.getDimension(R.styleable.TabChildView_indicatorHeight, indicatorsHeight!!)
            indicatorsWidth = typeArray.getDimension(R.styleable.TabChildView_indicatorWidth, indicatorsWidth!!)
            indicatorColor = typeArray.getColor(R.styleable.TabChildView_indicatorColor, indicatorColor)
            indicatorPaddingLeft = typeArray.getDimension(R.styleable.TabChildView_indicatorPaddingLeft, 10.0f)
            indicatorPaddingRight = typeArray.getDimension(R.styleable.TabChildView_indicatorPaddingRight, 10.0f)
            mOrientation = typeArray.getInteger(R.styleable.TabChildView_orientationView, mOrientation)
            mCurMode = typeArray.getInteger(R.styleable.TabChildView_mode, mCurMode)
            mDrawable = typeArray.getDrawable(R.styleable.TabChildView_src)
        }

        if (mDrawable == null) {
            mDrawable = ContextCompat.getDrawable(c, R.drawable.ic_home_black_24dp)
        }
        if (text == null || text!!.isEmpty())
            text = "Home"

        resetView(c)
    }

    private fun resetView(c: Context) {
        if (this.childCount > 0)
            removeAllViews()
        this.orientation = mOrientation     //default vertical
        mImageView!!.setImageDrawable(mDrawable)
        mTextView!!.text = text
        mTextView!!.textSize = textSize!!
        mTextView!!.setTextColor(color)
        mIndicator!!.setBackgroundColor(indicatorColor)

        when (mCurMode) {
            MODE_NORMAL_TEXT -> {
                //text + ind
                mImageView!!.visibility = View.GONE

                if (mOrientation == VERTICAL) {
                    this.gravity = Gravity.CENTER_HORIZONTAL

                    mTextView!!.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 0.9f)
                    mTextView!!.gravity = Gravity.CENTER

                    mIndicator!!.layoutParams = LinearLayout.LayoutParams(indicatorsWidth!!.toInt(), indicatorsHeight!!.toInt())
                    if (indicatorsVisible)
                        mIndicator!!.visibility = View.VISIBLE
                    else
                        mIndicator!!.visibility = View.GONE

                    this.addView(mTextView)
                    this.addView(mIndicator)
                } else {
                    //HORIZONTIAL
                    this.gravity = Gravity.CENTER
                    var s = Math.max(indicatorsWidth!!, indicatorsHeight!!).toInt()

                    mTextView!!.layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f)
                    mTextView!!.gravity = Gravity.START

                    if (s > 60)
                        s = 60
                    mIndicator!!.layoutParams = RelativeLayout.LayoutParams(s, s)
                    var lp = RelativeLayout.LayoutParams(mIndicator!!.layoutParams)
                    lp.setMargins(indicatorPaddingLeft!!.toInt(), 0, indicatorPaddingRight!!.toInt(), 0)
                    mIndicator!!.layoutParams = lp
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        var drawable = ContextCompat.getDrawable(c, R.drawable.circle_indicator)
//                        mIndicator!!.background = drawable
                        mIndicator!!.background = tintDrawable(drawable!!, indicatorColor)
                    }

                    if (indicatorsVisible)
                        mIndicator!!.visibility = View.VISIBLE
                    else {
                        if (mCurMode == MODE_NORMAL_TEXT && mOrientation == HORIZONTAL)
                            mIndicator!!.visibility = View.INVISIBLE
                        else
                            mIndicator!!.visibility = View.GONE
                    }
                    this.addView(mIndicator)
                    this.addView(mTextView)
                }
            }

            MODE_DRAWABLE -> {
                //text + drawable
                mImageView!!.visibility = View.VISIBLE
                mIndicator!!.visibility = View.GONE

                var imageSize = Math.max(imageHeight!!, imageWidth!!)
                if (mOrientation == VERTICAL) {
                    this.gravity = Gravity.CENTER
//                    mImageView!!.layoutParams = LinearLayout.LayoutParams(imageSize.toInt(), 0, 0.75f)
//                    mTextView!!.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 0, 0.25f)
                    mImageView!!.layoutParams = LinearLayout.LayoutParams(imageWidth!!.toInt(), imageHeight!!.toInt())
                    mTextView!!.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                    mTextView!!.gravity = Gravity.CENTER
                } else {
                    this.gravity = Gravity.CENTER
//                    mImageView!!.layoutParams = LinearLayout.LayoutParams(imageWidth!!.toInt(), imageHeight!!.toInt())
//                    mImageView!!.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT)
                    mImageView!!.layoutParams = LinearLayout.LayoutParams(imageWidth!!.toInt(), imageHeight!!.toInt())
                    var lp = RelativeLayout.LayoutParams(mImageView!!.layoutParams)
                    lp.setMargins(imgPaddingLeft!!.toInt(), 0, imgPaddingRight!!.toInt(), 0)
                    mImageView!!.layoutParams = lp
                    mTextView!!.layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f)
                    mTextView!!.gravity = Gravity.START
                }

                this.addView(mImageView)
                this.addView(mTextView)
//                this.gravity = Gravity.CENTER
            }
        }
    }

    fun setText(text: String) {
        this.text = text
        resetView(mContext!!)
    }

    fun setText(text: String, size: Float?, color: Int?) {
        this.text = text
        if (size != null)
            this.textSize = size
        if (color != null)
            this.color = color
        resetView(mContext!!)
        Log.d(TAG, "setText '" + this.text + "'='" + text + "'")
    }

    fun setTextSize(size: Float?) {
        if (size != null) {
            this.textSize = size
            resetView(mContext!!)
        }
    }

    fun setTextColor(color: Int?) {
        if (color != null) {
            this.color = color
            resetView(mContext!!)
        }
    }

    fun setIndicatorColor(color: Int?) {
        if (color != null) {
            this.indicatorColor = color
            resetView(mContext!!)
        }
    }

    fun setIndicatorSize(width: Float, height: Float) {
        this.indicatorsWidth = width
        this.indicatorsHeight = height
        resetView(mContext!!)
    }

    fun setIndicatorPadding(paddingLeft: Float, paddingRight: Float) {
        this.indicatorPaddingLeft = paddingLeft
        this.indicatorPaddingRight = paddingRight
        resetView(mContext!!)
    }

    fun setIndicatorVisibility(visibility: Boolean) {
        this.indicatorsVisible = visibility
        resetView(mContext!!)
    }

    fun setDrawable(drawableId: Int) {
        var d = ContextCompat.getDrawable(mContext!!, drawableId)
        if (d != null) {
            this.mDrawable = d
            resetView(mContext!!)
        }
    }

    fun setImageSize(width: Float, height: Float, paddingLeft: Float?, paddingRight: Float?) {
        this.imageWidth = width
        this.imageHeight = height
        if (paddingLeft != null)
            this.imgPaddingLeft = paddingLeft
        if (paddingRight != null)
            this.imgPaddingRight = paddingRight
//        this.removeView(mImageView)
        mImageView = null
        mImageView = ImageView(mContext)
        resetView(mContext!!)
    }

    fun setMode(mode: Int) {
        if (mode > 0 && mode < 4)
            this.mCurMode = mode
        initView(mContext!!, null)
    }

    fun setViewOrientation(o: Int) {
        this.mOrientation = o
        initView(mContext!!, null)
    }

    fun tintDrawable(drawable: Drawable, color: Int): Drawable {
        val wrappedDrawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTintList(wrappedDrawable, ColorStateList.valueOf(color));
        return wrappedDrawable;
    }
}