package com.example.paging3sampleapp.room.dao

import androidx.paging.PagingSource
import androidx.room.*
import com.example.paging3sampleapp.room.entity.MovieDataModel
import com.example.paging3sampleapp.room.entity.RemoteKey

@Dao
interface MovieDao {

    @Query("Select * from MovieDataModel")
    fun getMovieListData(): PagingSource<Int, MovieDataModel>

    @Query("Delete from MovieDataModel")
    suspend fun deleteAllMovieData()

    @Query("Delete from RemoteKey where `query`=:query")
    suspend fun deleteRemoteKey(query: String)

    @Query("Select nextKey from RemoteKey where `query` = :query")
    suspend fun getNextPageNumber(query: String): Int?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(moviesDataModel: List<MovieDataModel>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertKey(remoteKey: RemoteKey)

}