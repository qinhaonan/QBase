package com.qhn.mybase.utils.extend

import android.content.Context
import android.os.Handler
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.orhanobut.logger.Logger
import com.qhn.mybase.ui.activity.BaseActivity
import com.qhn.mybase.ui.fragment.BaseFragment
import java.math.BigDecimal

/**
 *  Create by QinHaonan on 2018/10/12
 */
fun Context.setTimeOut(timeOut: Long = 1000L, exec: () -> Unit): () -> Unit {
    val handler = Handler(mainLooper)
    val runnable = Runnable {
        exec.invoke()
    }
    handler.postDelayed(runnable, Math.max(timeOut, 0L))
    return {
        handler.removeCallbacks(runnable)
    }
}

fun Context.toast(str: String?) {
    if (!str.isNullOrEmpty())
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show()
}


fun BaseActivity<*>.ad(print: Any?) {
    Logger.d("TAG--- $TAG:", print)
}

fun BaseActivity<*>.ae(print: Any?) {
    Logger.e("TAG--- $TAG:", print)
}

fun BaseActivity<*>.aj(print: String) {
    Logger.json("TAG--- $TAG:   $print")
}

/**\
 *  fragment 相关
 */
fun BaseFragment<*>.fd(print: Any?) {
    Logger.d("TAG--- $TAG:\",print")
}

fun BaseFragment<*>.fe(print: Any?) {
    Logger.e("TAG--- $TAG:", print)
}

fun BaseFragment<*>.fj(print: String) {
    Logger.json("TAG--- $TAG:   $print")
}

fun Fragment.toast(str: String) {
    Toast.makeText(activity, str, Toast.LENGTH_SHORT).show()
}

//保留两位小数,四舍五入
fun Context.format(double: Double) = BigDecimal(double).setScale(8, BigDecimal.ROUND_HALF_UP).toFloat()

