package com.example.paging3sampleapp.view.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.paging3sampleapp.repository.PagerRepository
import com.example.paging3sampleapp.rest.api.MovieDetailService
import com.example.paging3sampleapp.room.entity.MovieDataModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@ExperimentalPagingApi
@HiltViewModel
class FetchMoviesViewModel
@Inject constructor(
    private val repository: PagerRepository,
    private val movieDetailService: MovieDetailService
) : ViewModel() {

    private var query: String = ""
    private lateinit var data: Flow<PagingData<MovieDataModel>>

    fun loadPagingData(query: String): Flow<PagingData<MovieDataModel>> {
        return if (this.query == query && ::data.isInitialized) {
            data
        } else {
            this.query = query
            val flowData = repository.getPager(query).cachedIn(viewModelScope)
            this.data = flowData
            flowData
        }
    }

    val stateFlowLiveData = exposeData().map {
        val s = "Total Result is $it"
        s
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = "Initial value"
    )

    fun exposeData() = flow {
        val response = movieDetailService.getMovieDetails(query = query, page = 1)
        emit(response.totalResults + 10)
        val response2 = movieDetailService.getMovieDetails(query = query, page = 1)
        emit(response2.totalResults+ 20)
        val response3 = movieDetailService.getMovieDetails(query = query, page = 1)
        emit(response3.totalResults+ 30)
        val response4 = movieDetailService.getMovieDetails(query = query, page = 1)
        emit(response4.totalResults+ 40)
    }
}