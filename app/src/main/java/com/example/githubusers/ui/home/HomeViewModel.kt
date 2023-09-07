package com.example.githubusers.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.githubusers.data.UserRepository
import com.example.githubusers.data.local.entity.FavoriteEntity
import com.example.githubusers.helper.SettingPreferences
import kotlinx.coroutines.launch

class HomeViewModel(private val userRepository: UserRepository, private val pref: SettingPreferences) : ViewModel() {

    fun getUserData(searchQuery: String = "") = userRepository.getAllUser(searchQuery)

    fun getFavoriteData(searchQuery: String = "") = userRepository.getFavoriteUser(searchQuery)

    fun isUserFavorite(favoriteUser: FavoriteEntity) = userRepository.isUserFavorite(favoriteUser)

    fun addFavoriteUser(favoriteUser: FavoriteEntity) {
        viewModelScope.launch {
            userRepository.setFavoriteUser(favoriteUser, false)
        }
    }

    fun removeFavoriteUser(favoriteUser: FavoriteEntity) {
        viewModelScope.launch {
            userRepository.setFavoriteUser(favoriteUser, true)
        }
    }

    fun getThemeSettings(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            pref.saveThemeSetting(isDarkModeActive)
        }
    }
}