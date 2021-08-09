package com.qhn.mybase.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
fun stringToTimeStamp(timeStr: String, formatStr: String): Long {
    val format = SimpleDateFormat(formatStr)
    val data = format.parse(timeStr)
    return data.time
}

@SuppressLint("SimpleDateFormat")
fun timeStampToString(timeLong: Long, formatStr: String): String {
    return SimpleDateFormat(formatStr).format(Date(timeLong))
}