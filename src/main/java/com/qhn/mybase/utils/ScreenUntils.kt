package com.qhn.mybase.utils

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.util.DisplayMetrics
import android.view.WindowManager
import com.qhn.mybase.utils.extend.dp

//    获得屏幕宽度
fun getScreenWidth(context: Context): Int {
    val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val outMetrics = DisplayMetrics()
    wm.defaultDisplay.getMetrics(outMetrics)
    return outMetrics.widthPixels
}

//    获得屏幕高度
fun getScreenHeight(context: Context): Int {
    val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val outMetrics = DisplayMetrics()
    wm.defaultDisplay.getMetrics(outMetrics)
    return outMetrics.heightPixels
}

//    状态栏高度
fun getStateHeight(context: Activity): Int {
    val frame = Rect()
    context.window.decorView.getWindowVisibleDisplayFrame(frame)
    var height = 0
    val resourceId = context.applicationContext.resources.getIdentifier("status_bar_height", "dimen", "android");
    if (resourceId > 0) {
        height = context.applicationContext.resources.getDimensionPixelSize(resourceId)
    }
    return height
}

//获取屏幕宽度默认减去spacing=16+16+6+6dp的三分之一
fun getPicHeightOrWidth(context: Context, divisor: Int = 3, spacing: Int = 44): Int {
    val wm = context
            .getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val outMetrics = DisplayMetrics()
    wm.defaultDisplay.getMetrics(outMetrics)
    return ((outMetrics.widthPixels - spacing.dp()) / divisor)
}