package ru.netology.nmedia.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.github.javafaker.Faker
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.util.getTime

@Entity
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    var author: String,
    val content: String,
    var published: String,
    val likes: Int = 0,
    val share: Int = 0,
    val view: Int = 0,
    val likedByMe: Boolean = false,
    val videoURL: String,
) {
    fun toDto() = Post(id, author, content, published, likes, share, view, likedByMe, videoURL)

    companion object {
        fun fromDto(dto: Post) =
            PostEntity(
                dto.id,
                Faker().name().fullName().also { dto.author = it },
                dto.content,
                getTime().also { dto.published = it },
                dto.likes,
                dto.share,
                dto.view,
                dto.likedByMe,
                dto.videoURL
            )
    }
}

