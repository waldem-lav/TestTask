package com.waldemlav.testtask.ui.photos

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.waldemlav.testtask.databinding.GridViewItemBinding
import com.waldemlav.testtask.domain.model.PhotoData

class PhotoGridAdapter(
    private val onItemClicked: (PhotoData) -> Unit,
    private val onLongClick: (PhotoData) -> Unit
) : ListAdapter<PhotoData, PhotoGridAdapter.PhotosViewHolder>(DiffCallback) {

    class PhotosViewHolder(
        private var binding: GridViewItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(photo: PhotoData) {
            binding.photo = photo
            // This is important, because it forces the data binding to execute immediately,
            // which allows the RecyclerView to make the correct view size measurements
            binding.executePendingBindings()
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<PhotoData>() {
        override fun areItemsTheSame(oldItem: PhotoData, newItem: PhotoData): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: PhotoData, newItem: PhotoData): Boolean {
            return oldItem.url == newItem.url && oldItem.date == newItem.date
                    && oldItem.lat == newItem.lat && oldItem.lng == newItem.lng
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PhotosViewHolder {
        return PhotosViewHolder(
            GridViewItemBinding.inflate(LayoutInflater.from(parent.context))
        )
    }

    override fun onBindViewHolder(holder: PhotosViewHolder, position: Int) {
        val photoObject = getItem(position)
        holder.bind(photoObject)
        holder.itemView.setOnClickListener {
            onItemClicked(photoObject)
        }
        holder.itemView.setOnLongClickListener {
            onLongClick(photoObject)
            true
        }
    }
}
