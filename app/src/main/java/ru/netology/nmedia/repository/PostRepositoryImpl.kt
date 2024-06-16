package ru.netology.nmedia.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import ru.netology.nmedia.dto.Post
import java.io.IOException
import java.util.concurrent.TimeUnit


class PostRepositoryImpl(
    // private val dao: PostDao,
) : PostRepository {
    private val client = OkHttpClient.Builder()
        .callTimeout(30, TimeUnit.SECONDS)
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()

    //    override fun getAll() = dao.getAll().map { list ->
//        list.map {
//            it.toDto()
//        }
//    }

    private val gson = Gson()
    private val type = object : TypeToken<List<Post>>() {}.type

    companion object {
        private const val BASE_URL = "http://10.0.2.2:9999/"
        private val jsonType = "application/json".toMediaType()
    }

//    override fun getAll(): List<Post> {
//        val request = Request.Builder()
//            .url("${BASE_URL}api/slow/posts")
//            .build()
//        val response = client.newCall(request)
//            .execute()
//        val responseText = response.body?.string() ?: error("Response body is null")
//        return gson.fromJson(responseText, type)
//    }

    override fun getAllAsync(callback: PostRepository.Callback<List<Post>>) {
        val request = Request.Builder()
            .url("${BASE_URL}api/slow/posts")
            .build()
        client.newCall(request)
            .enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    try {
                        callback.onSuccess(
                            gson.fromJson(response.body?.string(), type)
                        )
                    } catch (e: Exception) {
                        callback.onError(e)
                    }
                }
            })


    }

    override fun likeById(post: Post): Post {
//        dao.likeById(id)
        val request = if (post.likedByMe) {
            Request.Builder()
                .delete()
                .url("${BASE_URL}api/posts/${post.id}/likes")
                .build()

        } else {
            Request.Builder()
                .url("${BASE_URL}api/posts/${post.id}/likes")
                .post(gson.toJson(post, Post::class.java).toRequestBody(jsonType))
                .build()
        }
        val call = client.newCall(request)
        val response = call.execute()

        val bodyText = requireNotNull(response.body).string()

        return gson.fromJson(bodyText, Post::class.java)

    }

    override fun shareById(id: Long) {
//        dao.sharedById(id)
    }

    override fun save(post: Post, callback: PostRepository.Callback<Post>){
//        dao.save(PostEntity.fromDto(post))
        val request = Request.Builder()
            .url("${BASE_URL}api/slow/posts")
            .post(gson.toJson(post).toRequestBody(jsonType))
            .build()
        client.newCall(request)
            .enqueue(
                object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        callback.onError(e)
                    }

                    override fun onResponse(call: Call, response: Response) {
                        val responseBody = response.body?.string()
                        try {
                            callback.onSuccess(gson.fromJson(responseBody, Post::class.java))
                        } catch (e: Exception) {
                            callback.onError(e)
                        }
                    }
                }
            )

    }

    override fun playMedia(id: Long) {
        TODO("Not yet implemented")
    }

    override fun removeById(id: Long, callback: PostRepository.Callback<Post>) {
//        dao.removeById(id)
        val request = Request.Builder()
            .delete()
            .url("${BASE_URL}api/slow/posts/$id")
            .build()

        client.newCall(request)
            .enqueue(
                object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        callback.onError(e)
                    }

                    override fun onResponse(call: Call, response: Response) {
                        val responseBody = response.body?.string()
                        try {
                            callback.onSuccess(gson.fromJson(responseBody, type))
                        } catch (e: Exception) {
                            callback.onError(e)
                        }
                    }
                }
            )
    }
}
