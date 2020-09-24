### 自定义画板
+ 效果
![image.png](https://upload-images.jianshu.io/upload_images/18961644-f5bb71913b1983c7.png?imageMogr2/auto-orient/strip|imageView2/2/w/1029/format/webp)
### 介绍：
左边是可以滑动小圆点的滑动条，用来改变字体大小，中间是绘画区域，右边是选择颜色的区域，下边有画笔，橡皮擦，撤销，反撤销等操作，这个 demo我用到了今天老师教的知识点和我上篇文章介绍的画板的绘制，除了保存我还没实现，其它都实现了，如果还有小问题，请指出来，我会帮您解答的
### 主要代码
+ 滑动条触摸比值回调
```
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

    /**
     * 回调滑动比值
     * */
    private fun callSliderValueBack(){
        listener?.sliderCallBack(position / measuredHeight)
    }
```
+ 画板绘制功能
```
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
```
