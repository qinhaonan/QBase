package com.qhn.mybase.utils

import android.content.Context
import android.os.Build
import android.os.LocaleList
import java.util.*

/**
 *  Create by QinHaonan on 2021/3/13
 */
/**
 * 获取系统的locale
 *
 * @return Locale对象
 */

fun getSystemLocale(context: Context):Locale {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        LocaleList.getDefault().get(0);
    } else {
        Locale.getDefault();
    }
}