package com.mufti.github.user.data

import com.google.gson.annotations.SerializedName

data class Search(

	@field:SerializedName("total_count")
	val totalCount: Int,

	@field:SerializedName("items")
	val items: List<User>
)
