package com.example.agency04movieskotlin.Network

import com.example.agency04movieskotlin.Models.MoviesItem
import com.example.agency04movieskotlin.Models.MoviesListItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RetrofitInterface {

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(@Query("api_key") apiKey: String): Response<MoviesListItem?>

    @GET("movie/popular")
    suspend fun getPopularMovies(@Query("api_key") apiKey: String): Response<MoviesListItem?>

    @GET("movie/{id}")
    suspend fun getMovieDetail(
        @Path("id") id: String,
        @Query("api_key") apiKey: String
    ): Response<MoviesItem>

    @GET("movie/{id}/recommendations")
    suspend fun getSimilarMovies(
        @Path("id") id: String,
        @Query("api_key") apiKey: String
    ): Response<MoviesListItem?>
}