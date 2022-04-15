package com.mufti.github.user.room

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface GithubDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(github: Github)

    @Delete
    fun delete(github: Github)

    @Query("SELECT * from github ORDER BY id ASC")
    fun getAll(): LiveData<List<Github>>

    @Query("SELECT * from github WHERE username = :username")
    fun getByUsername(username: String): LiveData<Github>
}