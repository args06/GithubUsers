package com.example.githubusers.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubusers.data.local.entity.FavoriteEntity
import com.example.githubusers.data.remote.response.UserResponseItem
import com.example.githubusers.databinding.UserItemBinding
import com.example.githubusers.helper.OnSetFavorite

class HomeAdapter : ListAdapter<UserResponseItem, HomeAdapter.ViewHolder>(DIFF_CALLBACK) {

    var onFavoriteClickListener: OnSetFavorite? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = UserItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)

        val favoriteUser = FavoriteEntity(username = user.login, avatarUrl = user.avatarUrl)
        onFavoriteClickListener?.onFavoriteDrawable(favoriteUser, holder.binding.ibFavorite)

        holder.binding.ibFavorite.setOnClickListener {
            onFavoriteClickListener?.onFavoriteClick(favoriteUser, holder.binding.ibFavorite)
        }
    }

    class ViewHolder(val binding: UserItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: UserResponseItem) {
            with(binding) {
                Glide.with(itemView.context).load(user.avatarUrl).into(ivProfileImage)
                tvUsername.text = user.login

                cvUser.setOnClickListener {view ->
                    val toDetailFragment = HomeFragmentDirections.actionNavigationHomeToNavigationDetail()
                    toDetailFragment.username = user.login
                    view.findNavController().navigate(toDetailFragment)
                }
            }

        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<UserResponseItem>() {
            override fun areItemsTheSame(
                oldItem: UserResponseItem, newItem: UserResponseItem
            ): Boolean {
                return oldItem.login == newItem.login
            }

            override fun areContentsTheSame(
                oldItem: UserResponseItem, newItem: UserResponseItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}