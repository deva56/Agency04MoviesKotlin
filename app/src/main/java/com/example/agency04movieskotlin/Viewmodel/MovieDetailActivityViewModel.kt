package com.example.agency04movieskotlin.Viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agency04movieskotlin.Constants.Companion.SELECTED_MOVIE_ID
import com.example.agency04movieskotlin.Constants.Companion.api_key
import com.example.agency04movieskotlin.Models.MoviesItem
import com.example.agency04movieskotlin.Repository.NetworkCallsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailActivityViewModel @Inject constructor(
    private val networkCallsRepository: NetworkCallsRepository,
) :
    ViewModel() {

    private val throwableLiveData: MutableLiveData<String> = MutableLiveData()
    private val moviesItemLiveData: MutableLiveData<MoviesItem> = MutableLiveData()

    init {
        getMovieDetail(SELECTED_MOVIE_ID, api_key)
    }

    fun getMovieDetail(movieID: String, api_key: String) {
        viewModelScope.launch {
            try {
                val result = networkCallsRepository.getMovieDetail(movieID, api_key)
                if (result.isSuccessful) {
                    moviesItemLiveData.postValue(result.body())
                } else {
                    throwableLiveData.postValue(result.errorBody().toString())
                }
            } catch (e: Exception) {
                throwableLiveData.postValue(e.message)
            }
        }
    }

    fun getThrowableLiveData(): LiveData<String> {
        return throwableLiveData
    }

    fun getMoviesItemLiveData(): LiveData<MoviesItem> {
        return moviesItemLiveData
    }
}