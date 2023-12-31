package com.best.myappstory.myApi

import com.best.myappstory.myResponse.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface Apiservice {
    @POST("register")
    fun register(
        @Body regCredential : RegisterRequest
    ): Call<RegisterResponse>

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @GET("stories")
    fun getAllStories(
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null,
    ): Call<StoriesResponse>

    @Multipart
    @POST("stories")
    fun addStory(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): Call<AddResponse>
}