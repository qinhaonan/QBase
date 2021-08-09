package com.g.base.help

import com.qhn.mybase.BuildConfig
import com.qhn.mybase.utils.e

/**
 * Created by G on 2017/11/7 0007.
 */
fun tryCatch( exec: () -> Unit) {
    try {
        exec.invoke()
    } catch (error: Exception) {
        if (BuildConfig.DEBUG)
            e(error.message)
    }
}