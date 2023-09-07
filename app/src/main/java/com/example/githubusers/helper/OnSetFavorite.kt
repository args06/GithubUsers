package com.example.githubusers.helper

import android.widget.ImageButton
import com.example.githubusers.data.local.entity.FavoriteEntity

interface OnSetFavorite {
    fun onFavoriteClick(data: FavoriteEntity, selectedView: ImageButton)
    fun onFavoriteDrawable(data: FavoriteEntity, selectedView: ImageButton)
}