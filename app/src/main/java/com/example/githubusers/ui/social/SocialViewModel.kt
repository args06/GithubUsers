package com.example.githubusers.ui.social

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubusers.data.UserRepository
import com.example.githubusers.data.local.entity.FavoriteEntity
import kotlinx.coroutines.launch

class SocialViewModel(private val userRepository: UserRepository) : ViewModel() {

    fun getUserSocial(username: String, status: String) =
        userRepository.getUserSocial(username, status)

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
}