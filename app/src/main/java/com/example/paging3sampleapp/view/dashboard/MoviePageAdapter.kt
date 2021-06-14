package com.example.paging3sampleapp.view.dashboard

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintSet
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.paging3sampleapp.R
import com.example.paging3sampleapp.databinding.ItemsMoviesBinding
import com.example.paging3sampleapp.room.entity.MovieDataModel
import com.example.paging3sampleapp.util.loadImage

class MoviePageAdapter :
    PagingDataAdapter<MovieDataModel, MoviePageAdapter.ViewHolder>(diffCallback = object :
        DiffUtil.ItemCallback<MovieDataModel>() {

        override fun areItemsTheSame(oldItem: MovieDataModel, newItem: MovieDataModel): Boolean {
            return oldItem.imdbID == newItem.imdbID
        }

        override fun areContentsTheSame(oldItem: MovieDataModel, newItem: MovieDataModel): Boolean {
            return oldItem == newItem
        }

    }) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemsMoviesBinding.inflate(LayoutInflater.from(parent.context)),
            parent.context
        )
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)
        holder.bind(data)
    }

    class ViewHolder(private val binding: ItemsMoviesBinding, private val context: Context) :
        RecyclerView.ViewHolder(binding.root) {

        private val constraintSet = ConstraintSet()


        fun bind(movieDataModel: MovieDataModel?) {
            with(binding) {
                movieDataModel?.run {
                    imageViewMovie.loadImage(
                        Poster, R.mipmap.movie_placeholder,
                        errorPlaceholderResId = R.mipmap.movie_placeholder
                    ) { _ ->
                        imageViewMovie.stopLoading()
                    }
                    textViewName.text = Title
                    textViewId.text = imdbID
                    textViewYear.text = Year

                    val ratio = String.format("%d:%d", 1, 2)
                    constraintSet.clone(constraintLayout)
                    constraintSet.setDimensionRatio(imageViewMovie.id, ratio)
                    constraintSet.applyTo(constraintLayout)
                }
            }
        }
    }
}