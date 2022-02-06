package com.waldemlav.testtask.ui.photos.details

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.waldemlav.testtask.databinding.CommentItemBinding
import com.waldemlav.testtask.domain.model.CommentData

class CommentAdapter(private val onLongClick: (CommentData) -> Unit
) : ListAdapter<CommentData, CommentAdapter.CommentViewHolder>(DiffCallback) {

    class CommentViewHolder(
        private var binding: CommentItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(comment: CommentData) {
            binding.comment = comment
            // This is important, because it forces the data binding to execute immediately,
            // which allows the RecyclerView to make the correct view size measurements
            binding.executePendingBindings()
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<CommentData>() {
        override fun areItemsTheSame(oldItem: CommentData, newItem: CommentData): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CommentData, newItem: CommentData): Boolean {
            return oldItem.text == newItem.text && oldItem.date == newItem.date
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CommentViewHolder {
        return CommentViewHolder(
            CommentItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val comment = getItem(position)
        holder.bind(comment)
        holder.itemView.setOnLongClickListener {
            onLongClick(comment)
            true
        }
    }
}
