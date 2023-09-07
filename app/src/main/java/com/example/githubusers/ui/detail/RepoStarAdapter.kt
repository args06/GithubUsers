package com.example.githubusers.ui.detail

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.githubusers.data.remote.response.RepoResponseItem
import com.example.githubusers.databinding.RepoItemBinding

class RepoStarAdapter: ListAdapter<RepoResponseItem, RepoStarAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RepoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val repoStar = getItem(position)
        holder.bind(repoStar)
    }

    class ViewHolder(val binding: RepoItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(repoStar: RepoResponseItem) {
            with(binding) {
                tvProjectName.text = repoStar.name
                tvLanguage.text = repoStar.language
                tvLastUpdate.text = repoStar.updatedAt.substring(0, 10)
                tvVisibility.text = repoStar.visibility
                tvFork.text = repoStar.forksCount.toString()
                cvUser.setOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(repoStar.htmlUrl)
                    it.context.startActivity(intent)
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<RepoResponseItem>() {
            override fun areItemsTheSame(
                oldItem: RepoResponseItem, newItem: RepoResponseItem
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: RepoResponseItem, newItem: RepoResponseItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}