package com.mufti.github.user.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.mufti.github.user.room.Github
import com.mufti.github.user.room.GithubRepository

class FavoriteViewModel(application: Application) : ViewModel() {

    private val mGithubRepository: GithubRepository = GithubRepository(application)

    fun getAllGithub(): LiveData<List<Github>> = mGithubRepository.getAllGithub()
}