package com.example.agency04movieskotlin.Repository

import com.example.agency04movieskotlin.Models.MoviesItem
import com.example.agency04movieskotlin.Models.MoviesListItem
import com.example.agency04movieskotlin.Network.RetrofitInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkCallsRepository @Inject constructor(private val retrofitInterface: RetrofitInterface) {

    suspend fun getTopRatedMovies(api_key: String): Response<MoviesListItem?> {
        return withContext(Dispatchers.IO) {
            retrofitInterface.getTopRatedMovies(api_key)
        }
    }

    suspend fun getPopularMovies(api_key: String): Response<MoviesListItem?> {
        return withContext(Dispatchers.IO) {
            retrofitInterface.getPopularMovies(api_key)
        }
    }

    suspend fun getMovieDetail(id: String, api_key: String): Response<MoviesItem> {
        return withContext(Dispatchers.IO) {
            retrofitInterface.getMovieDetail(id, api_key)
        }
    }

    suspend fun getSimilarMovies(id: String, api_key: String): Response<MoviesListItem?> {
        return withContext(Dispatchers.IO) {
            retrofitInterface.getSimilarMovies(id, api_key)
        }
    }
}