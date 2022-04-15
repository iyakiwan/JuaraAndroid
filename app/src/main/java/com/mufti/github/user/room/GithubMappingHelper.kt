package com.mufti.github.user.room

import com.mufti.github.user.data.User

object GithubMappingHelper {
    fun mapCursorToListUser(listGithub: List<Github>): ArrayList<User> {
        val userList = ArrayList<User>()
        listGithub.map { github ->
            userList.add(User(github.username, github.type, github.avatar))
        }
        return userList
    }
}