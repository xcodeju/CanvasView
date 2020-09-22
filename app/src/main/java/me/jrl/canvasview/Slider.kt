package me.jrl.canvasview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class Slider : View {

    //画笔(绘制滑动条)
    private val mPaint by lazy {
        Paint().apply {
            color = mColor
            strokeWidth = mLineWidth
            style = Paint.Style.STROKE
            isAntiAlias = true
        }
    }

    //颜色
    private var mColor:Int = 0
    //线宽
    private var mLineWidth = 0f
    //小圆半径
    private var mRadius = 0f
    //记录触摸滑动值
    private var position = 0f

    //绘制不可变的线条背景
    private val mSliderPaint: Paint by lazy {
        Paint().apply {
            color = Color.GRAY
            strokeWidth = mLineWidth
            style = Paint.Style.STROKE
            isAntiAlias = true
        }
    }
    //绘制小圆
    private val mCirclePaint: Paint by lazy {
        Paint().apply {
            color = mColor
            style = Paint.Style.FILL
            isAntiAlias = true
        }
    }

    constructor(context: Context):super(context){}

    constructor(context: Context,attrs:AttributeSet?):super(context,attrs){
        init(context, attrs)
    }


    /**
     * 初始化
     * */
    private fun init(context: Context, attrs: AttributeSet?) {
        initAttr(context, attrs)
    }

    private fun initAttr(context: Context, attrs: AttributeSet?) {
        val array = context.obtainStyledAttributes(attrs,R.styleable.Slider)
        mColor = array.getColor(R.styleable.Slider_mColor, Color.MAGENTA)
        mRadius = array.getFloat(R.styleable.Slider_mRadius,dp2px(context,6))
        array.recycle()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawLine(measuredWidth / 2f,0f,measuredWidth / 2f,measuredHeight * 1.0f,mSliderPaint)
        canvas?.drawCircle(measuredWidth / 2f,position + mRadius,mRadius,mCirclePaint)
        canvas?.drawLine(measuredWidth / 2f,0f,measuredWidth / 2f,position,mPaint)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                position = when {
                    position < -1 -> {
                        0f
                    }
                    position > measuredHeight - mRadius + 1 -> {
                        measuredHeight - mRadius
                    }
                    else -> {
                        event.y
                    }
                }
                callSliderValueBack()
                invalidate()//刷新
            }
            MotionEvent.ACTION_MOVE -> {
                position = when {
                    position <  - 1 -> {
                        0f
                    }
                    position >= measuredHeight - mRadius + 1 -> {
                        measuredHeight - mRadius

                    }
                    else -> {
                        event.y
                    }
                }
                callSliderValueBack()
                invalidate()//刷新
            }
            MotionEvent.ACTION_UP -> {
                //松手

            }
        }
        return true
    }



    /**
     * dp 转 px
     * @param dpValue dp值
     * */
    private fun dp2px(context: Context,dpValue:Int):Float{
        val scale = context.resources.displayMetrics.density
        return (scale * dpValue + 0.5f)
    }

    /**
     * 接口，实现滑动条触摸值回调
     * */
    public interface OnSliderChangedListener{
        fun sliderCallBack(p:Float)
    }

    /**
     * 监听器
     * */
    private var listener:OnSliderChangedListener? = null

    /**
     * 注册监听器
     * */
    public fun addListener(listener: OnSliderChangedListener){
        this.listener = listener
    }

    private fun callSliderValueBack(){
        listener?.sliderCallBack(position)
    }

}