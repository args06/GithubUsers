package com.example.githubusers.ui.detail

import androidx.lifecycle.ViewModel
import com.example.githubusers.data.UserRepository

class DetailViewModel(private val userRepository: UserRepository) : ViewModel() {

    fun getUserDetail(username: String) = userRepository.getUserDetail(username)

    fun getUserRepoStar(username: String, status: String) = userRepository.getUserRepoStar(username, status)
}