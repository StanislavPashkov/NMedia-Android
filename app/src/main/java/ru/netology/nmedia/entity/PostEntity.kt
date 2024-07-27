package ru.netology.nmedia.entity


import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.nmedia.dto.Post


@Entity
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val author: String,
    val authorAvatar: String,
    val content: String,
    val published: String,
    val likes: Int = 0,
    val share: Int = 0,
    val view: Int = 0,
    val likedByMe: Boolean = false,
    // val videoURL: String,
) {
    fun toDto() = Post(
        id = id,
        author = author,
        authorAvatar = authorAvatar,
        content = content,
        published = published,
        likes = likes,
        share = share,
        view = view,
        likedByMe = likedByMe
    )

    companion object {
        fun fromDto(dto: Post) =
            PostEntity(
                id = dto.id,
                author = dto.author,
                authorAvatar = dto.authorAvatar,
                content = dto.content,
                published = dto.published,
                likes = dto.likes,
                share = dto.share,
                view = dto.view,
                likedByMe = dto.likedByMe,
                //dto.videoURL
            )
    }
}

