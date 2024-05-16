package ru.netology.nmedia.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.R
import ru.netology.nmedia.adapter.OnInteractoinListener
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.viewmodel.PostViewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel: PostViewModel by viewModels()
        val newPostLauncher = registerForActivityResult(NewPostContract){
            viewModel.editCancel()
            val result = it ?: return@registerForActivityResult
            viewModel.changeContentAndSave(result)
        }
        val adapter = PostsAdapter(object : OnInteractoinListener
            {
                override fun onLike(post: Post) {
                    viewModel.likeById(post.id)
                }

                override fun onShare(post: Post) {
                    viewModel.shareById(post.id)
                    val intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, post.content)
                        type = "text/plain"
                    }

                    val shareIntent =
                        Intent.createChooser(intent, getString(R.string.chooser_share_post))
                    startActivity(shareIntent)
                    viewModel.shareById(post.id)
                }

                override fun onEdit(post: Post) {
                    viewModel.edit(post)
                    newPostLauncher.launch(post.content)
                }

                override fun onRemove(post: Post) {
                    viewModel.removeById(post.id)
                }
                override fun playMedia(post: Post) {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(post.videoURL))
                    if (intent.resolveActivity(packageManager) != null) {
                        startActivity(intent)
                    }
                    viewModel.playMedia(post.id)
                    viewModel.editCancel()
                }

                override fun openPost(post: Post) {
                    viewModel.playMedia(post.id)
                    viewModel.editCancel()
                }
            }
        )
        binding.list.adapter = adapter
        viewModel.data.observe(this) { posts ->
            val newPost = posts.size > adapter.currentList.size
            adapter.submitList(posts) {
                if (newPost) {
                    binding.list.smoothScrollToPosition(0)
                }
            }
        }
//        viewModel.edited.observe(this){
//            if (it.id != 0L){
//                binding.content.setText(it.content)
//                binding.editText.setText(it.content)
//                binding.content.focusAndShowKeyboard()
//                binding.editGroup.visibility = View.VISIBLE
//            }
//        }
        binding.fab.setOnClickListener {
            viewModel.editCancel()
            newPostLauncher.launch(" ")
        }

//        binding.editCancel.setOnClickListener{
//            binding.content.setText("")
//            binding.content.clearFocus()
//            binding.editGroup.visibility = View.GONE
//            viewModel.editCancel()
//            AndroidUtils.hideKeyboard(binding.content)
//        }
    }
}



