package com.mufti.github.user.room

import android.app.Application
import androidx.lifecycle.LiveData
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class GithubRepository(application: Application) {
    private val mGithubDao: GithubDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = GithubRoomDatabase.getDatabase(application)
        mGithubDao = db.githubDao()
    }

    fun getAllGithub(): LiveData<List<Github>> = mGithubDao.getAll()

    fun getByUsername(username: String): LiveData<Github> = mGithubDao.getByUsername(username)

    fun insert(github: Github) {
        executorService.execute { mGithubDao.insert(github) }
    }

    fun delete(github: Github) {
        executorService.execute { mGithubDao.delete(github) }
    }
}