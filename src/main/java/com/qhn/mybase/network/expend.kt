package com.qhn.mybase.network

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers


fun <T> Observable<T>.allOnIoThread() =
        subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())


