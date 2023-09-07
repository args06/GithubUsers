package com.example.githubusers.ui.home

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubusers.R
import com.example.githubusers.data.Result
import com.example.githubusers.data.local.entity.FavoriteEntity
import com.example.githubusers.databinding.FragmentHomeBinding
import com.example.githubusers.helper.OnSetFavorite
import com.example.githubusers.helper.SettingPreferences
import com.example.githubusers.ui.ViewModelFactory

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pref = SettingPreferences.getInstance(requireContext().dataStore)

        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity(), pref)
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
        val homeAdapter = HomeAdapter()
        homeAdapter.onFavoriteClickListener = object : OnSetFavorite {
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

        viewModel.getUserData(searchQuery).observe(this) { users ->
            if (users != null) {
                when (users) {
                    is Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is Result.Success -> {
                        binding.progressBar.visibility = View.GONE
                        val userData = users.data
                        homeAdapter.submitList(userData)
                    }
                    is Result.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(
                            requireActivity(),
                            "Terjadi kesalahan" + users.error,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

        binding.rvUsers.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = homeAdapter
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}