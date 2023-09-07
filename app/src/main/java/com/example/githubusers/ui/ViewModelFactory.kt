package com.example.githubusers.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.githubusers.data.UserRepository
import com.example.githubusers.di.Injection
import com.example.githubusers.helper.SettingPreferences
import com.example.githubusers.ui.detail.DetailViewModel
import com.example.githubusers.ui.home.HomeViewModel
import com.example.githubusers.ui.social.SocialViewModel

class ViewModelFactory private constructor(
    private val userRepository: UserRepository, private val pref: SettingPreferences?
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = when (modelClass) {
        HomeViewModel::class.java -> pref?.let { HomeViewModel(userRepository, it) } as T
        DetailViewModel::class.java -> DetailViewModel(userRepository) as T
        SocialViewModel::class.java -> SocialViewModel(userRepository) as T
        else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context, pref: SettingPreferences?): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(context), pref)
            }.also { instance = it }
    }
}