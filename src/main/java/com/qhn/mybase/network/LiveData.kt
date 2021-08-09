package com.qhn.mybase.network

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations


/**
 *  Create by QinHaonan on 2018/10/18
 */
//LiveData扩展函数,观察activity和fragment
fun <T> LiveData<ResultWarp<T>>.observeEx(owner: LifecycleOwner, observer: (ResultWarp<T>) -> Unit) {
    this.observe(owner, Observer {
        observer.invoke(it!!)
    })
}

//返回的数据不能为空 List长度不能为0
fun <T> LiveData<ResultWarp<T?>>.resultNotNull(): LiveData<ResultWarp<T?>> = Transformations.map(this) {
    if (it.status == Status.Content && (it.data == null || (it.data as? List<*>)?.isEmpty() == true)) {
        ResultWarp.error(ErrorException(ErrorCode.EMPTY, "暂时找不到相关的数据~", null))
    } else {
        it
    }
}