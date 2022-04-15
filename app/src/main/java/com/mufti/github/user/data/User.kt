package com.mufti.github.user.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(

    @field:SerializedName("login")
    val username: String? = null,

    @field:SerializedName("type")
    val type: String? = null,

    @field:SerializedName("avatar_url")
    val avatar: String? = null,
) : Parcelable
