package com.mufti.github.user.data

import com.google.gson.annotations.SerializedName

data class Detail(

    @field:SerializedName("login")
    val username: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("company")
    val company: String? = null,

    @field:SerializedName("location")
    val location: String? = null,

    @field:SerializedName("public_repos")
    val repository: Int? = null,

    @field:SerializedName("followers")
    val followers: Int? = null,

    @field:SerializedName("following")
    val following: Int? = null,

    @field:SerializedName("avatar_url")
    val avatar: String? = null,
)
