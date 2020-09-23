package me.jrl.canvasview

import android.graphics.Paint
import android.graphics.Path

class PathList {

    var paint:Paint? = null
    var path:Path? = null

    constructor(paint: Paint?, path: Path?) {
        this.paint = paint
        this.path = path
    }

}