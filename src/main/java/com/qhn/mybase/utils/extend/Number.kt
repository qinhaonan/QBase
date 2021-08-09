package com.qhn.mybase.utils.extend

import com.qhn.mybase.BaseApplication
import java.math.BigDecimal
import kotlin.Float as Float1

/**
 * Created by G on 2017/8/12 0012.
 */

fun Number.dp(): Int {
    val scale = BaseApplication.application.resources.displayMetrics.density
    return (this.toFloat() * scale + 0.5f).toInt()
}

/**
 * 保留几位小数
 */
fun Number.round(scale: Int): String {
    if (scale < 0) {
        throw IllegalArgumentException("The scale must be a positive integer or zero")
    }
    val b = BigDecimal(this.toDouble())
    val one = BigDecimal("1")
    return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).toString()
}

/**
 *  加减乘除
 */
fun Number.add(addend: Number): String {
    return BigDecimal(this.toString()).add(BigDecimal(addend.toString())).toString()
}

fun Number.subtract(addend: Number): String {
    return BigDecimal(this.toString()).subtract(BigDecimal(addend.toString())).toString()
}

fun Number.multiply(addend: Number): String {
    return BigDecimal(this.toString()).multiply(BigDecimal(addend.toString())).toString()
}
fun Number.multiplyF(addend: Number): Float1 {
    return BigDecimal(this.toString()).multiply(BigDecimal(addend.toString())).toFloat()
}

// 除法 默认保留两位小数,四舍五入
fun Number.divide(addend: Number, scale: Int = 2, roundingMode: Int = BigDecimal.ROUND_HALF_UP): Float1 {
    return BigDecimal(this.toString()).divide(BigDecimal(addend.toString()), scale, roundingMode).toFloat()
}