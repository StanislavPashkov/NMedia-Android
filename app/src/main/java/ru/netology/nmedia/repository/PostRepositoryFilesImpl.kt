package ru.netology.nmedia.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.util.getTime

class PostRepositoryFilesImpl(private val context: Context) : PostRepository {
    companion object {
        private const val FILE_NAME = "posts.json"
    }

    private val gson = Gson()
    private val typeToken = TypeToken.getParameterized(List::class.java, Post::class.java).type
    private var posts = emptyList<Post>()
        private set(value) {
            field = value
            sync()
        }

    private var defaultPosts = listOf(
        Post(
            id = 9,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Освоение новой профессии — это не только открывающиеся возможности и перспективы, но и настоящий вызов самому себе. Приходится выходить из зоны комфорта и перестраивать привычный образ жизни: менять распорядок дня, искать время для занятий, быть готовым к возможным неудачам в начале пути. В блоге рассказали, как избежать стресса на курсах профпереподготовки → http://netolo.gy/fPD",
            published = "23 сентября в 10:12",
            likedByMe = false,
            videoURL = "",

            ),
        Post(
            id = 8,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Нетология что это такое? (исповедь человека, прошедшего Нетологию)",
            published = "22 сентября в 10:14",
            likedByMe = false,
            videoURL = "https://yandex.ru/video/preview/15202559606961200008",

            ),
        Post(
            id = 7,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Таймбоксинг — отличный способ навести порядок в своём календаре и разобраться с делами, которые долго откладывали на потом. Его главный принцип — на каждое дело заранее выделяется определённый отрезок времени. В это время вы работаете только над одной задачей, не переключаясь на другие. Собрали советы, которые помогут внедрить таймбоксинг \uD83D\uDC47\uD83C\uDFFB",
            published = "22 сентября в 10:12",
            likedByMe = false,
            videoURL = "",

            ),
        Post(
            id = 6,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "24 сентября стартует новый поток бесплатного курса «Диджитал-старт: первый шаг к востребованной профессии» — за две недели вы попробуете себя в разных профессиях и определите, что подходит именно вам → http://netolo.gy/fQ",
            published = "21 сентября в 10:12",
            likedByMe = false,
            videoURL = "",

            ),
        Post(
            id = 5,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Диджитал давно стал частью нашей жизни: мы общаемся в социальных сетях и мессенджерах, заказываем еду, такси и оплачиваем счета через приложения.",
            published = "20 сентября в 10:14",
            likedByMe = false,
            videoURL = "",

            ),
        Post(
            id = 4,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Большая афиша мероприятий осени: конференции, выставки и хакатоны для жителей Москвы, Ульяновска и Новосибирска \uD83D\uDE09",
            published = "19 сентября в 14:12",
            likedByMe = false,
            videoURL = "",

            ),
        Post(
            id = 3,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Языков программирования много, и выбрать какой-то один бывает нелегко. Собрали подборку статей, которая поможет вам начать, если вы остановили свой выбор на JavaScript.",
            published = "19 сентября в 10:24",
            likedByMe = false,
            videoURL = "",


            ),
        Post(
            id = 2,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Знаний хватит на всех: на следующей неделе разбираемся с разработкой мобильных приложений, учимся рассказывать истории и составлять PR-стратегию прямо на бесплатных занятиях \uD83D\uDC47",
            published = "18 сентября в 10:12",
            likedByMe = false,
            videoURL = "",

            ),
        Post(
            id = 1,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
            published = "21 мая в 18:36",
            likedByMe = false,
            videoURL = "",

            )
    )
    private var nextId = 1L

    private val data = MutableLiveData(posts)


    init {
        val file = context.filesDir.resolve(FILE_NAME)
        //nextId = posts.maxOfOrNull { it.id }?.inc() ?:  1
        //nextId = posts.maxOf { it.id } + 1
        if (file.exists()) {
            nextId = posts.maxOf { it.id } + 1
            context.openFileInput(FILE_NAME).bufferedReader().use { it ->
                posts = gson.fromJson(it, typeToken)
                //nextId = posts.maxOf { it.id } + 1
            }
        } else {
            posts = defaultPosts
        }
        data.value = posts
    }

    private fun sync() {
        context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE).bufferedWriter().use {
            it.write(gson.toJson(posts))
        }
    }

    override fun getAll(): LiveData<List<Post>> = data
    override fun likeById(id: Long) {
        posts = posts.map {
            if (it.id != id) it else it.copy(
                likedByMe = !it.likedByMe,
                likes = if (it.likedByMe) it.likes - 1 else it.likes + 1

            )
        }
        data.value = posts


    }

    override fun shareById(id: Long) {
        posts = posts.map {
            if (it.id != id) it else it.copy(share = 1 + it.share)
        }
        data.value = posts

    }

    override fun removeById(id: Long) {
        posts = posts.filter { it.id != id }
        data.value = posts

    }

    override fun save(post: Post) {
        posts = if (post.id == 0L) {
            listOf(
                post.copy(
                    id = nextId++,
                    author = "Me",
                    likedByMe = false,
                    published = getTime()
                )
            ) + posts
        } else {
            posts.map { if (it.id == post.id) it.copy(content = post.content) else it }
        }
        data.value = posts


    }

    override fun playMedia(id: Long) {
        posts = posts.map {
            if (it.id != id) it else it.copy(view = it.view + 1)
        }
        data.value = posts

    }
}
