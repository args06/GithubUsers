package com.example.githubusers.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class FavoriteEntity(
    @field:ColumnInfo(name = "username")
    @field:PrimaryKey()
    val username: String,

    @field:ColumnInfo(name = "avatarUrl")
    val avatarUrl: String? = null,
)