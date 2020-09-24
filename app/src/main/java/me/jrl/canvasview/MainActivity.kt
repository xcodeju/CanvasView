package me.jrl.canvasview

import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    /**
     * 初始化 设置点击事件
     * */
    private fun init() {

        iv_black.setOnClickListener(this)
        iv_accent.setOnClickListener(this)
        iv_primary.setOnClickListener(this)
        iv_red.setOnClickListener(this)
        //设置默认画笔背景为蓝色
        mPaint.background.level = 1
        mPaint.drawable.level = 1
        mPaint.setOnClickListener(this)
        mEraser.setOnClickListener(this)
        mClean.setOnClickListener(this)
        mLast.setOnClickListener(this)
        mNext.setOnClickListener(this)
        //添加监听事件
        addOnSlierChangeListener()
    }

    /**
     * 监听回调
     * */
    private fun addOnSlierChangeListener() {
        slider.addListener(object : Slider.OnSliderChangedListener {
            override fun sliderCallBack(p: Float) {
                drawBoard.setPaintSize(p * dp2px(applicationContext,20))
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onClick(v: View?) {
        when (v) {
            iv_black -> {
                drawBoard.setColor(Color.BLACK)
            }
            iv_accent -> {
                drawBoard.setColor(getColor(R.color.colorAccent))
            }
            iv_primary -> {
                drawBoard.setColor(getColor(R.color.colorPrimary))
            }
            iv_red -> {
                drawBoard.setColor(Color.RED)
            }

            mPaint -> {
                setBgColor()
                if (drawBoard.getMode() != EraserMode.PaintMode) {
                    drawBoard.setMode(EraserMode.PaintMode)
                }
                mPaint.background.level = 1
                mPaint.drawable.level = 1
            }
            mEraser -> {
                setBgColor()
                if (drawBoard.getMode() != EraserMode.EraserMode) {
                    drawBoard.setMode(EraserMode.EraserMode)
                }
                mEraser.background.level = 1
                mEraser.drawable.level = 1
            }
            mClean -> {
                setBgColor()
                if (drawBoard.getMode() != EraserMode.ClearMode) {
                    drawBoard.setMode(EraserMode.ClearMode)
                }
                alertDialogClean()
                mClean.background.level = 1
            }
            mLast -> {
                setBgColor()
                if (drawBoard.getMode() != EraserMode.LastMode) {
                    drawBoard.setMode(EraserMode.LastMode)
                }
                mLast.background.level = 1
                drawBoard.lastStep()
            }
            mNext -> {
                setBgColor()
                if (drawBoard.getMode() != EraserMode.NextMode) {
                    drawBoard.setMode(EraserMode.NextMode)
                }
                mNext.background.level = 1
                drawBoard.nextStep()
            }
        }
    }


    /**
     * 重新设置按钮背景色
     * */
    private fun setBgColor() {

        when (drawBoard.getMode()) {

            EraserMode.PaintMode -> {
                mPaint.drawable.level = 0
                mPaint.background.level = 0
            }
            EraserMode.EraserMode -> {
                mEraser.drawable.level = 0
                mEraser.background.level = 0
            }
            EraserMode.ClearMode -> {
                mClean.background.level = 0
            }
            EraserMode.LastMode -> {
                mLast.background.level = 0
            }
            EraserMode.NextMode -> {
                mNext.background.level = 0
            }
        }
    }

    /**
     * dp 转 px
     * @param dpValue dp值
     * */
    private fun dp2px(context: Context, dpValue: Int): Float {
        val scale = context.resources.displayMetrics.density
        return (scale * dpValue + 0.5f)
    }


    /**
     * 设置画板清空对话框
     */
    private fun alertDialogClean() {

        val builder = AlertDialog.Builder(this).apply {
            setMessage("确定要清空画板？")
            setPositiveButton(
                "确定"
            ) { _, _ ->
                drawBoard.clear()
            }
            setNegativeButton("取消") { _, _ ->
                return@setNegativeButton
            }
        }
        val dialog = builder.show()
        dialog.show()
    }

}