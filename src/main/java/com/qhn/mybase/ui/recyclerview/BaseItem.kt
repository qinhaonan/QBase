package com.qhn.mybase.ui.recyclerview

import android.view.View
import androidx.databinding.ViewDataBinding
import com.qhn.mybase.BR


abstract class BaseItem<T : ViewDataBinding> {

    abstract val resourceId: Int
    var onClickListener: ((view: View) -> Unit)? = null
    open val variableId: Int = BR.data
    open fun onBind(dataBinding: T) {}
    open fun onClick(view: View) {
        onClickListener?.invoke(view)
    }

    //点击事件处理携带额外参数
//    open fun setOnClickListener(call: ((view: View) -> Unit)?) {
//        onClickListener = call
//    }
}