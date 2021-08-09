package com.qhn.mybase.utils

import android.annotation.SuppressLint
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 *  Create by QinHaonan on 2019/1/14
 */


//每隔 1s(默认) 执行一次任务，第一次任务执行前有 1s(默认) 的间隔，执行1(默认)次
fun setInterval(initialDelay: Long = 1000L, period: Long = 1000L, time: Long = 1L, func: () -> Unit) {
    Observable.interval(initialDelay, period, TimeUnit.MILLISECONDS)
            .take(time)
            .mainIoScheduler()
            .subscribe {
                func.invoke()
            } }

@SuppressLint("CheckResult")
//遇到错误重试
fun <T : Any?> Observable<T>.retryWhenDelay(num: Int, max: Int) {
    var i = num
    this.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers
                    .mainThread())
            .retryWhen {
                it.flatMap {
                    if (i++ < max)
                        Observable.timer(1000, TimeUnit.SECONDS)
                    else
                        Observable.error(Throwable("错了"))
                }
            }.subscribe()
}

//合并三个最新的observable
fun combineObservable() {

}