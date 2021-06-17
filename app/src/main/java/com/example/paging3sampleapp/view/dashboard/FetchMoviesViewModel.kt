package com.example.paging3sampleapp.view.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.paging3sampleapp.paging.MovieListRemoteMediator
import com.example.paging3sampleapp.room.dao.MovieDao
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@ExperimentalPagingApi
@HiltViewModel
class FetchMoviesViewModel
@Inject constructor(
    private val dao: MovieDao,
    private val remoteMediator: MovieListRemoteMediator,
) : ViewModel() {

    fun loadPagingData(query: String) = Pager(
        config = PagingConfig(pageSize = 10, initialLoadSize = 20),
        remoteMediator = remoteMediator.setQueryParameter(query = query)
    ) {
        dao.getMovieListData()
    }.flow.asLiveData().cachedIn(viewModelScope)
}