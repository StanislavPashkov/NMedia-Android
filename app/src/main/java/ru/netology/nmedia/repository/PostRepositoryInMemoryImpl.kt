package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.R
import ru.netology.nmedia.dto.Post

class PostRepositoryInMemoryImpl : PostRepository {
    private var post = Post(
        1,
        author = "Нетология. Университет интернет-профессий будущего",
        content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
        published = "21 мая в 18:36",
        likedByMe = false
    )
    private val data = MutableLiveData(post)

    override fun get(): LiveData<Post> = data
    override fun like() {
        post = post.copy(
            likedByMe = !post.likedByMe,
            likes = if (post.likedByMe) {
                post.likes - 1
            } else {
                post.likes + 1
            }
            )
        data.value = post
    }

    override fun share() {
        val localSharesCount = post.share
        post = post.copy(share = localSharesCount + 1)
        data.value = post
    }
}
object Calc {
    fun converter(value: Int): String {
        val convertedValue = when(value) {
            in 0 .. 999 -> "$value"
            in 1000 .. 1099 -> "${(value/1000)}K"
            in 1100 .. 9999 -> "${String.format("%.1f", value.toDouble()/1000)}K"
            in 10000 .. 999999 -> "${(value/1000)}K"
            else -> if (value > 0) "${String.format("%.1f", value.toDouble()/1_000_000)}М" else "Negative"
        }
        return convertedValue
    }
}