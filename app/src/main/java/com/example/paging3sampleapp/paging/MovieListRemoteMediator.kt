package com.example.paging3sampleapp.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.example.paging3sampleapp.app.AppConstants
import com.example.paging3sampleapp.app.AppConstants.NETWORK_PAGE_SIZE
import com.example.paging3sampleapp.rest.api.MovieDetailService
import com.example.paging3sampleapp.room.dao.MovieDao
import com.example.paging3sampleapp.room.entity.MovieDataModel
import com.example.paging3sampleapp.room.entity.RemoteKey
import java.io.IOException
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class MovieListRemoteMediator @Inject constructor(
    private val dao: MovieDao,
    private val apiService: MovieDetailService
) : RemoteMediator<Int, MovieDataModel>() {

    private var query: String = ""

    fun setQueryParameter(
        query: String
    ): MovieListRemoteMediator {
        return this.apply {
            this.query = query
        }
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MovieDataModel>
    ): MediatorResult {
        return try {
            val nextPageNumber = when (loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    dao.getNextPageNumber(query) ?: 1
                }
            }
            val response = apiService.getMovieDetails(query = query, page = nextPageNumber)
            response.Error?.let {
                return MediatorResult.Error(Throwable(it))
            }
            if (loadType == LoadType.REFRESH) {
                dao.deleteAllMovieData()
                dao.deleteRemoteKey(query)
            }
            response.Search?.let {
                dao.insertMovies(it)
                dao.insertKey(RemoteKey(query, nextPageNumber + 1))
            }
            MediatorResult.Success(
                endOfPaginationReached = response.Search?.isEmpty() ?: true || (nextPageNumber * NETWORK_PAGE_SIZE >= response.totalResults.toInt())
            )
        } catch (e: Throwable) {
            MediatorResult.Error(Throwable(if (e is IOException) AppConstants.NO_INTERNET_AVAILABLE else AppConstants.SERVER_ERROR))
        }
    }

    override suspend fun initialize(): InitializeAction {
        return if (dao.getNextPageNumber(query) == null) InitializeAction.LAUNCH_INITIAL_REFRESH else InitializeAction.SKIP_INITIAL_REFRESH
    }
}