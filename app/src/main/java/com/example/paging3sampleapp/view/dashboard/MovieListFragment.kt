package com.example.paging3sampleapp.view.dashboard

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.recyclerview.widget.ConcatAdapter
import com.example.paging3sampleapp.R
import com.example.paging3sampleapp.databinding.FragmentMovieListBinding
import com.example.paging3sampleapp.util.AppConfig
import com.example.paging3sampleapp.util.Logger
import com.example.paging3sampleapp.util.showToastMsg
import com.example.paging3sampleapp.view.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@ExperimentalPagingApi
@AndroidEntryPoint
class MovieListFragment : BaseFragment(R.layout.fragment_movie_list) {
    @Inject
    lateinit var adapter: MoviePageAdapter

    @Inject
    lateinit var appConfig: AppConfig

    private lateinit var binding: FragmentMovieListBinding
    private val fetchMoviesViewModel by viewModels<FetchMoviesViewModel>()
    private var job: Job? = null
    private var isMoviesSearched = false
    private lateinit var concatAdapter: ConcatAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity.setTheme(R.style.AppTheme)
        binding = FragmentMovieListBinding.bind(view)
        setHasOptionsMenu(true)
        initView()
        searchMovies()
        setOnClickListener()
        observeLoadStates()
    }

    private fun initView() {
        if (!::concatAdapter.isInitialized) {
            concatAdapter = adapter.withLoadStateFooter(
                footer = MovieLoadStateAdapter {
                    adapter.retry()
                })
        }
        binding.recyclerViewMovies.adapter = concatAdapter
        binding.swipeRefresh.setOnRefreshListener {
            adapter.refresh()
        }
    }

    private fun searchMovies() {
        lifecycleScope.launch {
            val query = appConfig.getQueryParam()
            setActionBarTitle(query)
            observeData(query)
            cancel()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_search, menu)
        val menuItem = menu.findItem(R.id.action_search)
        val searchView = menuItem.actionView as SearchView
        initSearchView(menuItem, searchView)
    }


    /**
     * Method to initialize Search View
     * @param menuItem Search Menu Item
     * @param searchView Search View Action class
     */
    private fun initSearchView(menuItem: MenuItem, searchView: SearchView) {
        searchView.queryHint = getString(R.string.str_search)
        val searchPlateView = searchView.findViewById<View>(androidx.appcompat.R.id.search_plate)
        searchPlateView.setBackgroundColor(
            ContextCompat.getColor(
                fragmentContext,
                android.R.color.transparent
            )
        )
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                searchView.isIconified = !searchView.isIconified
                menuItem.collapseActionView()
                isMoviesSearched = true
                setActionBarTitle(query)
                observeData(query)
                return false
            }

            override fun onQueryTextChange(s: String?): Boolean {
                return false
            }
        })
    }

    /**
     * Collect Combined Load state flow for data loading states
     */
    private fun observeLoadStates() {
        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                adapter.loadStateFlow.collectLatest { loadStates ->
                    Logger.log<MovieListFragment>(
                        Logger.LogType.DEBUG,
                        loadStates.toString()
                    )
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
                            if (isMoviesSearched && append is LoadState.Loading) {
                                isMoviesSearched = false
                                binding.recyclerViewMovies.scrollToPosition(0)
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Method to observe PagingData changes and set PagingData to PagingDataAdapter
     */
    private fun observeData(query: String) {
        job?.cancel()
        job = viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                fetchMoviesViewModel.loadPagingData(query).collectLatest {
                    Logger.log<MovieListFragment>(Logger.LogType.DEBUG, "Submit Data called")
                    appConfig.setQueryParam(query)
                    adapter.submitData(it)
                }
            }
        }
    }

    private fun setOnClickListener() {
        adapter.onItemClick = {
            navigateTo(
                navDirections = MovieListFragmentDirections.actionMoviesListFragmentToMovieDetailFragment(
                    it
                )
            )
        }
    }
}