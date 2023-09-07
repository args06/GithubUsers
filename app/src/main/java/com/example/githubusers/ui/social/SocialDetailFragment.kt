package com.example.githubusers.ui.social

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubusers.R
import com.example.githubusers.data.Result
import com.example.githubusers.data.local.entity.FavoriteEntity
import com.example.githubusers.data.remote.response.UserResponseItem
import com.example.githubusers.databinding.FragmentSocialDetailBinding
import com.example.githubusers.helper.Constant
import com.example.githubusers.helper.OnSetFavorite
import com.example.githubusers.ui.ViewModelFactory

class SocialDetailFragment : Fragment() {

    private var _binding: FragmentSocialDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var username: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSocialDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        username = arguments?.getString(Constant.ARG_USERNAME).toString()
        setTabData()
    }

    override fun onResume() {
        super.onResume()
        setTabData()
    }

    private fun setTabData() {
        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity(), null)
        val viewModel: SocialViewModel by viewModels { factory }

        val socialAdapter = SocialAdapter()
        socialAdapter.onFavoriteClickListener = object : OnSetFavorite {
            override fun onFavoriteClick(data: FavoriteEntity, selectedView: ImageButton) {
                viewModel.isUserFavorite(data).observe(viewLifecycleOwner) { isFavorite ->
                    when (isFavorite) {
                        is Result.Loading -> {
                            selectedView.setImageDrawable(
                                ContextCompat.getDrawable(
                                    selectedView.context, R.drawable.circular_progress_bar
                                )
                            )
                        }
                        is Result.Success -> {
                            binding.lpiProgressBar.visibility = View.GONE
                            if (isFavorite.data) {
                                viewModel.removeFavoriteUser(data)
                                selectedView.setImageDrawable(
                                    ContextCompat.getDrawable(
                                        selectedView.context, R.drawable.baseline_favorite_border_24
                                    )
                                )
                            } else {
                                viewModel.addFavoriteUser(data)
                                selectedView.setImageDrawable(
                                    ContextCompat.getDrawable(
                                        selectedView.context, R.drawable.baseline_favorite_24
                                    )
                                )
                            }
                        }
                        is Result.Error -> {
                            Toast.makeText(
                                requireActivity(),
                                "Terjadi kesalahan" + isFavorite.error,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }

            override fun onFavoriteDrawable(data: FavoriteEntity, selectedView: ImageButton) {
                viewModel.isUserFavorite(data).observe(viewLifecycleOwner) { isFavorite ->
                    when (isFavorite) {
                        is Result.Success -> {
                            if (isFavorite.data) {
                                selectedView.setImageDrawable(
                                    ContextCompat.getDrawable(
                                        selectedView.context, R.drawable.baseline_favorite_24
                                    )
                                )
                            } else {
                                selectedView.setImageDrawable(
                                    ContextCompat.getDrawable(
                                        selectedView.context, R.drawable.baseline_favorite_border_24
                                    )
                                )
                            }
                        }
                        else -> {}
                    }
                }
            }
        }

        when (arguments?.getInt(Constant.ARG_SECTION_NUMBER, 1)) {
            1 -> viewModel.getUserSocial(username, Constant.ARG_FOLLOWERS)
                .observe(viewLifecycleOwner) { setUserSocial(it, socialAdapter) }
            2 -> viewModel.getUserSocial(username, Constant.ARG_FOLLOWING)
                .observe(viewLifecycleOwner) { setUserSocial(it, socialAdapter) }
        }
    }

    private fun setUserSocial(
        userSocial: Result<ArrayList<UserResponseItem>>,
        socialAdapter: SocialAdapter
    ) {
        when (userSocial) {
            is Result.Loading -> {
                showLoading(true)
            }
            is Result.Success -> {
                showLoading(false)
                val userSocialData = userSocial.data

                socialAdapter.submitList(userSocialData)

                binding.apply {
                    rvSocial.apply {
                        layoutManager = LinearLayoutManager(context)
                        setHasFixedSize(true)
                        adapter = socialAdapter
                        visibility = if (userSocialData.isEmpty()) View.GONE else View.VISIBLE
                    }

                    tvNoData.visibility = if (userSocialData.isEmpty()) View.VISIBLE else View.GONE
                }

            }
            is Result.Error -> {
                showLoading(false)
                Toast.makeText(
                    requireActivity(), "Terjadi kesalahan" + userSocial.error, Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            with(binding) {
                lpiProgressBar.visibility = View.VISIBLE
                rvSocial.visibility = View.INVISIBLE
                tvNoData.visibility = View.GONE
            }

        } else {
            with(binding) {
                lpiProgressBar.visibility = View.GONE
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}