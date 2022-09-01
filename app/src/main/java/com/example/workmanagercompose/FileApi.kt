package com.example.workmanagercompose

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET

interface FileApi {
    @GET("/wQQKvti.jpg")
    suspend fun downloadImage(): Response<ResponseBody>

    companion object {
        val instance by lazy {
            Retrofit.Builder().baseUrl("https://i.imgur.com")
                .build()
                .create(FileApi::class.java)
        }
    }
}