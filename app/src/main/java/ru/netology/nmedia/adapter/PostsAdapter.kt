package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.util.Calc

interface OnInteractoinListener {
    fun onLike(post: Post)
    fun onShare(post: Post)
    fun onEdit(post: Post)
    fun onRemove(post: Post)
    fun playMedia(post: Post)
    fun openPost(post: Post)
}


class PostsAdapter(
    private val onInteractoinListener: OnInteractoinListener
) :
    ListAdapter<Post, PostViewHolder>(PostDiffUtil) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding, onInteractoinListener)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


}

class PostViewHolder(
    private val binding: CardPostBinding,
    private val onInteractionListener: OnInteractoinListener
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(post: Post) {
        binding.apply {
            author.text = post.author
            published.text = post.published
            content.text = post.content
            like.isChecked = post.likedByMe
            like.text = Calc.converter(post.likes)
            share.text = Calc.converter(post.share)
            view.text = Calc.converter(post.view)

            val url = "http://10.0.2.2:9999/avatars/${post.authorAvatar}"
            Glide.with(itemView)
                .load(url)
                .placeholder(R.drawable.ic_loading_24dp)
                .error(R.drawable.ic_baseline_error_outline_24dp)
                .timeout(10_000)
                .circleCrop()
                .into(avatar)

            val urlAttachment = "http://10.0.2.2:9999/images/${post.attachment?.url}"
            if (post.attachment != null) {
                attachment.visibility = View.VISIBLE
                Glide.with(itemView)
                    .load(urlAttachment)
                    .placeholder(R.drawable.ic_loading_24dp)
                    .error(R.drawable.ic_baseline_error_outline_24dp)
                    .timeout(10_000)
                    .into(attachment)
            } else {
                attachment.visibility = View.GONE
            }


//            if (post.videoURL != "") {
//                group.visibility = View.VISIBLE
//                content.text = post.content
//                tvWatch.text = post.view.toString() + " просмотров"
//            } else {
//                group.visibility = View.GONE
//                content.visibility = View.VISIBLE
//            }

            like.setOnClickListener {
                onInteractionListener.onLike(post)
            }

            share.setOnClickListener {
                onInteractionListener.onShare(post)
            }
            content.setOnClickListener {
                onInteractionListener.openPost(post)
            }

            menu.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.option_post)
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.edit -> {
                                onInteractionListener.onEdit(post)
                                true
                            }

                            R.id.remove -> {
                                onInteractionListener.onRemove(post)
                                true
                            }

                            else -> false
                        }
                    }
                }.show()
            }
            play.setOnClickListener() {
                onInteractionListener.playMedia(post)
            }

            imgPlay.setOnClickListener() {
                onInteractionListener.playMedia(post)
            }
        }
    }
}

object PostDiffUtil : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem == newItem
    }

}