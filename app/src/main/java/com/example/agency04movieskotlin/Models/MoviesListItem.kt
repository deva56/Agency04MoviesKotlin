package com.example.agency04movieskotlin.Models

import com.google.gson.annotations.SerializedName

data class MoviesListItem(
    @SerializedName("results")
    val moviesItemList: List<MoviesItem>?
)