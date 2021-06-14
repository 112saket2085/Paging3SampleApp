package com.example.paging3sampleapp.view.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.paging3sampleapp.databinding.LoadStateItemBinding

class MovieLoadStateAdapter(private val onRetryClick: () -> Unit) :
    LoadStateAdapter<MovieLoadStateAdapter.LoadStateViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): LoadStateViewHolder {
        return LoadStateViewHolder(
            LoadStateItemBinding.inflate(
                LayoutInflater.from(parent.context)
            ), onRetryClick
        )
    }

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }


    class LoadStateViewHolder(
        private val binding: LoadStateItemBinding,
        val onRetryClick: () -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(loadState: LoadState) {

            setViewVisibility(loadState)
            if (loadState is LoadState.Error) {
                binding.textViewError.text = loadState.error.localizedMessage
                binding.textViewRetry.setOnClickListener {
                    setViewVisibility(LoadState.Loading)
                    onRetryClick()
                }
            }
        }

        private fun setViewVisibility(loadState: LoadState) {
            with(binding) {
                progressBar.isVisible = loadState is LoadState.Loading
                textViewError.isVisible = loadState is LoadState.Error
                textViewRetry.isVisible = loadState is LoadState.Error
            }
        }
    }
}