package ru.netology.nmedia

data class Post(
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    var likes: Int = 999,
    var share: Int = 9999,
    var view: Int = 100,
    var likedByMe: Boolean = false,

    )
