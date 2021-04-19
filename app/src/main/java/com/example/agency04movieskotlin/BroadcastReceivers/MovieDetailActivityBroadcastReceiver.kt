package com.example.agency04movieskotlin.BroadcastReceivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.view.View
import com.example.agency04movieskotlin.Activity.MovieDetailActivity
import com.example.agency04movieskotlin.Constants.Companion.api_key
import com.example.agency04movieskotlin.Models.MoviesItem
import com.example.agency04movieskotlin.R
import com.example.agency04movieskotlin.Viewmodel.MovieDetailActivityViewModel

class MovieDetailActivityBroadcastReceiver(
    private val movieDetailActivityViewModel: MovieDetailActivityViewModel,
    private val movieID: String, private val movieDetailActivity: MovieDetailActivity
) :
    BroadcastReceiver() {

    private var moviesItem: MoviesItem? = null

    fun setMoviesItem(moviesItem: MoviesItem?) {
        this.moviesItem = moviesItem
    }

    override fun onReceive(context: Context, intent: Intent?) {
        val conn = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = conn.activeNetworkInfo
        if (networkInfo != null && (networkInfo.type == ConnectivityManager.TYPE_WIFI || networkInfo.type == ConnectivityManager.TYPE_MOBILE)) {
            if (moviesItem == null) {
                movieDetailActivity.findViewById<View>(R.id.MovieDetailNoInternetLinearLayout).visibility =
                    View.GONE
            } else {
                movieDetailActivity.findViewById<View>(R.id.MovieDetailNoInternetLinearLayout2).visibility =
                    View.GONE
            }
            if (moviesItem == null) {
                movieDetailActivity.findViewById<View>(R.id.MovieDetailProgressBar).visibility =
                    View.VISIBLE
                movieDetailActivityViewModel.getMovieDetail(movieID, api_key)
            }
        } else {
            if (moviesItem == null) {
                movieDetailActivity.findViewById<View>(R.id.MovieDetailNoInternetLinearLayout).visibility =
                    View.VISIBLE
            } else {
                movieDetailActivity.findViewById<View>(R.id.MovieDetailNoInternetLinearLayout2).visibility =
                    View.VISIBLE
            }
        }
    }

}