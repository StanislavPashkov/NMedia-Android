package ru.netology.nmedia.activity

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.repository.Calc
import ru.netology.nmedia.viewmodel.PostViewModel


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel: PostViewModel by viewModels()
        viewModel.data.observe(this) { post ->
            with(binding) {
                author.text = post.author
                published.text = post.published
                content.text = post.content
                likeCount.text = Calc.converter(post.likes)
                shareCount.text = Calc.converter(post.share)
                vievCount.text = Calc.converter(post.view)

                like.setImageResource(
                    if (post.likedByMe) R.drawable.ic_liked_24 else R.drawable.ic_like_24
                )
            }
        }
        binding.share.setOnClickListener {
            viewModel.share()
        }
        binding.like.setOnClickListener {
            viewModel.like()
        }
        binding.root.setOnClickListener {
            Log.d("MyLog", "clickRoot")
        }

        binding.avatar.setOnClickListener {
            Log.d("MyLog", "clickAvatar")
        }
    }
}



