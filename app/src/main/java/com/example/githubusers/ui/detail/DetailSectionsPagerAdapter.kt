package com.example.githubusers.ui.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.githubusers.helper.Constant.ARG_REPO
import com.example.githubusers.helper.Constant.ARG_SECTION_NUMBER
import com.example.githubusers.helper.Constant.ARG_STAR
import com.example.githubusers.helper.Constant.ARG_USERNAME

class DetailSectionsPagerAdapter (
    fragment: Fragment, private val tabCount: Int
) : FragmentStateAdapter(fragment) {

    var username: String = ""

    override fun getItemCount(): Int = tabCount

    override fun createFragment(position: Int): Fragment {
        val fragment = RepoFragment()

        fragment.arguments = Bundle().apply {
            if (position == 0) {
                putString(ARG_SECTION_NUMBER, ARG_REPO)
            } else {
                putString(ARG_SECTION_NUMBER, ARG_STAR)
            }
            putString(ARG_USERNAME, username)
        }
        return fragment
    }
}