package ru.netology.nmedia.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import ru.netology.nmedia.R
import ru.netology.nmedia.activity.NewPostFragment.Companion.textArg
import ru.netology.nmedia.adapter.OnInteractoinListener
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.databinding.FragmentFeedBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.viewmodel.PostViewModel

class FeedFragment : Fragment() {
    //    private val viewModel: PostViewModel by viewModels(
//        ownerProducer = ::requireParentFragment
//    )
    private val viewModel: PostViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentFeedBinding.inflate(
            inflater,
            container,
            false
        )


        val adapter = PostsAdapter(object : OnInteractoinListener {
            override fun onLike(post: Post) {
                viewModel.likeById(post.id)
                //if (!post.likedByMe) viewModel.likeById(post.id) else viewModel.dislikeById(post.id)
            }

            override fun onShare(post: Post) {
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
                findNavController().navigate(R.id.action_feedFragment_to_newPostFragment,
                    Bundle().apply { textArg = post.content })
                viewModel.edit(post)

            }

            override fun onRemove(post: Post) {
                viewModel.removeById(post.id)
            }

            override fun playMedia(post: Post) {
//                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(post.videoURL))
//
//                startActivity(intent)
//                viewModel.playMedia(post.id)
//                viewModel.editCancel()
            }

            override fun openPost(post: Post) {
                findNavController().navigate(
                    R.id.action_feedFragment_to_postSingleFragment,
                    Bundle().apply { textArg = post.id.toString() }
                )
            }
        })

        binding.list.adapter = adapter
        viewModel.data.observe(viewLifecycleOwner) { state ->
            val newPost = state.posts.size > adapter.currentList.size
            binding.emptyState.isVisible = state.empty
            adapter.submitList(state.posts) {
                if (newPost) {
                    binding.list.smoothScrollToPosition(0)
                }
            }
        }
        viewModel.state.observe(viewLifecycleOwner) { state ->
            if (state.error) {
                Snackbar.make(binding.root, R.string.error_loading, Snackbar.LENGTH_SHORT)
                    .setAction(R.string.retry) {
                        viewModel.loadPosts()
                    }
                    .show()
            }
            binding.progress.isVisible = state.loading
            binding.swipe.isRefreshing = state.refreshing
        }

//        viewModel.data.observe(viewLifecycleOwner) { posts ->
//            val newPost = posts.size > adapter.currentList.size
//            adapter.submitList(posts){
//                if (newPost) {
//                    binding.list.smoothScrollToPosition(0)
//                }
//            }
//        }

        binding.fab.setOnClickListener {
            //viewModel.editCancel()
            findNavController().navigate(R.id.action_feedFragment_to_newPostFragment)
        }

        binding.swipe.setOnRefreshListener {
            viewModel.refreshPosts()
        }
        viewModel.error.observe(viewLifecycleOwner) { error ->
            Toast.makeText(
                context,
                error,
                Toast.LENGTH_SHORT
            )
                .show()
        }
        return binding.root
    }
}



