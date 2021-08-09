package com.qhn.mybase.utils

//button防抖
var lastClickTime = 0L
const val MIN_CLICK_DELAY_TIME = 1000L
fun isValidClick(): Boolean {
    return if (System.currentTimeMillis() - lastClickTime > MIN_CLICK_DELAY_TIME) {
        lastClickTime = System.currentTimeMillis()
        true
    } else false
}
