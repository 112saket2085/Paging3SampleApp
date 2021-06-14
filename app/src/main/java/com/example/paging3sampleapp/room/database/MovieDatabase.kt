package com.example.paging3sampleapp.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.paging3sampleapp.room.dao.MovieDao
import com.example.paging3sampleapp.room.entity.MovieDataModel
import com.example.paging3sampleapp.room.entity.RemoteKey

@Database(entities = [MovieDataModel::class,RemoteKey::class], version = 1, exportSchema = false)
abstract class MovieDatabase : RoomDatabase() {

    abstract fun getMovieDao(): MovieDao

}