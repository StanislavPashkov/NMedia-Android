package ru.netology.nmedia.dto

data class Post(
    val id: Long,
    var author: String,
    val content: String,
    var published: String,
    val likes: Int = 0,
    val share: Int = 0,
    val view: Int = 0,
    val likedByMe: Boolean = false,
    val videoURL: String,
    )
