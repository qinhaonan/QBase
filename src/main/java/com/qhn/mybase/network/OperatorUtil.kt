package com.qhn.mybase.network

import android.annotation.SuppressLint
import io.reactivex.Observable
import io.reactivex.ObservableSource

@SuppressLint("CheckResult")
        /**
 *  Create by QinHaonan on 2018/10/18
 */

fun merge() {
    Observable.mergeArray(ObservableSource<Any> {  },ObservableSource<Any> {  }).subscribe()
}