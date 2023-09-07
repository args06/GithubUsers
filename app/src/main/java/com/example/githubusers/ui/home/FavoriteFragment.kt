package com.example.githubusers.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubusers.R
import com.example.githubusers.data.local.entity.FavoriteEntity
import com.example.githubusers.databinding.FragmentFavoriteBinding
import com.example.githubusers.helper.OnSetFavorite
import com.example.githubusers.ui.ViewModelFactory
import com.example.githubusers.data.Result

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity(), null)
        val viewModel: HomeViewModel by viewModels { factory }

        binding.svSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                getUserData(viewModel, query)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.isBlank()) getUserData(viewModel)
                return false
            }
        })

        getUserData(viewModel)
    }

    private fun getUserData(viewModel: HomeViewModel, searchQuery: String = "") {
        val favoriteAdapter = FavoriteAdapter()
        favoriteAdapter.onFavoriteClickListener = object : OnSetFavorite {
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
                            binding.progressBar.visibility = View.GONE
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

        viewModel.getFavoriteData(searchQuery).observe(this) { users ->
            binding.progressBar.visibility = View.GONE
            favoriteAdapter.submitList(users)
        }

        binding.rvUsers.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = favoriteAdapter
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}