package com.example.githubusers.di

import android.content.Context
import com.example.githubusers.data.UserRepository
import com.example.githubusers.data.local.room.UserDatabase
import com.example.githubusers.data.remote.service.UserServiceAPI

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val apiService = UserServiceAPI.getApiService()
        val database = UserDatabase.getInstance(context)
        val dao = database.userDao()
        return UserRepository.getInstance(apiService, dao)
    }
}