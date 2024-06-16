package ru.netology.nmedia.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import ru.netology.nmedia.R
import ru.netology.nmedia.adapter.OnInteractoinListener
import ru.netology.nmedia.adapter.PostViewHolder
import ru.netology.nmedia.databinding.FragmentPostSingleBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.util.StringArg
import ru.netology.nmedia.viewmodel.PostViewModel
import java.nio.file.Files.find

class PostSingleFragment : Fragment() {
    companion object {
        var Bundle.textArg: String? by StringArg
    }

    //    private val viewModel: PostViewModel by viewModels(
//        ownerProducer = ::requireParentFragment
//    )
    private val viewModel: PostViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentPostSingleBinding.inflate(
            inflater,
            container,
            false
        )


        viewModel.data.observe(viewLifecycleOwner) { model ->
            val post = model.posts.find { it.id == arguments?.textArg?.toLong() } ?: return@observe

            PostViewHolder(binding.singlePost, object : OnInteractoinListener {

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
                        Intent.createChooser(intent, "Share post")
                    startActivity(shareIntent)
                }

                override fun onEdit(post: Post) {
                    viewModel.edit(post)
                    findNavController().navigate(
                        R.id.action_postSingleFragment_to_newPostFragment,
                        Bundle().apply {
                            textArg = post.content
                        })
                    viewModel.edit(post)
                }

                override fun onRemove(post: Post) {
                    viewModel.removeById(post.id)
                    findNavController().navigateUp()
                }

                override fun playMedia(post: Post) {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(post.videoURL))
                    startActivity(intent)
                    viewModel.playMedia(post.id)
                    viewModel.editCancel()
                }

                override fun openPost(post: Post) {
                }
            })
                .bind(post)
        }
        return binding.root
    }
}