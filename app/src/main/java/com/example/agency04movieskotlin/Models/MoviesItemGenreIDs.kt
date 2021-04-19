package com.example.agency04movieskotlin.Models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MoviesItemGenreIDs(
    val id: String?,
    val name: String?
) : Parcelable