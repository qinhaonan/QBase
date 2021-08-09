package com.qhn.mybase.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.qhn.mybase.network.ResultWarp


abstract class BaseRecyclerViewViewModel<T> : BaseViewModel() {
    open val paramsLiveData by lazy { MutableLiveData<Pair<Int, Int>>() }
    open val limit = 100
    open val list by lazy { ArrayList<T>() }
    abstract fun  obsData(vararg params: Any): LiveData<ResultWarp<List<T>?>>
 }