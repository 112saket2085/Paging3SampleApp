package com.example.paging3sampleapp.rest.api

import com.example.paging3sampleapp.app.AppConstants
import com.example.paging3sampleapp.room.entity.MovieDataModel
import com.example.paging3sampleapp.rest.response.ResponseView
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieDetailService {


    @GET(AppConstants.BASE_URL)
    suspend fun getMovieDetails(
        @Query("apikey") apikey: String = AppConstants.apikey,
        @Query("s") query: String,
        @Query("page") page: Int
    ): ResponseView.BaseResponse<List<MovieDataModel>>
}