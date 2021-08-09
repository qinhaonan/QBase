package com.qhn.mybase.bindingAdapter

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.qhn.mybase.R
import com.qhn.mybase.helper.glide.GlideCircleTransform
import com.g.base.help.glide.GlideRoundTransform

@BindingAdapter("android:back")
fun setBackground(view: View, color: Int) {
    view.setBackgroundColor(color)
}

//设置背景图片 圆角 OR 圆形图片 OR 默认图片
@BindingAdapter("app:image", "app:imageRadius", "app:imageCircle", "app:placeholder", "app:error", requireAll = false)
fun setBackgroundImage(view: ImageView, oldSrc: Any? = null, oldRadius: Number? = null, oldCircle: Boolean? = null, oldPlaceholder: Int? = null, oldError: Int? = null,
                       src: Any? = null, radius: Number? = null, circle: Boolean? = null, placeholder: Int? = null, error: Int? = null) {
    if (oldSrc == src) return
    Glide.with(view.context).load(src)
            .asBitmap()
            .placeholder(placeholder ?: R.drawable.load_placeholder)
            .error(error ?: R.drawable.load_error)
            .apply {
                if (circle != null && circle) {
                    transform(GlideCircleTransform(view.context))
                } else if (radius != null && radius.toInt() > 0) {
                    transform(GlideRoundTransform(view.context, radius.toInt()))
                } else {
                    dontTransform()
                }
                crossFade(300)
                into(view)
            }
}