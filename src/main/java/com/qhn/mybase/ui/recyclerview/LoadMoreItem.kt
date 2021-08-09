package com.qhn.mybase.ui.recyclerview

import android.view.View
import androidx.databinding.ObservableField
import com.qhn.mybase.R
import com.qhn.mybase.databinding.ItemFootBindingBinding

class LoadMoreItem : BaseItem<ItemFootBindingBinding>() {
    override val resourceId: Int = R.layout.item_foot_binding

    val contentObs by lazy { ObservableField("加载中...") }
    var onLoadingCallBack: (() -> Unit)? = null //加载更多回调
    var loadMoreStatus = LoadingStatus.LOADING_SUCCEED
        private set

    fun setLoadingStatus(status: LoadingStatus) {
        loadMoreStatus = status
        when (status) {
            LoadingStatus.LOADING_NO_MORE -> {
                contentObs.set("没有数据了")
            }
            LoadingStatus.LOADING_ING -> {
                contentObs.set("加载中...")
            }
            LoadingStatus.LOADING_SUCCEED -> {
                contentObs.set("加载成功")
            }
            LoadingStatus.LOADING_FAILED -> {
                contentObs.set("加载失败,点击重试")
            }
        }
    }

    override fun onClick(view: View) {
        if (loadMoreStatus == LoadingStatus.LOADING_FAILED) {
            contentObs.set("加载中...")
            onLoadingCallBack?.invoke()
        }
    }


}

enum class LoadingStatus {
    LOADING_NO_MORE,
    LOADING_ING,
    LOADING_SUCCEED,
    LOADING_FAILED
}

