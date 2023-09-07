package com.example.githubusers.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.githubusers.data.local.entity.FavoriteEntity

@Dao
interface UserDao {

    @Query("SELECT * FROM user")
    fun getUsers(): LiveData<List<FavoriteEntity>>

    @Query("SELECT * FROM user WHERE username LIKE '%' || :username || '%'")
    fun getSearchedUser(username: String): LiveData<List<FavoriteEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUser(user: FavoriteEntity)

    @Delete
    suspend fun deleteUser(user: FavoriteEntity)

    @Query("SELECT EXISTS(SELECT * FROM user WHERE username = :username)")
    suspend fun isUserBookmarked(username: String): Boolean
}