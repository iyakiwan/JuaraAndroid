package com.mufti.github.user.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mufti.github.user.setting.SettingPreferences
import com.mufti.github.user.viewmodel.SettingViewModel

class ViewModelFactory (private val pref: SettingPreferences) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingViewModel::class.java)) {
            return SettingViewModel(pref) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}