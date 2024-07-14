package ru.netology.nmedia.extensions

import android.widget.ImageView
import com.bumptech.glide.Glide
import ru.netology.nmedia.R

fun ImageView.loadAvatars(url: String){
    Glide.with(this)
        .load(url)
        .placeholder(R.drawable.ic_loading_24dp)
        .error(R.drawable.ic_baseline_error_outline_24dp)
        .timeout(10_000)
        .circleCrop()
        .into(this)
}
fun ImageView.loadAttachment(url: String){
    Glide.with(this)
        .load(url)
        .placeholder(R.drawable.ic_loading_24dp)
        .error(R.drawable.ic_baseline_error_outline_24dp)
        .timeout(10_000)
        .into(this)
}