package com.onething.a2appstudiotest.utils

import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.onething.a2appstudiotest.R
import com.onething.a2appstudiotest.features.links.LinksListAdapter
import com.onething.a2appstudiotest.model.Links

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<Links>?) {
    val adapter = recyclerView.adapter as LinksListAdapter
    adapter.submitList(data)
}

@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        imgView.load(it) {
            placeholder(R.drawable.loading)
            crossfade(true)
            error(R.drawable.error_placeholder)
        }
    }
}