package com.example.paging3sampleapp.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.paging3sampleapp.room.dao.MovieDao
import com.example.paging3sampleapp.room.entity.MovieDataModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ExperimentalPagingApi
class PagerRepository @Inject constructor(
    private val dao: MovieDao,
    private val remoteMediator: MovieListRemoteMediator
) {

    fun getPager(query: String): Flow<PagingData<MovieDataModel>> = Pager(
        config = PagingConfig(pageSize = 50),
        remoteMediator = remoteMediator.setQueryParameter(query = query)
    ) {
        dao.getMovieDataPagingSource(query)
    }.flow
}