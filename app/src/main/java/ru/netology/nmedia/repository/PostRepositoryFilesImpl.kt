package ru.netology.nmedia.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.javafaker.Faker
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.util.getTime

class PostRepositoryFilesImpl(private val context: Context) : PostRepository {
    companion object {
        private const val FILE_NAME = "posts.json"
    }
    val faker = Faker()

    private val gson = Gson()
    private val typeToken = TypeToken.getParameterized(List::class.java, Post::class.java).type
    private var posts = emptyList<Post>()
        private set(value) {
            field = value
            sync()
        }
    private var nextId = 1L
    private var defaultPosts = listOf(
        Post(
            id = nextId++,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "import android.content.Context\n" +
                    "import androidx.lifecycle.LiveData\n" +
                    "import androidx.lifecycle.MutableLiveData\n" +
                    "import com.google.gson.Gson\n" +
                    "import com.google.gson.reflect.TypeToken\n" +
                    "import ru.netology.nmedia.dto.Post\n" +
                    "import ru.netology.nmedia.util.getTime\n" +
                    "\n" +
                    "class PostRepositoryFilesImpl(private val context: Context) : PostRepository {\n" +
                    "    companion object {\n" +
                    "        private const val FILE_NAME = \"posts.json\"\n" +
                    "    }\n" +
                    "\n" +
                    "    private val gson = Gson()\n" +
                    "    private val typeToken = TypeToken.getParameterized(List::class.java, Post::class.java).type\n" +
                    "    private var posts = emptyList<Post>()\n" +
                    "        private set(value) {\n" +
                    "            field = value\n" +
                    "            sync()\n" +
                    "        }\n" +
                    "    private var nextId = 1L\n" +
                    "    private var defaultPosts = listOf(\n" +
                    "        Post(\n" +
                    "            id = nextId++,\n" +
                    "            author = \"Нетология. Университет интернет-профессий будущего\",\n" +
                    "            content = \"Освоение новой профессии — это не только открывающиеся возможности и перспективы, но и настоящий вызов самому себе. Приходится выходить из зоны комфорта и перестраивать привычный образ жизни: менять распорядок дня, искать время для занятий, быть готовым к возможным неудачам в начале пути. В блоге рассказали, как избежать стресса на курсах профпереподготовки → http://netolo.gy/fPD\",\n" +
                    "            published = \"23 сентября в 10:12\",\n" +
                    "            likedByMe = false,\n" +
                    "            videoURL = \"\",\n" +
                    "\n" +
                    "            ),\n" +
                    "        Post(\n" +
                    "            id = nextId++,\n" +
                    "            author = \"Нетология. Университет интернет-профессий будущего\",\n" +
                    "            content = \"Нетология что это такое? (исповедь человека, прошедшего Нетологию)\",\n" +
                    "            published = \"22 сентября в 10:14\",\n" +
                    "            likedByMe = false,\n" +
                    "            videoURL = \"https://yandex.ru/video/preview/15202559606961200008\",\n" +
                    "\n" +
                    "            ),\n" +
                    "        Post(\n" +
                    "            id = nextId++,\n" +
                    "            author = \"Нетология. Университет интернет-профессий будущего\",\n" +
                    "            content = \"Таймбоксинг — отличный способ навести порядок в своём календаре и разобраться с делами, которые долго откладывали на потом. Его главный принцип — на каждое дело заранее выделяется определённый отрезок времени. В это время вы работаете только над одной задачей, не переключаясь на другие. Собрали советы, которые помогут внедрить таймбоксинг \\uD83D\\uDC47\\uD83C\\uDFFB\",\n" +
                    "            published = \"22 сентября в 10:12\",\n" +
                    "            likedByMe = false,\n" +
                    "            videoURL = \"\",\n" +
                    "\n" +
                    "            ),\n" +
                    "        Post(\n" +
                    "            id = nextId++,\n" +
                    "            author = \"Нетология. Университет интернет-профессий будущего\",\n" +
                    "            content = \"24 сентября стартует новый поток бесплатного курса «Диджитал-старт: первый шаг к востребованной профессии» — за две недели вы попробуете себя в разных профессиях и определите, что подходит именно вам → http://netolo.gy/fQ\",\n" +
                    "            published = \"21 сентября в 10:12\",\n" +
                    "            likedByMe = false,\n" +
                    "            videoURL = \"\",\n" +
                    "\n" +
                    "            ),\n" +
                    "        Post(\n" +
                    "            id = nextId++,\n" +
                    "            author = \"Нетология. Университет интернет-профессий будущего\",\n" +
                    "            content = \"Диджитал давно стал частью нашей жизни: мы общаемся в социальных сетях и мессенджерах, заказываем еду, такси и оплачиваем счета через приложения.\",\n" +
                    "            published = \"20 сентября в 10:14\",\n" +
                    "            likedByMe = false,\n" +
                    "            videoURL = \"\",\n" +
                    "\n" +
                    "            ),\n" +
                    "        Post(\n" +
                    "            id = nextId++,\n" +
                    "            author = \"Нетология. Университет интернет-профессий будущего\",\n" +
                    "            content = \"Большая афиша мероприятий осени: конференции, выставки и хакатоны для жителей Москвы, Ульяновска и Новосибирска \\uD83D\\uDE09\",\n" +
                    "            published = \"19 сентября в 14:12\",\n" +
                    "            likedByMe = false,\n" +
                    "            videoURL = \"\",\n" +
                    "\n" +
                    "            ),\n" +
                    "        Post(\n" +
                    "            id = nextId++,\n" +
                    "            author = \"Нетология. Университет интернет-профессий будущего\",\n" +
                    "            content = \"Языков программирования много, и выбрать какой-то один бывает нелегко. Собрали подборку статей, которая поможет вам начать, если вы остановили свой выбор на JavaScript.\",\n" +
                    "            published = \"19 сентября в 10:24\",\n" +
                    "            likedByMe = false,\n" +
                    "            videoURL = \"\",\n" +
                    "\n" +
                    "\n" +
                    "            ),\n" +
                    "        Post(\n" +
                    "            id = nextId++,\n" +
                    "            author = \"Нетология. Университет интернет-профессий будущего\",\n" +
                    "            content = \"Знаний хватит на всех: на следующей неделе разбираемся с разработкой мобильных приложений, учимся рассказывать истории и составлять PR-стратегию прямо на бесплатных занятиях \\uD83D\\uDC47\",\n" +
                    "            published = \"18 сентября в 10:12\",\n" +
                    "            likedByMe = false,\n" +
                    "            videoURL = \"\",\n" +
                    "\n" +
                    "            ),\n" +
                    "        Post(\n" +
                    "            id = nextId++,\n" +
                    "            author = \"Нетология. Университет интернет-профессий будущего\",\n" +
                    "            content = \"Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb\",\n" +
                    "            published = \"21 мая в 18:36\",\n" +
                    "            likedByMe = false,\n" +
                    "            videoURL = \"\",\n" +
                    "\n" +
                    "            )\n" +
                    "    )\n" +
                    "\n" +
                    "\n" +
                    "    private val data = MutableLiveData(posts)\n" +
                    "\n" +
                    "\n" +
                    "    init {\n" +
                    "        val file = context.filesDir.resolve(FILE_NAME)\n" +
                    "        if (file.exists()) {\n" +
                    "            context.openFileInput(FILE_NAME).bufferedReader().use {\n" +
                    "                posts = gson.fromJson(it, typeToken)\n" +
                    "            }\n" +
                    "        } else {\n" +
                    "            posts = defaultPosts\n" +
                    "        }\n" +
                    "        data.value = posts\n" +
                    "        nextId = posts.maxOfOrNull { it.id }?.inc() ?: 1\n" +
                    "    }\n" +
                    "\n" +
                    "    private fun sync() {\n" +
                    "        context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE).bufferedWriter().use {\n" +
                    "            it.write(gson.toJson(posts))\n" +
                    "        }\n" +
                    "    }\n" +
                    "\n" +
                    "    override fun getAll(): LiveData<List<Post>> = data\n" +
                    "    override fun likeById(id: Long) {\n" +
                    "        posts = posts.map {\n" +
                    "            if (it.id != id) it else it.copy(\n" +
                    "                likedByMe = !it.likedByMe,\n" +
                    "                likes = if (it.likedByMe) it.likes - 1 else it.likes + 1\n" +
                    "\n" +
                    "            )\n" +
                    "        }\n" +
                    "        data.value = posts\n" +
                    "\n" +
                    "\n" +
                    "    }\n" +
                    "\n" +
                    "    override fun shareById(id: Long) {\n" +
                    "        posts = posts.map {\n" +
                    "            if (it.id != id) it else it.copy(share = 1 + it.share)\n" +
                    "        }\n" +
                    "        data.value = posts\n" +
                    "\n" +
                    "    }\n" +
                    "\n" +
                    "    override fun removeById(id: Long) {\n" +
                    "        posts = posts.filter { it.id != id }\n" +
                    "        data.value = posts\n" +
                    "\n" +
                    "    }\n" +
                    "\n" +
                    "    override fun save(post: Post) {\n" +
                    "        posts = if (post.id == 0L) {\n" +
                    "            listOf(\n" +
                    "                post.copy(\n" +
                    "                    id = nextId++,\n" +
                    "                    author = \"Me\",\n" +
                    "                    likedByMe = false,\n" +
                    "                    published = getTime()\n" +
                    "                )\n" +
                    "            ) + posts\n" +
                    "        } else {\n" +
                    "            posts.map { if (it.id == post.id) it.copy(content = post.content) else it }\n" +
                    "        }\n" +
                    "        data.value = posts\n" +
                    "\n" +
                    "\n" +
                    "    }\n" +
                    "\n" +
                    "    override fun playMedia(id: Long) {\n" +
                    "        posts = posts.map {\n" +
                    "            if (it.id != id) it else it.copy(view = it.view + 1)\n" +
                    "        }\n" +
                    "        data.value = posts\n" +
                    "\n" +
                    "    }\n" +
                    "}\n",
            published = "23 сентября в 10:12",
            likedByMe = false,
            videoURL = "",

            ),
        Post(
            id = nextId++,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Нетология что это такое? (исповедь человека, прошедшего Нетологию)",
            published = "22 сентября в 10:14",
            likedByMe = false,
            videoURL = "https://yandex.ru/video/preview/15202559606961200008",

            ),
        Post(
            id = nextId++,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Таймбоксинг — отличный способ навести порядок в своём календаре и разобраться с делами, которые долго откладывали на потом. Его главный принцип — на каждое дело заранее выделяется определённый отрезок времени. В это время вы работаете только над одной задачей, не переключаясь на другие. Собрали советы, которые помогут внедрить таймбоксинг \uD83D\uDC47\uD83C\uDFFB",
            published = "22 сентября в 10:12",
            likedByMe = false,
            videoURL = "",

            ),
        Post(
            id = nextId++,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "24 сентября стартует новый поток бесплатного курса «Диджитал-старт: первый шаг к востребованной профессии» — за две недели вы попробуете себя в разных профессиях и определите, что подходит именно вам → http://netolo.gy/fQ",
            published = "21 сентября в 10:12",
            likedByMe = false,
            videoURL = "",

            ),
        Post(
            id = nextId++,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Диджитал давно стал частью нашей жизни: мы общаемся в социальных сетях и мессенджерах, заказываем еду, такси и оплачиваем счета через приложения.",
            published = "20 сентября в 10:14",
            likedByMe = false,
            videoURL = "",

            ),
        Post(
            id = nextId++,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Большая афиша мероприятий осени: конференции, выставки и хакатоны для жителей Москвы, Ульяновска и Новосибирска \uD83D\uDE09",
            published = "19 сентября в 14:12",
            likedByMe = false,
            videoURL = "",

            ),
        Post(
            id = nextId++,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Языков программирования много, и выбрать какой-то один бывает нелегко. Собрали подборку статей, которая поможет вам начать, если вы остановили свой выбор на JavaScript.",
            published = "19 сентября в 10:24",
            likedByMe = false,
            videoURL = "",


            ),
        Post(
            id = nextId++,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Знаний хватит на всех: на следующей неделе разбираемся с разработкой мобильных приложений, учимся рассказывать истории и составлять PR-стратегию прямо на бесплатных занятиях \uD83D\uDC47",
            published = "18 сентября в 10:12",
            likedByMe = false,
            videoURL = "",

            ),
        Post(
            id = nextId++,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
            published = "21 мая в 18:36",
            likedByMe = false,
            videoURL = "",

            )
    )


    private val data = MutableLiveData(posts)


    init {
        val file = context.filesDir.resolve(FILE_NAME)
        if (file.exists()) {
            context.openFileInput(FILE_NAME).bufferedReader().use {
                posts = gson.fromJson(it, typeToken)
            }
        } else {
            posts = defaultPosts
        }
        data.value = posts
        nextId = posts.maxOfOrNull { it.id }?.inc() ?: 1
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
                    author = faker.name().fullName(),
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
