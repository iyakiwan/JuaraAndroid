package com.mufti.github.user.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mufti.github.user.data.Detail
import com.mufti.github.user.retrofit.ApiConfig
import com.mufti.github.user.room.Github
import com.mufti.github.user.room.GithubRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(application: Application) : ViewModel() {

    private val mGithubRepository: GithubRepository = GithubRepository(application)

    private val _detailUser = MutableLiveData<Detail>()
    val detailUser: LiveData<Detail> = _detailUser

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getDetailUser(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetailUser(username)
        client.enqueue(object : Callback<Detail> {
            override fun onResponse(call: Call<Detail>, response: Response<Detail>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _detailUser.postValue(response.body())
                    _errorMessage.value = ""
                } else {
                    _errorMessage.value = response.message()
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<Detail>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = t.message.toString()
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun insertToRoom(github: Github) {
        mGithubRepository.insert(github)
    }

    fun deleteFromRoom(github: Github) {
        mGithubRepository.delete(github)
    }

    fun getGithubByUsername(username: String): LiveData<Github> = mGithubRepository.getByUsername(username)

    companion object {
        private const val TAG = "DetailViewModel"
    }
}