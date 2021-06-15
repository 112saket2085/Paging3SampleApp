package com.example.paging3sampleapp.di.module

import android.content.Context
import androidx.room.Room
import com.example.paging3sampleapp.app.AppConstants
import com.example.paging3sampleapp.room.database.MovieDatabase
import com.example.paging3sampleapp.rest.api.MovieDetailService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun getDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, MovieDatabase::class.java, "movie_database").build()

    @Provides
    @Singleton
    fun getMovieDao(database: MovieDatabase) = database.getMovieDao()

    @Provides
    @Singleton
    fun getOkhttpClient(): OkHttpClient = OkHttpClient.Builder().run {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        addNetworkInterceptor(loggingInterceptor).build()
    }

    @Provides
    @Singleton
    fun getMovieDetailService(okHttpClient: OkHttpClient): MovieDetailService =
        Retrofit.Builder()
            .baseUrl(AppConstants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build().create(MovieDetailService::class.java)
}