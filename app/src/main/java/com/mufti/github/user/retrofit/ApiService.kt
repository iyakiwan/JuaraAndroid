package com.mufti.github.user.retrofit

import com.mufti.github.user.data.Detail
import com.mufti.github.user.data.Search
import com.mufti.github.user.data.User
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("search/users")
    fun getSearchUser(
        @Query("q") query: String
    ): Call<Search>

    @GET("users/{username}")
    fun getDetailUser(
        @Path("username") username: String
    ): Call<Detail>

    @GET("users/{username}/followers")
    fun getFollowersUser(
        @Path("username") username: String
    ): Call<List<User>>

    @GET("users/{username}/following")
    fun getFollowingUser(
        @Path("username") username: String
    ): Call<List<User>>
}