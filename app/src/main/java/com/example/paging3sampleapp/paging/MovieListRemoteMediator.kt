package com.example.paging3sampleapp.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.example.paging3sampleapp.rest.api.MovieDetailService
import com.example.paging3sampleapp.room.dao.MovieDao
import com.example.paging3sampleapp.room.entity.MovieDataModel
import com.example.paging3sampleapp.room.entity.RemoteKey
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class MovieListRemoteMediator @Inject constructor(
    private val dao: MovieDao,
    private val apiService: MovieDetailService
) : RemoteMediator<Int, MovieDataModel>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MovieDataModel>
    ): MediatorResult {
        return try {
            val nextPageNumber = when (loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    dao.getNextPageNumber("Batman") ?: 1
                }
            }
            val response = apiService.getMovieDetails(query = "Batman", page = nextPageNumber)
            response?.Search?.let {
                dao.insertMovies(it)
                dao.insertKey(RemoteKey("Batman", nextPageNumber + 1))
            }
            MediatorResult.Success(
                endOfPaginationReached = response == null || response.Search?.isEmpty() ?: true || (state.pages.size + 10 >= response.totalResults.toInt())
            )
        } catch (e: Throwable) {
            MediatorResult.Error(e)
        }
    }

    override suspend fun initialize(): InitializeAction {
        return if (dao.getNextPageNumber("Batman") == null) InitializeAction.LAUNCH_INITIAL_REFRESH else InitializeAction.SKIP_INITIAL_REFRESH
    }
}