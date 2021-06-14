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
import kotlinx.coroutines.flow.collectLatest
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
            adapter.withLoadStateFooter(footer = MovieLoadStateAdapter {
                adapter.retry()
            })
        binding.swipeRefresh.setOnRefreshListener {
            adapter.refresh()
        }
    }

    private fun observeData() {
        lifecycleScope.launch {
            adapter.loadStateFlow.collectLatest { loadStates ->
                if (binding.swipeRefresh.isRefreshing && loadStates.refresh is LoadState.Error && adapter.itemCount > 0) {
                    fragmentContext.showToastMsg(getString(R.string.str_empty_movie_list))
                }
                binding.swipeRefresh.isRefreshing =
                    binding.swipeRefresh.isRefreshing && loadStates.refresh is LoadState.Loading
                binding.contentProgressBar.contentProgressBarLayout.isVisible =
                    adapter.itemCount == 0 && (loadStates.refresh is LoadState.Loading || loadStates.mediator == null)
                binding.contentEmpty.contentEmptyLayout.isVisible =
                    adapter.itemCount == 0 && loadStates.refresh is LoadState.Error
                binding.contentEmpty.textViewRetry.setOnClickListener {
                    adapter.retry()
                }
            }
        }
        fetchMoviesViewModel.pagingDataLiveData.observe(viewLifecycleOwner) {
            lifecycleScope.launch {
                adapter.submitData(it)
            }
        }
    }
}