package com.example.paging3sampleapp.view.dashboard

import androidx.lifecycle.*
import androidx.paging.*
import com.example.paging3sampleapp.paging.MovieListRemoteMediator
import com.example.paging3sampleapp.room.dao.MovieDao
import com.example.paging3sampleapp.room.entity.MovieDataModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FetchMoviesViewModel @Inject constructor(
    private val dao: MovieDao,
    private val remoteMediator: MovieListRemoteMediator
) : ViewModel() {

    @OptIn(ExperimentalPagingApi::class)
    internal val pagingDataLiveData by lazy {
        Pager(
            config = PagingConfig(pageSize = 10, initialLoadSize = 10),
            remoteMediator = remoteMediator
        ) {
            dao.getMovieListData()
        }.flow.asLiveData().cachedIn(viewModelScope)
    }
}