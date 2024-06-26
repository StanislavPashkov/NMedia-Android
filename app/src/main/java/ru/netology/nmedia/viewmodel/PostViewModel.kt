package ru.netology.nmedia.viewmodel


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryFilesImpl

private val empty = Post(
    id = 0,
    content = "",
    author = "",
    likedByMe = false,
    likes = 0,
    published = "",
    videoURL = "",

)

class PostViewModel (application: Application): AndroidViewModel(application) {

    private val repository: PostRepository = PostRepositoryFilesImpl(application)
    val data = repository.getAll()
    private val edited = MutableLiveData(empty)
    fun likeById(id: Long) = repository.likeById(id)
    fun shareById(id: Long) = repository.shareById(id)
    fun removeById(id: Long) = repository.removeById(id)
    fun edit(post: Post) {
        edited.value = post
    }
    fun playMedia(id: Long) = repository.playMedia(id)

    fun changeContentAndSave(text: String) {
        edited.value?.let {
            if (it.content != text.trim()) {
                repository.save(it.copy(content = text))
            }
            edited.value = empty
        }
    }

    fun editCancel() {
        edited.value = empty
    }


}