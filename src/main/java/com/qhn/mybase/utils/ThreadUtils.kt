package com.qhn.mybase.utils

import android.annotation.SuppressLint
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

@SuppressLint("CheckResult")
// 子线程完成计算,UI线程触发事件
fun <T> calculateOnSubToMain(subThread: () -> T, mainThread: (T) -> Unit) {
    Observable.create<T> {
        it.onNext(subThread())
        it.onComplete()
    }
            .mainComputationScheduler()
            .subscribe(
                    {
                        mainThread(it)
                    },
                    {
                        e(it.message)
                    })
}
//订阅在IO线程,观察在UI线程

fun <T> Observable<T>.mainIoScheduler(): Observable<T> =
        subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

//订阅在computation线程,观察在UI线程

fun <T> Observable<T>.mainComputationScheduler(): Observable<T> =
        subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread())

//全部在IO线程
fun <T> Observable<T>.allIoScheduler(): Observable<T> =
        subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())


//工作在子线程完成计算 产生一个值 传给MainThread消费
fun <T> ioResultToMainThread(ioThread: () -> T, mainThread: ((p: T) -> Unit)? = null): Disposable =
        Observable.create<T>(
                {
                    it.onNext(ioThread())
                    it.onComplete()
                })
                .mainCalcSchedulers()
                .subscribe({ mainThread?.invoke(it) }, { e(it.message) })

//线程切换
fun <T> Observable<T>.mainCalcSchedulers(): Observable<T> =
        subscribeOn((RxScheduler.computation)).observeOn(RxScheduler.mainThread)

object RxScheduler {
    val io1 = io.reactivex.internal.schedulers.IoScheduler()
    val computation = io.reactivex.internal.schedulers.ComputationScheduler()
    val mainThread: Scheduler = AndroidSchedulers.mainThread()
}