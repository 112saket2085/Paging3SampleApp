package com.example.paging3sampleapp.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MovieDataModel(
    @PrimaryKey val imdbID: String,
    val Title: String,
    val Year: String,
    val Poster: String
)