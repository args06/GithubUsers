package com.example.githubusers.ui.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubusers.data.remote.response.RepoResponseItem
import com.example.githubusers.databinding.FragmentRepoBinding
import com.example.githubusers.helper.Constant.ARG_REPO
import com.example.githubusers.helper.Constant.ARG_SECTION_NUMBER
import com.example.githubusers.helper.Constant.ARG_STAR
import com.example.githubusers.helper.Constant.ARG_USERNAME
import com.example.githubusers.ui.ViewModelFactory
import com.example.githubusers.data.Result

class RepoFragment : Fragment() {

    private var _binding: FragmentRepoBinding? = null
    private val binding get() = _binding!!

    private lateinit var username: String
    private var tabName: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRepoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        username = arguments?.getString(ARG_USERNAME).toString()
        setTabData()
    }

    private fun setTabData() {
        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity(), null)
        val viewModel: DetailViewModel by viewModels { factory }

        tabName = arguments?.getString(ARG_SECTION_NUMBER)
        when (tabName) {
            ARG_REPO -> viewModel.getUserRepoStar(username, ARG_REPO).observe(viewLifecycleOwner) {setUserRepoStar(it)}
            ARG_STAR -> viewModel.getUserRepoStar(username, ARG_STAR).observe(viewLifecycleOwner) {setUserRepoStar(it)}
        }
    }

    private fun setUserRepoStar(
        userRepoStar: Result<ArrayList<RepoResponseItem>>,
    ) {
        when (userRepoStar) {
            is Result.Loading -> {
                showLoading(true)
            }
            is Result.Success -> {
                showLoading(false)
                val userRepoStarData = userRepoStar.data
                val repoStarAdapter = RepoStarAdapter()
                repoStarAdapter.submitList(userRepoStarData)

                binding.apply {
                    rvRepoStar.apply {
                        layoutManager = LinearLayoutManager(context)
                        setHasFixedSize(true)
                        adapter = repoStarAdapter
                        visibility = if (userRepoStarData.isEmpty()) View.GONE else View.VISIBLE
                    }

                    tvNoData.visibility = if (userRepoStarData.isEmpty()) View.VISIBLE else View.GONE
                }

            }
            is Result.Error -> {
                showLoading(false)
                Toast.makeText(
                    requireActivity(),
                    "Terjadi kesalahan" + userRepoStar.error,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            with(binding) {
                lpiProgressBar.visibility = View.VISIBLE
                rvRepoStar.visibility = View.INVISIBLE
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