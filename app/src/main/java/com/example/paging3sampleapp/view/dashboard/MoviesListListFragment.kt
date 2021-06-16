package com.example.paging3sampleapp.view.dashboard

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.example.paging3sampleapp.R
import com.example.paging3sampleapp.databinding.FragmentMovieListBinding
import com.example.paging3sampleapp.util.showToastMsg
import com.example.paging3sampleapp.view.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MoviesListListFragment : BaseFragment(R.layout.fragment_movie_list) {


    private val fetchMoviesViewModel by viewModels<FetchMoviesViewModel>()
    private lateinit var adapter: MoviePageAdapter
    private lateinit var binding: FragmentMovieListBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMovieListBinding.bind(view)
        initView()
        observeData()
    }

    private fun initView() {
        adapter = MoviePageAdapter()
        binding.recyclerViewMovies.adapter =
            adapter.withLoadStateFooter(
                footer = MovieLoadStateAdapter {
                    adapter.retry()
                })
        binding.swipeRefresh.setOnRefreshListener {
            adapter.refresh()
        }
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            adapter.loadStateFlow.collect { loadStates ->
                with(binding) {
                    with(loadStates) {
                        if (swipeRefresh.isRefreshing && refresh is LoadState.Error && adapter.itemCount > 0) {
                            fragmentContext.showToastMsg(getString(R.string.str_empty_movie_list))
                        }
                        swipeRefresh.isRefreshing =
                            swipeRefresh.isRefreshing && refresh is LoadState.Loading
                        contentProgressBar.contentProgressBarLayout.isVisible =
                            adapter.itemCount == 0 && (refresh is LoadState.Loading || mediator == null)
                        contentEmpty.contentEmptyLayout.isVisible =
                            adapter.itemCount == 0 && refresh is LoadState.Error
                        contentEmpty.textViewRetry.setOnClickListener {
                            adapter.retry()
                        }
                        if (refresh is LoadState.Error) {
                            binding.contentEmpty.textViewError.text =
                                (refresh as LoadState.Error).error.localizedMessage
                        }
                    }

                }
            }
        }
        fetchMoviesViewModel.setQueryParameter("Movies").pagingDataLiveData.observe(
            viewLifecycleOwner
        ) {
            lifecycleScope.launch {
                adapter.submitData(it)
            }
        }
    }
}