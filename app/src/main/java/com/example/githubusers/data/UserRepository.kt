package com.example.githubusers.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.githubusers.data.local.entity.FavoriteEntity
import com.example.githubusers.data.local.room.UserDao
import com.example.githubusers.data.remote.response.RepoResponseItem
import com.example.githubusers.data.remote.response.UserDetailResponse
import com.example.githubusers.data.remote.response.UserResponseItem
import com.example.githubusers.data.remote.service.UserAPI
import com.example.githubusers.helper.Constant
import com.example.githubusers.helper.Constant.ONE_HUNDRED

class UserRepository private constructor(
    private val apiService: UserAPI,
    private val userDao: UserDao,
) {

    fun getAllUser(searchQuery: String): LiveData<Result<ArrayList<UserResponseItem>>> = liveData {
        emit(Result.Loading)
        try {
            if (searchQuery.isEmpty()) {
                val response = apiService.getUserList()
                emit(Result.Success(response))
            } else {
                val response = apiService.getSearchList(searchQuery).items as ArrayList
                emit(Result.Success(response))
            }

        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getUserDetail(username: String): LiveData<Result<UserDetailResponse>> = liveData {
        emit(Result.Loading)
        try {
            if (username.isNotEmpty()) {
                val response = apiService.getUserDetail(username)
                emit(Result.Success(response))
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getUserRepoStar(username: String, status: String): LiveData<Result<ArrayList<RepoResponseItem>>> = liveData {
        emit(Result.Loading)
        try {
            if (username.isNotEmpty()) {
                val response = if (status == Constant.ARG_REPO)
                     apiService.getUserRepos(username, ONE_HUNDRED)
                else
                    apiService.getUserStar(username, ONE_HUNDRED)
                emit(Result.Success(response))
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getUserSocial(username: String, status: String): LiveData<Result<ArrayList<UserResponseItem>>> = liveData {
        emit(Result.Loading)
        try {
            if (username.isNotEmpty()) {
                val response = if (status == Constant.ARG_FOLLOWERS)
                    apiService.getUserFollowers(username, ONE_HUNDRED)
                else
                    apiService.getUserFollowing(username, ONE_HUNDRED)
                emit(Result.Success(response))
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getFavoriteUser(searchQuery: String): LiveData<List<FavoriteEntity>>  {
        return if (searchQuery.isEmpty()) {
            val response = userDao.getUsers()
            response
        } else {
            val response = userDao.getSearchedUser(searchQuery)
            response
        }
    }

    fun isUserFavorite(favoriteUser: FavoriteEntity): LiveData<Result<Boolean>> = liveData {
        emit(Result.Loading)
        try {
            val isUserFavorite = userDao.isUserBookmarked(favoriteUser.username)
            emit(Result.Success(isUserFavorite))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    suspend fun setFavoriteUser(favoriteUser: FavoriteEntity, favoriteState: Boolean) {
        if (favoriteState)
            userDao.deleteUser(favoriteUser)
        else
            userDao.insertUser(favoriteUser)
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            apiService: UserAPI, userDao: UserDao
        ): UserRepository = instance ?: synchronized(this) {
            instance ?: UserRepository(apiService, userDao)
        }.also { instance = it }
    }
}