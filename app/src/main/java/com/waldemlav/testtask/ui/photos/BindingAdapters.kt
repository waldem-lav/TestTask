package com.waldemlav.testtask.ui.photos

import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.waldemlav.testtask.R
import com.waldemlav.testtask.domain.model.CommentData
import com.waldemlav.testtask.domain.model.PhotoData
import com.waldemlav.testtask.ui.photos.details.CommentAdapter
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<PhotoData>?) {
    val adapter = recyclerView.adapter as PhotoGridAdapter
    adapter.submitList(data)
}

@BindingAdapter("listData")
fun bindCommentsRecyclerView(recyclerView: RecyclerView, data: List<CommentData>?) {
    val adapter = recyclerView.adapter as CommentAdapter
    adapter.submitList(data)
}

@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
        imgView.load(imgUri) {
            placeholder(R.drawable.loading_animation)
            error(R.drawable.ic_broken_image)
        }
    }
}

@BindingAdapter("dateText")
fun bindDateText(textView: TextView, date: Long) {
    val formatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    textView.text = formatter.format(date * 1000)
}

@BindingAdapter("dateHoursText")
fun bindDateHoursText(textView: TextView, date: Long) {
    val formatter = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
    textView.text = formatter.format(date * 1000)
}

@BindingAdapter("dateHoursInAmPmText")
fun bindDateHoursInAmPmText(textView: TextView, date: Long) {
    val formatter = SimpleDateFormat("dd.MM.yyyy hh:mm a", Locale.getDefault())
    textView.text = formatter.format(date * 1000)
}