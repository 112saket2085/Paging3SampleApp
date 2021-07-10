package com.example.paging3sampleapp.view.details

import android.os.Bundle
import android.view.View
import com.example.paging3sampleapp.R
import com.example.paging3sampleapp.databinding.FragmentMovieDetailBinding
import com.example.paging3sampleapp.room.entity.MovieDataModel
import com.example.paging3sampleapp.util.getShimmerPlaceholder
import com.example.paging3sampleapp.util.loadImage
import com.example.paging3sampleapp.view.base.BaseFragment


class MovieDetailFragment : BaseFragment(R.layout.fragment_movie_detail) {

    private lateinit var binding: FragmentMovieDetailBinding

    override fun isToolbarNeeded(): Boolean {
        return false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMovieDetailBinding.bind(view)
        initView()
        getSafeArgsValue()
        setOnClickListener()
    }

    private fun getSafeArgsValue() {
        arguments?.let {
            val movieData = MovieDetailFragmentArgs.fromBundle(it).movieData
            setMovieDetail(movieData)
        }
    }

    private fun initView() {
        binding.toolbar.setNavigationIcon(R.drawable.ic_back)
    }

    private fun setMovieDetail(movieDataModel: MovieDataModel) {
        with(movieDataModel) {
            binding.imageViewMovie.loadImage(
                Poster,
                shimmerPlaceholder = binding.imageViewMovie.getShimmerPlaceholder(baseColor = R.color.list_bg),
                errorPlaceholderResId = R.mipmap.image_placeholder_new
            )
            binding.contentMovieDetail.textViewMovieName.text = Title
            binding.contentMovieDetail.textViewTypeName.text = Type
            binding.contentMovieDetail.textViewYearDetail.text = Year
            binding.toolbar.title = Title
        }
    }

    private fun setOnClickListener() {
        binding.toolbar.setNavigationOnClickListener {
            navigateUp()
        }
    }
}