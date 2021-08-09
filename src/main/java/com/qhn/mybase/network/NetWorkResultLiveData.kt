package com.qhn.mybase.network

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.qhn.mybase.utils.e
import io.reactivex.Observable

/**
 *  获取数据类
 *
 */
class NetWorkResultLiveData<ResultType>(
        var remoteResult: (() -> Observable<Result<ResultType>>)? = null,
        var localResult: (() -> LiveData<ResultType>)? = null,
        var isOnlyNet: Boolean = true,
        var saveRemoteResult: ((ResultType) -> Unit)? = null,
        var onRemoteSuccess: ((ResultType) -> Unit)? = null,
        var onRemoteFailed: ((ErrorException) -> Unit)? = null
) : MediatorLiveData<ResultWarp<ResultType?>>() {

    /**
     *  策略: isOnlyNet为true的时候 只从网络获取,否则从数据库获取,如果数据库查不到数据时从网络获取
     */
    init {
        getResult(remoteResult, localResult, isOnlyNet, saveRemoteResult, onRemoteSuccess, onRemoteFailed, true)
    }

    private fun fromRemote() {
        if (isOnlyNet)
            remoteResult?.invoke()?.allOnIoThread()?.subscribe(
                    {
                        onRemoteSuccess?.invoke(it.data)
                        postValue(ResultWarp(Status.Content, it.data))
                        saveRemoteResult?.invoke(it.data)
                    },
                    {
                        val error: ErrorException = (it as? ErrorException)
                                ?: ErrorException(ErrorCode.UNKNOWN, "未知错误", null)
                        postValue(ResultWarp(Status.Content, error = error))
                        onRemoteFailed?.invoke(error)
                        e(it.message)
                    }
            )
    }

    fun getResult(remoteResult: (() -> Observable<Result<ResultType>>)? = null,
                  localResult: (() -> LiveData<ResultType>)? = null,
                  isOnlyNet: Boolean = true,
                  saveRemoteResult: ((ResultType) -> Unit)? = null,
                  onRemoteSuccess: ((ResultType) -> Unit)? = null,
                  onRemoteFailed: ((ErrorException) -> Unit)? = null,
                  isInit: Boolean = false) {
        if (!isInit) {
            this.remoteResult = remoteResult
            this.localResult = localResult
            this.isOnlyNet = isOnlyNet
            this.saveRemoteResult = saveRemoteResult
            this.onRemoteSuccess = onRemoteSuccess
            this.onRemoteFailed = onRemoteFailed
        }
        postValue(ResultWarp.loading())
        if (isOnlyNet || localResult == null) {
            fromRemote()
        } else {
            addSource(localResult.invoke()) {
                if (it != null)
                    postValue(ResultWarp(Status.Content, it))
                else
                    fromRemote()
            }
        }
    }
}



