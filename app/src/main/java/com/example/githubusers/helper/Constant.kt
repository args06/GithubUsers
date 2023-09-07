package com.example.githubusers.helper

import androidx.annotation.StringRes
import com.example.githubusers.R

object Constant {

    const val ARG_REPO: String = "repository"
    const val ARG_STAR: String = "star"
    const val ARG_FOLLOWING: String = "following"
    const val ARG_FOLLOWERS: String = "followers"
    const val ARG_SECTION_NUMBER = "section_number"
    const val ARG_USERNAME = "username"

    const val ZERO: Int = 0
    const val ONE: Int = 1
    const val ONE_HUNDRED: Int = 100

    @StringRes
    val SOCIAL_TAB_TITLES = intArrayOf(
        R.string.followers_title,
        R.string.following_title,
    )

    @StringRes
    val DETAIL_TAB_TITLES = intArrayOf(
        R.string.repositories_title,
        R.string.star_title,
    )
}