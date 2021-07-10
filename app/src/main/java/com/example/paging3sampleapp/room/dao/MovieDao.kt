package com.example.paging3sampleapp.room.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.paging3sampleapp.room.entity.MovieDataModel
import com.example.paging3sampleapp.room.entity.RemoteKey

@Dao
interface MovieDao {

    @Query("Select * from MovieDataModel where tag = :query")
    fun getMovieDataPagingSource(query: String): PagingSource<Int, MovieDataModel>

    @Query("Delete from MovieDataModel where tag = :query")
    suspend fun deleteMovieData(query: String)

    @Query("Delete from RemoteKey where `query`=:query")
    suspend fun deleteRemoteKey(query: String)

    @Query("Select nextKey from RemoteKey where `query` = :query")
    suspend fun getNextPageNumber(query: String): Int?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(moviesDataModel: List<MovieDataModel>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertKey(remoteKey: RemoteKey)

}