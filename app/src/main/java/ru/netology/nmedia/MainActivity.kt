package ru.netology.nmedia

import android.os.Bundle
import android.util.Log
import android.util.Log.d
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val post = Post(
            id = 1,
            author = getString(R.string.author),
            content = getString(R.string.content),
            published = getString(R.string.published),
            likedByMe = false
        )
        with(binding) {
            author.text = post.author
            published.text = post.published
            content.text = post.content
            likeCount.text = Calc.converter(post.likes)
            shareCount.text = Calc.converter(post.share)
            vievCount.text = Calc.converter(post.view)
            binding.like.setOnClickListener {
                post.likedByMe = !post.likedByMe
                binding.like.setImageResource(
                    if (post.likedByMe) R.drawable.ic_liked_24 else R.drawable.ic_like_24
                )
                if (post.likedByMe)
                    post.likes++.toString()
                else post.likes--.toString()
                likeCount.text = Calc.converter(post.likes)
            }
            binding.share.setOnClickListener {
                post.share++
                shareCount.text = Calc.converter(post.share)
            }
        }
    }
}


