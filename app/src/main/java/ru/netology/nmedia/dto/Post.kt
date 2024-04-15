package ru.netology.nmedia.dto

data class Post(
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    var likes: Int = 999,
    var share: Int = 999,
    var view: Int = 0,
    var likedByMe: Boolean = false,

    )
