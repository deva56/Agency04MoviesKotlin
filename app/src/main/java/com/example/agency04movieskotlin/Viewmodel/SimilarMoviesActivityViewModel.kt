package com.example.agency04movieskotlin.Viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agency04movieskotlin.Constants.Companion.SELECTED_MOVIE_ID
import com.example.agency04movieskotlin.Constants.Companion.api_key
import com.example.agency04movieskotlin.Models.MoviesListItem
import com.example.agency04movieskotlin.Repository.NetworkCallsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SimilarMoviesActivityViewModel @Inject constructor(
    private val networkCallsRepository: NetworkCallsRepository
) :
    ViewModel() {

    private val throwableLiveData: MutableLiveData<String> = MutableLiveData()
    private val moviesListItemLiveData: MutableLiveData<MoviesListItem> = MutableLiveData()

    init {
        getSimilarMovies(SELECTED_MOVIE_ID, api_key)
    }

    fun getSimilarMovies(movieID: String, api_key: String) {
        viewModelScope.launch {
            try {
                val result = networkCallsRepository.getTopRatedMovies(api_key)
                if (result.isSuccessful) {
                    moviesListItemLiveData.postValue(result.body())
                } else {
                    throwableLiveData.postValue(result.errorBody().toString())
                }
            } catch (e: Exception) {
                throwableLiveData.postValue(e.message)
            }
        }
    }


    fun getThrowableLiveData(): MutableLiveData<String> {
        return throwableLiveData
    }

    fun getMoviesListItemLiveData(): MutableLiveData<MoviesListItem> {
        return moviesListItemLiveData
    }
}