package com.qhn.mybase.ui.recyclerview

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView


class MViewHolder< T : ViewDataBinding>(private val dataBinding: T) : RecyclerView.ViewHolder(dataBinding.root) {
    fun bind(item: BaseItem<T>) {
        item.onBind(dataBinding)
        dataBinding.setVariable(item.variableId, item)
        dataBinding.executePendingBindings()
    }
}