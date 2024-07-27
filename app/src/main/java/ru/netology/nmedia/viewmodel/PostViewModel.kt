package ru.netology.nmedia.viewmodel


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.github.javafaker.Faker
import kotlinx.coroutines.launch
import ru.netology.nmedia.db.AppDb
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.model.FeedModel
import ru.netology.nmedia.model.FeedModelState
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryImpl
import ru.netology.nmedia.util.SingleLiveEvent

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

    private val repository: PostRepository = with(AppDb.getInstance(context = application)) {
        PostRepositoryImpl(postDao(), draftDao())
    }
    private val _state = MutableLiveData<FeedModelState>()
    val state: LiveData<FeedModelState>
        get() = _state
    val data: LiveData<FeedModel> = repository.data.map {
        FeedModel(posts = it, empty = it.isEmpty())
    }
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
        loadPosts()
    }

    fun loadPosts() {
        viewModelScope.launch {
            _state.postValue(FeedModelState(loading = true))
            _state.value = try {
                repository.getAll()
                FeedModelState()
            } catch (e: Exception) {
                FeedModelState(error = true)
            }
        }
    }

    fun refreshPosts() {
        viewModelScope.launch {
            _state.postValue(FeedModelState(refreshing = true))
            _state.value = try {
                repository.getAll()
                FeedModelState()
            } catch (e: Exception) {
                FeedModelState(error = true)
            }
        }
    }

    val edited = MutableLiveData(empty)

    fun likeById(id: Long) {
        viewModelScope.launch {
            try {
                repository.likeById(id)
            } catch (e: Exception) {
                FeedModelState(error = true)
            }
        }
//        repository.likeById(id, object : PostRepository.NMediaCallback<Post> {
//            override fun onError(e: Exception) {
//                _data.postValue(_data.value?.copy(error = true))
//            }
//
//            override fun onSuccess(posts: Post) {
//                _data.postValue(
//                    _data.value?.copy(posts = _data.value?.posts.orEmpty()
//                        .map {
//                            if (it.id == id) posts else it
//                        }
//                    )
//                )
//            }
//        })
    }

    fun dislikeById(id: Long) {
//        repository.dislikeById(id, object : PostRepository.NMediaCallback<Post> {
//            override fun onError(e: Exception) {
//                _data.postValue(_data.value?.copy(error = true))
//            }
//
//            override fun onSuccess(posts: Post) {
//                _data.postValue(
//                    _data.value?.copy(posts = _data.value?.posts.orEmpty()
//                        .map {
//                            if (it.id == id) posts else it
//                        }
//                    )
//                )
//            }
//        })
    }

    fun shareById(id: Long) {
        viewModelScope.launch {
            repository.shareById(id)
        }

    }


    fun removeById(id: Long) {
        viewModelScope.launch {
            try {
                repository.removeById(id)
                FeedModelState()
            } catch (e: Exception) {
                FeedModelState(error = true)
            }
        }
//        _data.value = _data.value?.copy(posts = _data.value?.posts.orEmpty()
//            .filter { it.id != id }
//        )
//        repository.removeById(id, object : PostRepository.NMediaCallback<Unit> {
//            override fun onSuccess(data: Unit) {
//            }
//
//            override fun onError(e: Exception) {
//                error(e.message)
//                //_data.postValue(_data.value?.copy(error = true))
//            }
//        })
    }

    fun edit(post: Post) {
        edited.postValue(post)
    }

    fun changeContentAndSave(content: String) {

        viewModelScope.launch {
            val faker = Faker()
            try {
                edited.value?.let {
                    repository.save(it.copy(content = content, author = faker.name().fullName()))
                    _postCreated.postValue(Unit)
                }
                edited.value = empty
            } catch (e: Exception) {
                _postCreated.postValue(Unit)
            }
        }
//        viewModelScope.launch {
//            val faker = Faker()
//            val post = edited.value?.copy(content = content, author = faker.name().fullName()) ?: return@launch
//            repository.save(post)
//            _postCreated.postValue(Unit)
//        }
    }

    fun editCancel() {
        edited.value = empty
    }
}