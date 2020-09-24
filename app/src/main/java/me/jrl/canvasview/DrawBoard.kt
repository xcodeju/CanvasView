package me.jrl.canvasview

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class DrawBoard : View {


    //画笔
    private val mPaint by lazy {
        Paint().apply {
            color = mColor
            strokeWidth = mLineWidth
            style = Paint.Style.STROKE
            isAntiAlias = true
            isDither = true
        }
    }

    //控件宽高
    private var mWidth = 0
    private var mHeight = 0

    //颜色
    private var mColor = Color.BLACK

    //线宽
    private var mLineWidth = dp2px(context,5)

    //路径
    private var mPath:Path? = null

    //位图
    private val mBuffBitmap by lazy {
        Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888)
    }

    //画板颜色
    private var mCanvasColor = Color.WHITE

    //画板
    private val mBuffCanvas by lazy {
        Canvas(mBuffBitmap).apply {
            drawColor(mCanvasColor)
        }
    }

    //画板画笔大小
    private var mCanvasSize = dp2px(context,20)

    //记录当前路径
    private var currPathList = mutableListOf<PathList>()

    //记录保存路径
    private var savePathList = mutableListOf<PathList>()

    //记录上次触摸的起点位置
    private var mLastX = 0f
    private var mLastY = 0f

    //设置图形混合模式
    private var portermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)

    //画笔模式
    private var mEraserMode = EraserMode.PaintMode

    constructor(context: Context) : super(context) {

    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    private fun init(){
        mPath = Path()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w
        mHeight = h

    }
    
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawBitmap(mBuffBitmap, 0f, 0f, null)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        val x = event?.x
        val y = event?.y

        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                mLastX = x!!
                mLastY = y!!
                mPath?.moveTo(mLastX,mLastY)
            }
            MotionEvent.ACTION_MOVE -> {
                mPath?.quadTo((mLastX + x!!) / 2,(mLastY + y!!) / 2,x,y)
                mBuffCanvas.drawPath(mPath!!,mPaint)
                invalidate()
                mLastX = x!!
                mLastY = y!!
                mPath?.moveTo(mLastX,mLastY)
            }
            MotionEvent.ACTION_UP -> {
                //保存路径
                savePathList()
                mPath?.reset()
            }
        }
        return true
    }

    /**
     * 保存路径
     * */
    private fun savePathList() {

        //最多保存20条路径
        val max = 20
        if (savePathList.size == max) {
            savePathList.removeAt(0)
        }
        savePathList.clear()
        savePathList.addAll(currPathList)
        val path = Path(mPath)
        val mPathList = PathList(mPaint, path)
        currPathList.add(mPathList)
        currPathList.add(mPathList)
    }


    /**
     * 清空画板
     * */
    public fun clear() {

        savePathList.clear()
        currPathList.clear()
        //将位图变为透明
        mBuffBitmap.eraseColor(Color.TRANSPARENT)
        invalidate()
    }

    /**
     * 上一步
     * 撤销
     * */
    public fun lastStep() {

        if (currPathList.size > 0) {
            currPathList.removeAt(currPathList.size - 1)
            drawBitmap()
        }

    }

    /**
     * 下一步
     * 恢复
     * */
    public fun nextStep() {

        if (savePathList.size > currPathList.size) {
            currPathList.add(savePathList[currPathList.size])
            drawBitmap()
        }
    }

    /**
     * 重绘位图
     * */
    private fun drawBitmap() {

        //先将位图变为透明
        mBuffBitmap.eraseColor(Color.TRANSPARENT)
        //再画保存的路径
        for (path in currPathList) {
            mBuffCanvas.drawPath(path.path!!, path.paint!!)
        }
        invalidate()
    }


    /**
     * dp 转 px
     *
     * @param dpValue dp值
     * */
    private fun dp2px(context: Context,dpValue: Int): Float {
        val scale = context.resources.displayMetrics.density
        return (scale * dpValue + 0.5f)
    }


    /**
     * 设置画笔大小和颜色的方法
     * */
    public fun setPaintSize(strokeWidth:Float){
        this.mLineWidth = strokeWidth
        mPaint.strokeWidth = mLineWidth
    }

    public fun setColor(color:Int){
        this.mColor = color
        mPaint.color = mColor
    }

    /**
     * 设置画笔模式
     * */
    public fun setMode(mode: EraserMode){
        if (mode != mEraserMode){
            if (mode == EraserMode.EraserMode){

                mPaint.apply {
                    color = mCanvasColor
                    strokeWidth = mCanvasSize
                    xfermode = portermode
                }
            }else if (mode == EraserMode.PaintMode) {
                mPaint.apply {
                    color = mColor
                    strokeWidth = mLineWidth
                    xfermode = null
                }
            }
            this.mEraserMode = mode
        }
    }

    public fun getMode():EraserMode{
        return mEraserMode
    }

}