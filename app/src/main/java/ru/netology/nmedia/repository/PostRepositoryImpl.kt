package ru.netology.nmedia.repository


import androidx.lifecycle.asLiveData
import kotlinx.coroutines.flow.combine
import okio.IOException
import ru.netology.nmedia.api.ApiService
import ru.netology.nmedia.dao.DraftDao
import ru.netology.nmedia.dao.PostDao
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.entity.DraftEntity
import ru.netology.nmedia.entity.PostEntity
import ru.netology.nmedia.error.ApiError
import ru.netology.nmedia.error.NetworkError
import ru.netology.nmedia.error.UnknownError


class PostRepositoryImpl(
    private val postDao: PostDao,
    private val draftDao: DraftDao
) : PostRepository {
    override val data = draftDao.getAll()
        .combine(postDao.getAll()) { drafts, posts ->
            drafts.map { it.toDto() } + posts.map { it.toDto() }
        }
        .asLiveData()

    override suspend fun getAll() {
        val response = ApiService.service.getAll()
        if (!response.isSuccessful) {
            throw RuntimeException(response.message())
        }
        val posts = response.body() ?: throw RuntimeException("Тело ответа пустоe")
        postDao.insert(posts.map(PostEntity::fromDto))

    }

    override suspend fun likeById(id: Long) {

        val entity = postDao.likeById(id)
        val updated = entity.copy(
            likedByMe = !entity.likedByMe,
            likes = if (entity.likedByMe) entity.likes - 1 else entity.likes + 1
        )
        postDao.insert(updated)
        try {
            val response = if (entity.likedByMe) {
                ApiService.service.dislikeById(id)
            } else {
                ApiService.service.likeById(id)
            }
            if (!response.isSuccessful) {
                throw RuntimeException(response.message())
            }
            val body = response.body() ?: throw RuntimeException(response.message())
            postDao.insert(PostEntity.fromDto(body))
        } catch (e: IOException) {
            postDao.insert(entity)
            throw NetworkError
        } catch (e: Exception) {
            postDao.insert(entity)
            throw UnknownError
        }
    }


    override suspend fun shareById(id: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun removeById(id: Long) {
        postDao.removeById(id)
        try {
            val response = ApiService.service.removeById(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun save(post: Post) {
        try {
            val id = draftDao.insert(DraftEntity(content = post.content))
            val response = ApiService.service.savePost(post)
            if (!response.isSuccessful) {
                throw RuntimeException(response.message())
            }
            val body = response.body() ?: throw RuntimeException("Тело ответа пустоe")
            draftDao.removeById(id)
            postDao.insert(PostEntity.fromDto(body))
        } catch (e: Exception) {
            e.printStackTrace()
            post
        }
    }
}
//    private val client = OkHttpClient.Builder()
//        .callTimeout(30, TimeUnit.SECONDS)
//        .addInterceptor(HttpLoggingInterceptor().apply {
//            level = HttpLoggingInterceptor.Level.BODY
//        })
//        .build()

//        override fun getAll() = dao.getAll().map { list ->
//        list.map {
//            it.toDto()
//        }
//    }

//    private val gson = Gson()
//    private val type = object : TypeToken<List<Post>>() {}.type
//
//    companion object {
//        private const val BASE_URL = "http://10.0.2.2:9999/"
//        private val jsonType = "application/json".toMediaType()
//    }

//    override fun getAll(): List<Post> {
//        return ApiService.service.getAll()
//            .execute()
//            .let {
//                it.body() ?: throw RuntimeException("body is null")
//            }
//    }


//    override fun getAllAsync(callback: PostRepository.NMediaCallback<List<Post>>) {
//        ApiService.service
//            .getAll()
//            .enqueue(object : Callback<List<Post>> {
//                override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
//                    try {
//                        if (!response.isSuccessful) {
//                            callback.onError(RuntimeException(context.getString(R.string.failed_refresh_news)))
//                            return
//                        }
//                        val body: List<Post> = response.body() ?: throw RuntimeException(
//                            context.getString(R.string.body_is_null)
//                        )
//                        callback.onSuccess(body)
//                    } catch (e: Exception) {
//                        callback.onError(e)
//                    }
//                }
//
//                override fun onFailure(call: Call<List<Post>>, t: Throwable) {
//                    callback.onError(Exception(t))
//                }
//
//            })
//
//
//    }
//
//    override fun likeById(id: Long, callback: PostRepository.NMediaCallback<Post>) {
//        ApiService.service.likeById(id)
//            .enqueue(object : Callback<Post> {
//                override fun onResponse(call: Call<Post>, response: Response<Post>) {
//                    if (!response.isSuccessful) {
//                        callback.onError(RuntimeException(response.message()))
//                    } else {
//                        callback.onSuccess(
//                            response.body() ?: throw RuntimeException("body is null")
//                        )
//                    }
//                }
//
//                override fun onFailure(call: Call<Post>, t: Throwable) {
//                    callback.onError(Exception(t))
//                }
//            })
//        dao.likeById(id)
//        val request = if (post.likedByMe) {
//            Request.Builder()
//                .delete()
//                .url("${BASE_URL}api/posts/${post.id}/likes")
//                .build()
//
//        } else {
//            Request.Builder()
//                .url("${BASE_URL}api/posts/${post.id}/likes")
//                .post(gson.toJson(post, Post::class.java).toRequestBody(jsonType))
//                .build()
//        }
//        val call = client.newCall(request)
//        val response = call.execute()
//
//        val bodyText = requireNotNull(response.body).string()
//
//        return gson.fromJson(bodyText, Post::class.java)

//    }

//    override fun dislikeById(id: Long, callback: PostRepository.NMediaCallback<Post>) {
//        ApiService.service.dislikeById(id)
//            .enqueue(object : Callback<Post> {
//                override fun onResponse(call: Call<Post>, response: Response<Post>) {
//                    if (!response.isSuccessful) {
//                        callback.onError(RuntimeException(response.message()))
//                    } else {
//                        callback.onSuccess(
//                            response.body() ?: throw RuntimeException("body is null")
//                        )
//                    }
//                }
//
//                override fun onFailure(call: Call<Post>, t: Throwable) {
//                    callback.onError(Exception(t))
//                }
//            })
//    }
//
//    override fun shareById(id: Long) {
////        dao.sharedById(id)
//
//    }
//
//    override fun save(post: Post, callback: PostRepository.NMediaCallback<Post>) {
//        ApiService.service.savePost(post)
//            .enqueue(object : Callback<Post> {
//                override fun onResponse(call: Call<Post>, response: Response<Post>) {
//                    val post = response.body() ?: throw RuntimeException("Invalid")
//                    callback.onSuccess(post)
//                }
//
//                override fun onFailure(call: Call<Post>, t: Throwable) {
//                    callback.onError(Exception(t))
//                }
//
//            })
//        dao.save(PostEntity.fromDto(post))
//        val request = Request.Builder()
//            .url("${BASE_URL}api/slow/posts")
//            .post(gson.toJson(post).toRequestBody(jsonType))
//            .build()
//        client.newCall(request)
//            .enqueue(
//                object : Callback {
//                    override fun onFailure(call: Call, e: IOException) {
//                        NMediaCallback.onError(e)
//                    }
//
//                    override fun onResponse(call: Call, response: Response) {
//                        val responseBody = response.body?.string()
//                        try {
//                            NMediaCallback.onSuccess(gson.fromJson(responseBody, Post::class.java))
//                        } catch (e: Exception) {
//                            NMediaCallback.onError(e)
//                        }
//                    }
//                }
//            )
//    }
//
//    override fun playMedia(id: Long) {
//        TODO("Not yet implemented")
//    }
//
//    override fun removeById(id: Long, callback: PostRepository.NMediaCallback<Unit>) {
//        ApiService.service.removeById(id)
//            .enqueue(object : Callback<Unit> {
//                override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
//                    if (!response.isSuccessful) {
//                        callback.onError(RuntimeException(context.getString(R.string.faile_delete_post)))
//                        return
//                    } else {
//                        callback.onSuccess(Unit)
//                    }
//                }
//
//                override fun onFailure(call: Call<Unit>, t: Throwable) {
//                    callback.onError(Exception(t))
//                }
//            })
//        dao.removeById(id)
//        val request = Request.Builder()
//            .delete()
//            .url("${BASE_URL}api/slow/posts/$id")
//            .build()
//
//        client.newCall(request)
//            .enqueue(
//                object : Callback {
//                    override fun onFailure(call: Call, e: IOException) {
//                        NMediaCallback.onError(e)
//                    }
//
//                    override fun onResponse(call: Call, response: Response) {
//                        val responseBody = response.body?.string()
//                        try {
//                            NMediaCallback.onSuccess(gson.fromJson(responseBody, type))
//                        } catch (e: Exception) {
//                            NMediaCallback.onError(e)
//                        }
//                    }
//                }
//            )
//    }
//}
