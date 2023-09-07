package com.example.githubusers.data.remote.service

import com.example.githubusers.data.remote.response.RepoResponseItem
import com.example.githubusers.data.remote.response.UserDetailResponse
import com.example.githubusers.data.remote.response.UserResponseItem
import com.example.githubusers.data.remote.response.UserSearchResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface UserAPI {

    @GET("users")
    suspend fun getUserList(): ArrayList<UserResponseItem>

    @GET("search/users?")
    suspend fun getSearchList(
        @Query("q") name: String
    ): UserSearchResponse

    @GET("users/{uname}")
    suspend fun getUserDetail(
        @Path("uname") name: String
    ): UserDetailResponse

    @GET("users/{uname}/repos")
    suspend fun getUserRepos(
        @Path("uname") name: String,
        @Query("per_page") max: Int
    ): ArrayList<RepoResponseItem>

    @GET("users/{uname}/starred")
    suspend fun getUserStar(
        @Path("uname") name: String,
        @Query("per_page") max: Int
    ): ArrayList<RepoResponseItem>

    @GET("users/{uname}/followers")
    suspend fun getUserFollowers(
        @Path("uname") name: String,
        @Query("per_page") max: Int
    ): ArrayList<UserResponseItem>

    @GET("users/{uname}/following")
    suspend fun getUserFollowing(
        @Path("uname") name: String,
        @Query("per_page") max: Int
    ): ArrayList<UserResponseItem>
}