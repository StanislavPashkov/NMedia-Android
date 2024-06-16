package ru.netology.nmedia.repository

import ru.netology.nmedia.dto.Post

interface PostRepository {
    //    fun getAll(): LiveData<List<Post>>
    //fun getAll(): List<Post>
    fun getAllAsync(callback: Callback<List<Post>>)
    fun likeById(post: Post): Post
    fun shareById(id: Long)
    fun removeById(id: Long, callback: Callback<Post>)
    fun save(post: Post, callback: Callback<Post>)
    fun playMedia(id: Long)

    interface Callback<T> {
        fun onSuccess(data: T)
        fun onError(e: Exception)
    }
}