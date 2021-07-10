package com.example.paging3sampleapp.util

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable

fun ImageView.loadImage(
    url: String,
    placeholderResId: Int? = null,
    errorPlaceholderResId: Int? = null,
    shimmerPlaceholder: Drawable? = null,
    isError: ((Boolean) -> Unit)? = null
) {
    Glide.with(this).load(url).apply {
        if (placeholderResId == null) (if (shimmerPlaceholder != null) placeholder(
            shimmerPlaceholder
        )) else placeholder(
            placeholderResId
        )
        error(errorPlaceholderResId ?: -1)
    }.into(this)
}


fun ImageView.getShimmerPlaceholder(
    baseColor: Int? = null,
    highlightColor: Int? = null
): Drawable {
    val colorHighlightBuilder =
        Shimmer.ColorHighlightBuilder()// The attributes for a ShimmerDrawable is set by this builder
            .setDuration(1800) // how long the shimmering animation takes to do one full sweep
            .setBaseAlpha(0.8f) //the alpha of the underlying children
            .setHighlightAlpha(0.7f) // the shimmer alpha amount
            .setDirection(Shimmer.Direction.LEFT_TO_RIGHT)
            .setAutoStart(true)
    baseColor?.let {
        colorHighlightBuilder.setBaseColor(ContextCompat.getColor(context, it))
    }
    highlightColor?.let {
        colorHighlightBuilder.setBaseColor(ContextCompat.getColor(context, it))
    }
    return ShimmerDrawable().apply {
        setShimmer(colorHighlightBuilder.build())
    }
}