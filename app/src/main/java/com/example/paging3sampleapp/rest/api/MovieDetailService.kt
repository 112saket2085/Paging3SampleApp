package com.example.paging3sampleapp.rest.api

import com.example.paging3sampleapp.app.Constants
import com.example.paging3sampleapp.room.entity.MovieDataModel
import com.example.paging3sampleapp.rest.response.ResponseView
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieDetailService {


    @GET(Constants.BASE_URL)
    suspend fun getMovieDetails(
        @Query("apikey") apikey: String = Constants.apikey,
        @Query("s") query: String,
        @Query("page") page: Int
    ): ResponseView.BaseResponse<List<MovieDataModel>>?
}