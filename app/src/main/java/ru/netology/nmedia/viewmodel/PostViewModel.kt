package ru.netology.nmedia.viewmodel


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.javafaker.Faker
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.model.FeedModel
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryImpl
import ru.netology.nmedia.util.SingleLiveEvent
import ru.netology.nmedia.util.getTime
import kotlin.concurrent.thread

private val empty = Post(
    id = 0,
    content = "",
    author = "",
    authorAvatar = "",
    likedByMe = false,
    likes = 0,
    share = 0,
    published = "",
    //videoURL = "",

)

class PostViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PostRepository = PostRepositoryImpl(application)


    //    val data = repository.getAll()
    private val _data = MutableLiveData<FeedModel>()
    val data: LiveData<FeedModel>
        get() = _data
    private val _postCreated = SingleLiveEvent<Unit>()
    val postCreated: LiveData<Unit>
        get() = _postCreated

    private val _error = SingleLiveEvent<String>()
    val error: LiveData<String>
        get() = _error

    private fun error(error: String?) {
        error.let {
            _error.postValue(it)
        }
    }

    init {
        load()
    }

    fun load() {
        _data.postValue(FeedModel(loading = true))
        repository.getAllAsync(object : PostRepository.NMediaCallback<List<Post>> {
            override fun onSuccess(data: List<Post>) {
                _data.postValue(FeedModel(posts = data, empty = data.isEmpty()))
            }

            override fun onError(e: Exception) {
                error(e.message)
                _data.postValue(FeedModel(error = true))
            }
        })
    }

    val edited = MutableLiveData(empty)

    fun likeById(id: Long) {
        repository.likeById(id, object : PostRepository.NMediaCallback<Post> {
            override fun onError(e: Exception) {
                _data.postValue(_data.value?.copy(error = true))
            }

            override fun onSuccess(posts: Post) {
                _data.postValue(
                    _data.value?.copy(posts = _data.value?.posts.orEmpty()
                        .map {
                            if (it.id == id) posts else it
                        }
                    )
                )
            }
        })
    }

    fun dislikeById(id: Long) {
        repository.dislikeById(id, object : PostRepository.NMediaCallback<Post> {
            override fun onError(e: Exception) {
                _data.postValue(_data.value?.copy(error = true))
            }

            override fun onSuccess(posts: Post) {
                _data.postValue(
                    _data.value?.copy(posts = _data.value?.posts.orEmpty()
                        .map {
                            if (it.id == id) posts else it
                        }
                    )
                )
            }
        })
    }

    fun shareById(id: Long) {
        repository.shareById(id)
    }


    fun removeById(id: Long) {
        _data.value = _data.value?.copy(posts = _data.value?.posts.orEmpty()
            .filter { it.id != id }
        )
        repository.removeById(id, object : PostRepository.NMediaCallback<Unit> {
            override fun onSuccess(data: Unit) {
            }

            override fun onError(e: Exception) {
                error(e.message)
                //_data.postValue(_data.value?.copy(error = true))
            }
        })
    }

    fun edit(post: Post) {
        edited.postValue(post)
    }

    fun playMedia(id: Long) {
        repository.playMedia(id)
    }

    fun changeContentAndSave(content: String) {
        val faker = Faker()
        val post = edited.value?.copy(content = content, author = faker.name().fullName()) ?: return
        //repository.save(post)
        repository.save(post, object : PostRepository.NMediaCallback<Post> {
            override fun onSuccess(post: Post) {
                _data.postValue(_data.value?.copy(posts = _data.value?.posts.orEmpty().plus(post)))
                _postCreated.postValue(Unit)
            }

            override fun onError(e: Exception) {
                error(e.message)
                println("Error saving post: ${e.message}")
            }
        })

    }

    fun editCancel() {
        edited.value = empty
    }
}