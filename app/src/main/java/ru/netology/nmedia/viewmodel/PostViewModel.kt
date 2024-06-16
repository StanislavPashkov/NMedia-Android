package ru.netology.nmedia.viewmodel


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.model.FeedModel
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryImpl
import ru.netology.nmedia.util.SingleLiveEvent
import java.io.IOException
import kotlin.concurrent.thread

private val empty = Post(
    id = 0,
    content = "",
    author = "",
    likedByMe = false,
    likes = 0,
    share = 0,
    published = "",
    videoURL = "",

    )

class PostViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PostRepository = PostRepositoryImpl(
        //AppDb.getInstance(context = application).postDao()
    )

    //    val data = repository.getAll()
    private val _data = MutableLiveData<FeedModel>()
    val data: LiveData<FeedModel>
        get() = _data
    private val _postCreated = SingleLiveEvent<Unit>()
    val postCreated: LiveData<Unit>
        get() = _postCreated

    init {
        load()
    }

    fun load() {
        _data.postValue(FeedModel(loading = true))
        repository.getAllAsync(object : PostRepository.Callback<List<Post>> {
            override fun onSuccess(data: List<Post>) {
                _data.postValue(FeedModel(posts = data, empty = data.isEmpty()))
            }

            override fun onError(e: Exception) {
                _data.postValue(FeedModel(error = true))
            }
        })
    }

    val edited = MutableLiveData(empty)
    fun likeById(id: Long) {
        thread {
            val post = _data.value?.posts?.find { it.id == id } ?: empty
            _data.postValue(_data.value?.copy(
                posts = _data.value?.posts.orEmpty().map {
                    if (it.id == id) repository.likeById(post) else it
                }
            )
            )
        }
    }

    fun shareById(id: Long) {
        repository.shareById(id)
    }


    fun removeById(id: Long) {
        repository.removeById(id, object : PostRepository.Callback<Post> {
            override fun onSuccess(data: Post) {
                _data.postValue(_data.value?.copy
                    (posts = _data.value?.posts.orEmpty().filter {
                    it.id != id
                })
                )
            }
            override fun onError(e: Exception) {
                _data.value
            }
        }
        )
        load()
    }

    fun edit(post: Post) {
        edited.postValue(post)
    }

    fun playMedia(id: Long) {
        repository.playMedia(id)
    }
    fun save() {
        edited.value?.let {
                repository.save(it, object  : PostRepository.Callback<Post> {
                    override fun onSuccess(post: Post) {
                        _postCreated.postValue(Unit)
                        edited.postValue(empty)
                    }
                    override fun onError(e: Exception) {
                        edited.postValue(empty)
                    }
                })
        }
    }
    fun changeContent(content: String) {
        val text = content.trim()
        if (edited.value?.content == text) {
            return
        }
        edited.value = edited.value?.copy(content = text)
    }
//    fun changeContentAndSave(content: String) {
//        edited.value?.let {
//            val text = content.trim()
//            if (it.content != text.trim()) {
//                repository.save(it, object : PostRepository.Callback<Post> {
//                    override fun onSuccess(data: Post) {
//                        it.copy(content = text)
//                        _postCreated.postValue(Unit)
//                    }
//
//                    override fun onError(e: Exception) {
//                        _data.postValue(FeedModel(error = true))
//                    }
//                })
//            }
//            edited.value = empty
//        }
//    }

    fun editCancel() {
        edited.value = empty
    }

}