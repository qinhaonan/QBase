package com.qhn.mybase.ui.recyclerview


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

class BaseAdapter : RecyclerView.Adapter<MViewHolder<ViewDataBinding>>() {

    var newItems: ArrayList<BaseItem<*>> = ArrayList()
    var oldItems: ArrayList<BaseItem<*>> = ArrayList()
    var hasFootItem: Boolean = true
//    private var recyclerViewStatus = Status.ShowContent

    private val loadMoreItem by lazy { LoadMoreItem() }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MViewHolder<ViewDataBinding> {
        return MViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), viewType, parent, false)!!)
    }

    override fun getItemCount(): Int {
        return if (hasFootItem && newItems.size != 0) newItems.size + 1 else newItems.size
    }

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(holder: MViewHolder<ViewDataBinding>, position: Int) {
        if (hasFootItem && position == newItems.size) {
            holder.bind(loadMoreItem as BaseItem<ViewDataBinding>)
            if (loadMoreItem.loadMoreStatus == LoadingStatus.LOADING_SUCCEED) {
                loadMoreItem.onLoadingCallBack?.invoke()
            }
        } else {
            if (position != newItems.size)
                holder.bind(newItems[position] as BaseItem<ViewDataBinding>)
        }

    }

    override fun getItemViewType(position: Int): Int {
        if (position == newItems.size) {
            return loadMoreItem.resourceId
        }
        return newItems[position].resourceId
    }


    /**
     *
     */

    fun calculateDiff(areItemsTheSame: ((oldItem: BaseItem<*>, newItem: BaseItem<*>) -> Boolean), areContentsTheSame: (oldItem: BaseItem<*>, newItem: BaseItem<*>) -> Boolean, funOnFinished: (() -> Unit)? = null) {
        val calculateDiff = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return if (hasFootItem && (newItemPosition == newItems.size || oldItemPosition == oldItems.size))
                    false
                else
                    areItemsTheSame(newItems[newItemPosition], oldItems[oldItemPosition])
            }

            override fun getOldListSize(): Int {
                return if (hasFootItem && oldItems.size != 0) oldItems.size + 1 else oldItems.size
            }

            override fun getNewListSize(): Int {
                return if (hasFootItem && newItems.size != 0) newItems.size + 1 else newItems.size
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return if (hasFootItem && (newItemPosition == newItems.size || oldItemPosition == oldItems.size))
                    false
                else
                    areContentsTheSame(newItems[newItemPosition], oldItems[oldItemPosition])
            }

        })

        oldItems=newItems
        calculateDiff.dispatchUpdatesTo(this)

        funOnFinished?.invoke()


    }


    fun setLoadingSuccess() {
        loadMoreItem.setLoadingStatus(LoadingStatus.LOADING_SUCCEED)
    }

    fun setLoadingFailed() {
        loadMoreItem.setLoadingStatus(LoadingStatus.LOADING_FAILED)
    }

    fun setLoadingNoMore() {
        loadMoreItem.setLoadingStatus(LoadingStatus.LOADING_NO_MORE)
    }

    /**
     *  设置回调函数
     */
    fun setLoadMoreListener(listener: () -> Unit) {
        loadMoreItem.onLoadingCallBack = listener
    }

    /**
     *  展示正在加载
     */
    fun showLoading() {
        if (newItems.isNotEmpty()) {
            newItems.clear()
        }
        newItems.add(LoadingItem())
        notifyDataSetChanged()
//        recyclerViewStatus = Status.ShowLoading
    }

    fun showContent(tempItems: ArrayList<BaseItem<*>>, isUseDiff: Boolean = false) {
//        if (recyclerViewStatus != Status.ShowContent) {
//            newItems.clear()
//            recyclerViewStatus = Status.ShowContent
//        }
        newItems.addAll(tempItems)
//        calculateDiff()
//        if (isNoDate)
//            notifyDataSetChanged()
//        else
//            calculateDiff()
    }
}

enum class Status {
    ShowLoading,
    ShowError,
    ShowContent,
}