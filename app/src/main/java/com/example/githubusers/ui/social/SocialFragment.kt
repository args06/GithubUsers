package com.example.githubusers.ui.social

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.githubusers.databinding.FragmentSocialBinding
import com.example.githubusers.helper.Constant
import com.example.githubusers.ui.MainActivity
import com.google.android.material.tabs.TabLayoutMediator


class SocialFragment : Fragment() {

    private var _binding: FragmentSocialBinding? = null
    private val binding get() = _binding!!

    private lateinit var username: String
    private var selectedTab: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSocialBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        username = SocialFragmentArgs.fromBundle(arguments as Bundle).username
        selectedTab = SocialFragmentArgs.fromBundle(arguments as Bundle).selectedTab

        (activity as MainActivity).supportActionBar?.title = username

        setTabLayout()
    }

    private fun setTabLayout() {
        val detailSectionsPagerAdapter = SocialSectionsPagerAdapter(this, 2)
        detailSectionsPagerAdapter.username = username
        binding.vpSocial.apply {
            adapter = detailSectionsPagerAdapter
            currentItem = selectedTab
        }

        TabLayoutMediator(binding.tlSocial, binding.vpSocial) { tab, position ->
            tab.text = resources.getString(Constant.SOCIAL_TAB_TITLES[position])
        }.attach()
    }

}