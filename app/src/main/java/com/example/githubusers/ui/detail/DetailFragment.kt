package com.example.githubusers.ui.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.example.githubusers.R
import com.example.githubusers.data.Result
import com.example.githubusers.data.remote.response.UserDetailResponse
import com.example.githubusers.databinding.FragmentDetailBinding
import com.example.githubusers.helper.Constant.DETAIL_TAB_TITLES
import com.example.githubusers.helper.Constant.ONE
import com.example.githubusers.helper.Constant.ZERO
import com.example.githubusers.ui.MainActivity
import com.example.githubusers.ui.ViewModelFactory
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class DetailFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var username: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        username = DetailFragmentArgs.fromBundle(arguments as Bundle).username

        (activity as MainActivity).supportActionBar?.title = username

        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity(), null)
        val viewModel: DetailViewModel by viewModels { factory }

        setTabLayout()
        getUserDetail(viewModel)

        with(binding) {
            llRepositories.setOnClickListener(this@DetailFragment)
            llFollowers.setOnClickListener(this@DetailFragment)
            llFollowing.setOnClickListener(this@DetailFragment)
        }
    }

    private fun getUserDetail(viewModel: DetailViewModel) {
        viewModel.getUserDetail(username).observe(this) { users ->
            if (users != null) {
                when (users) {
                    is Result.Loading -> {
                        showLoading(true)
                    }
                    is Result.Success -> {
                        showLoading(false)
                        val userDetail = users.data
                        setUserDetail(userDetail)
                    }
                    is Result.Error -> {
                        showLoading(false)
                        Toast.makeText(
                            requireActivity(),
                            "Terjadi kesalahan" + users.error,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun setUserDetail(userDetail: UserDetailResponse) {
        with(binding) {
            Glide.with(requireActivity()).load(userDetail.avatarUrl).into(civProfile)
            tvFullName.text = userDetail.name
            tvUsername.text = userDetail.login
            if (userDetail.bio != null) tvBio.text = userDetail.bio.toString()
            else tvBio.visibility = View.GONE
            tvRepositories.text = userDetail.publicRepos.toString()
            tvFollowers.text = userDetail.followers.toString()
            tvFollowing.text = userDetail.following.toString()
        }
    }

    private fun setTabLayout() {
        val detailSectionsPagerAdapter = DetailSectionsPagerAdapter(this, 2)
        detailSectionsPagerAdapter.username = username
        binding.vpRepoStar.adapter = detailSectionsPagerAdapter

        TabLayoutMediator(binding.tlRepoStar, binding.vpRepoStar) { tab, position ->
            tab.text = resources.getString(DETAIL_TAB_TITLES[position])
        }.attach()
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            with(binding) {
                progressBar.visibility = View.VISIBLE
                tvRepositoriesTitle.visibility = View.INVISIBLE
                tvFollowersTitle.visibility = View.INVISIBLE
                tvFollowingTitle.visibility = View.INVISIBLE
                tlRepoStar.visibility = View.INVISIBLE
            }

        } else {
            with(binding) {
                progressBar.visibility = View.GONE
                tvRepositoriesTitle.visibility = View.VISIBLE
                tvFollowersTitle.visibility = View.VISIBLE
                tvFollowingTitle.visibility = View.VISIBLE
                tlRepoStar.visibility = View.VISIBLE
            }
        }
    }

    override fun onClick(v: View?) {
        val toSocialFragment = DetailFragmentDirections.actionNavigationDetailToNavigationSocial()
        toSocialFragment.username = username
        when (v?.id) {
            R.id.ll_repositories -> {
                lifecycleScope.launch(Dispatchers.Default) {
                    withContext(Dispatchers.Main) {
                        binding.ablAppbar.setExpanded(false, true)
                    }
                }
            }

            R.id.ll_followers -> {
                toSocialFragment.selectedTab = ZERO
                view?.findNavController()?.navigate(toSocialFragment)
            }

            R.id.ll_following -> {
                toSocialFragment.selectedTab = ONE
                view?.findNavController()?.navigate(toSocialFragment)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}