package com.example.agency04movieskotlin.Models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MoviesItem(
    val backdrop_path: String?,
    val id: String?,
    val overview: String?,
    val poster_path: String?,
    val release_date: String?,
    val title: String?,
    val genres: List<MoviesItemGenreIDs>?
) : Parcelable