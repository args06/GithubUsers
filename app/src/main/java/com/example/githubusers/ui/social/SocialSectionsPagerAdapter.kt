package com.example.githubusers.ui.social

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.githubusers.helper.Constant

class SocialSectionsPagerAdapter(
    fragment: Fragment, private val tabCount: Int
) : FragmentStateAdapter(fragment) {

    var username: String = ""

    override fun getItemCount(): Int = tabCount

    override fun createFragment(position: Int): Fragment {
        val fragment = SocialDetailFragment()
        fragment.arguments = Bundle().apply {
            putInt(Constant.ARG_SECTION_NUMBER, position + 1)
            putString(Constant.ARG_USERNAME, username)
        }
        return fragment
    }
}