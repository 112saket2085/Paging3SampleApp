package com.example.paging3sampleapp.view.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.paging3sampleapp.R
import com.example.paging3sampleapp.databinding.ItemsMoviesBinding
import com.example.paging3sampleapp.room.entity.MovieDataModel
import com.example.paging3sampleapp.util.getShimmerPlaceholder
import com.example.paging3sampleapp.util.loadImage
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject

@FragmentScoped
class MoviePageAdapter @Inject constructor() :
    PagingDataAdapter<MovieDataModel, MoviePageAdapter.ViewHolder>(diffCallback = object :
        DiffUtil.ItemCallback<MovieDataModel>() {

        override fun areItemsTheSame(oldItem: MovieDataModel, newItem: MovieDataModel): Boolean {
            return oldItem.imdbID == newItem.imdbID
        }

        override fun areContentsTheSame(oldItem: MovieDataModel, newItem: MovieDataModel): Boolean {
            return oldItem == newItem
        }

    }) {

    internal lateinit var onItemClick: (MovieDataModel) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemsMoviesBinding.inflate(LayoutInflater.from(parent.context))
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)
        holder.bind(data)
        data?.let { movieData ->
            holder.itemView.setOnClickListener {
                onItemClick(movieData)
            }
        }
    }

    class ViewHolder(private val binding: ItemsMoviesBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(movieDataModel: MovieDataModel?) {
            with(binding) {
                movieDataModel?.run {
                    imageViewMovie.loadImage(
                        Poster,
                        shimmerPlaceholder = imageViewMovie.getShimmerPlaceholder(baseColor = R.color.light_grey),
                        errorPlaceholderResId = R.mipmap.image_placeholder_new
                    )
                }
            }
        }
    }
}