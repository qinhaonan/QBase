package com.qhn.mybase.utils

import android.app.Service
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator


fun Context.getVibrator():Vibrator =
    getSystemService(Service.VIBRATOR_SERVICE) as Vibrator

/**
 *  vibrate(new int[]{100,200,300,400},2)是指：先等待100ms，震动200ms，再等待300ms，震动400ms，
 *  接着就从pattern[2]的位置开始重复，就是继续的等待300ms，震动400ms，一直重复下去。
 *  当然传入0就是从开头一直重复下去，传入-1就是不重复震动。
 */
fun Vibrator.startVibrator(pattern: LongArray, repeat: Int) {
    if (hasVibrator()) {
        if (Build.VERSION.SDK_INT > 26) {
            vibrate(VibrationEffect.createWaveform(pattern, repeat))
        } else {
            vibrate(pattern, repeat)
        }
    }
}
