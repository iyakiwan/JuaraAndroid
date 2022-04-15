package com.mufti.github.user.util

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mufti.github.user.viewmodel.DetailViewModel
import com.mufti.github.user.viewmodel.FavoriteViewModel

class ViewModelFactoryRoom private constructor(private val mApplication: Application) : ViewModelProvider.NewInstanceFactory() {

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactoryRoom? = null
        @JvmStatic
        fun getInstance(application: Application): ViewModelFactoryRoom {
            if (INSTANCE == null) {
                synchronized(ViewModelFactoryRoom::class.java) {
                    INSTANCE = ViewModelFactoryRoom(application)
                }
            }
            return INSTANCE as ViewModelFactoryRoom
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) {
            return FavoriteViewModel(mApplication) as T
        } else if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(mApplication) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}