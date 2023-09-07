package com.example.githubusers.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubusers.data.local.entity.FavoriteEntity
import com.example.githubusers.databinding.UserItemBinding
import com.example.githubusers.helper.OnSetFavorite

class FavoriteAdapter : ListAdapter<FavoriteEntity, FavoriteAdapter.ViewHolder>(DIFF_CALLBACK) {

    var onFavoriteClickListener: OnSetFavorite? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = UserItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)

        onFavoriteClickListener?.onFavoriteDrawable(user, holder.binding.ibFavorite)

        holder.binding.ibFavorite.setOnClickListener {
            onFavoriteClickListener?.onFavoriteClick(user, holder.binding.ibFavorite)
        }
    }

    class ViewHolder(val binding: UserItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: FavoriteEntity) {
            with(binding) {
                Glide.with(itemView.context).load(user.avatarUrl).into(ivProfileImage)
                tvUsername.text = user.username

                cvUser.setOnClickListener {view ->
                    val toDetailFragment = FavoriteFragmentDirections.actionNavigationFavoriteToNavigationDetail()
                    toDetailFragment.username = user.username
                    view.findNavController().navigate(toDetailFragment)
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<FavoriteEntity>() {
            override fun areItemsTheSame(
                oldItem: FavoriteEntity, newItem: FavoriteEntity
            ): Boolean {
                return oldItem.username == newItem.username
            }

            override fun areContentsTheSame(
                oldItem: FavoriteEntity, newItem: FavoriteEntity
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}