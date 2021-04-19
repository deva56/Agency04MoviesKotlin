package com.example.agency04movieskotlin.Viewmodel

import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agency04movieskotlin.Constants.Companion.api_key
import com.example.agency04movieskotlin.Fragments.MovieItemFragment
import com.example.agency04movieskotlin.Models.MoviesListItem
import com.example.agency04movieskotlin.Repository.NetworkCallsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(private val networkCallsRepository: NetworkCallsRepository) :
    ViewModel() {

    private val itemList: MutableList<MoviesListItem?> = ArrayList()
    private val fragments: ArrayList<Fragment> = ArrayList()
    private val liveDataFragments: MutableLiveData<ArrayList<Fragment>> = MutableLiveData()
    private val throwableLiveData: MutableLiveData<String> = MutableLiveData()

    init {
        getPopularMovies(api_key)
    }

    fun getPopularMovies(api_key: String) {
        viewModelScope.launch {
            try {
                val result = networkCallsRepository.getPopularMovies(api_key)
                if (result.isSuccessful) {
                    itemList.add(result.body())
                    getTopRatedMovies(api_key)
                } else {
                    throwableLiveData.postValue(result.errorBody().toString())
                }
            } catch (e: Exception) {
                throwableLiveData.postValue(e.message)
            }
        }
    }

    private fun getTopRatedMovies(api_key: String) {
        viewModelScope.launch {
            try {
                val result = networkCallsRepository.getTopRatedMovies(api_key)
                if (result.isSuccessful) {
                    itemList.add(result.body())

                    for (item in itemList) {
                        val movieItemFragment = MovieItemFragment()
                        val args = Bundle()
                        args.putParcelableArrayList(
                            "MovieList",
                            item!!.moviesItemList as ArrayList<out Parcelable?>?
                        )
                        movieItemFragment.arguments = args
                        fragments.add(movieItemFragment)
                    }

                    liveDataFragments.postValue(fragments)

                } else {
                    throwableLiveData.postValue(result.errorBody().toString())
                }
            } catch (e: Exception) {
                throwableLiveData.postValue(e.message)
            }
        }
    }

    fun getFragments(): LiveData<ArrayList<Fragment>> {
        return liveDataFragments
    }

    fun getThrowableLiveData(): LiveData<String> {
        return throwableLiveData
    }
}
