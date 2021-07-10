package com.example.paging3sampleapp.room.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class MovieDataModel(
    @PrimaryKey val imdbID: String,
    var tag: String,
    val Title: String,
    val Type: String,
    val Year: String,
    val Poster: String
):Parcelable