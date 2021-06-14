package com.example.paging3sampleapp.util

import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target


fun ImageView.loadImage(
    url: String,
    placeholderResId: Int? = null,
    errorPlaceholderResId: Int? = null,
    isError: ((Boolean) -> Unit)? = null
) {
    Glide.with(this).load(url).placeholder(placeholderResId ?: -1).error(placeholderResId)
        .listener(object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                isError?.invoke(true)
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                isError?.invoke(false)
                return false
            }

        }).into(this)
}