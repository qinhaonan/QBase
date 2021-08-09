package com.qhn.mybase.utils

import android.app.Activity
import android.graphics.drawable.BitmapDrawable
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.PopupWindow
import android.widget.RelativeLayout
import com.qhn.mybase.R


/**
 *  Gravity.CENTER：在showAsDropDown()中是跟 Gravity.LEFT一样，在showAtLocation()中Gravity.CENTER才有效果
 *  popupWindow 高度超过屏幕会自动换反方向
 */
object PopupWindowUtils {
    fun build(view: View,
              anchorView: View,
              width: Int,
              height: Int,
              xOff: Int,
              yOff: Int,
              gravity: Int,
              activity: Activity? = null,
              onDismissPopupWindow: (() -> Unit)? = null
    ): PopupWindow {
        val popupWindow = PopupWindow(view, width, height)
        popupWindow.setBackgroundDrawable(BitmapDrawable())//如果传null,4.4手机 点击其他地方 popupWindow不消失,
        popupWindow.isFocusable = true
        popupWindow.isOutsideTouchable = true
        popupWindow.showAsDropDown(anchorView, xOff, yOff, gravity)
        if (activity != null) {
            backgroundAlpha(0.6f, activity)
        }
        popupWindow.setOnDismissListener {
            if (activity != null) {
                backgroundAlpha(1f, activity)
                onDismissPopupWindow?.invoke()
            }
        }
        return popupWindow
    }

    private fun backgroundAlpha(bgAlpha: Float, activity: Activity) {
        val lp = activity.window.attributes
        lp.alpha = bgAlpha
        activity.window.attributes = lp
    }

    fun buildBottomWindow(view: View,
                          activity: Activity? = null,
                          onDismissPopupWindow: (() -> Unit)? = null
    ): PopupWindow {
        val popupWindow = PopupWindow(view, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
        popupWindow.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
        popupWindow.setBackgroundDrawable(BitmapDrawable())
        popupWindow.isFocusable = true
        popupWindow.isOutsideTouchable = true
        popupWindow.animationStyle = R.style.PopupWindow
        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0)
        if (activity != null) {
            backgroundAlpha(0.6f, activity)
        }
        popupWindow.setOnDismissListener {
            if (activity != null) {
                backgroundAlpha(1f, activity)
                onDismissPopupWindow?.invoke()
            }
        }
        return popupWindow
    }
}