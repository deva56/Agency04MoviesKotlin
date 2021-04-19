package com.example.agency04movieskotlin.BroadcastReceivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.view.View
import com.example.agency04movieskotlin.Activity.SimilarMoviesActivity
import com.example.agency04movieskotlin.Constants.Companion.api_key
import com.example.agency04movieskotlin.Models.MoviesListItem
import com.example.agency04movieskotlin.R
import com.example.agency04movieskotlin.Viewmodel.SimilarMoviesActivityViewModel

class SimilarMoviesActivityBroadcastReceiver(
    private val similarMoviesActivityViewModel: SimilarMoviesActivityViewModel,
    private val similarMoviesActivity: SimilarMoviesActivity, private val movieID: String
) : BroadcastReceiver() {

    private var lockVariable: Boolean = false
    private var moviesListItem: MoviesListItem? = null

    fun setLockVariable(lockVariable: Boolean) {
        this.lockVariable = lockVariable
    }

    fun setMoviesListItem(moviesListItem: MoviesListItem?) {
        this.moviesListItem = moviesListItem!!
    }

    override fun onReceive(context: Context, intent: Intent?) {
        val conn = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = conn.activeNetworkInfo

        //multiple if checks to detect which view to show and what work to do if needed (e.g. refreshing data)
        if (networkInfo != null && (networkInfo.type == ConnectivityManager.TYPE_WIFI || networkInfo.type == ConnectivityManager.TYPE_MOBILE)) {
            if (moviesListItem != null) {
                if (moviesListItem!!.moviesItemList!!.isEmpty()) {
                    similarMoviesActivity.findViewById<View>(R.id.SimilarMoviesNoInternetLinearLayout).visibility =
                        View.GONE
                } else if (moviesListItem!!.moviesItemList!!.isEmpty() && lockVariable) {
                    similarMoviesActivity.findViewById<View>(R.id.SimilarMoviesProgressBar).visibility =
                        View.VISIBLE
                    similarMoviesActivityViewModel.getSimilarMovies(movieID, api_key)
                } else {
                    similarMoviesActivity.findViewById<View>(R.id.SimilarMoviesNoInternetLinearLayout2).visibility =
                        View.GONE
                }
            } else {
                if (lockVariable) {
                    similarMoviesActivity.findViewById<View>(R.id.SimilarMoviesProgressBar).visibility =
                        View.VISIBLE
                    similarMoviesActivityViewModel.getSimilarMovies(movieID, api_key)
                }
                similarMoviesActivity.findViewById<View>(R.id.SimilarMoviesNoInternetLinearLayout).visibility =
                    View.GONE
            }
        } else {
            if (moviesListItem != null) {
                if (moviesListItem!!.moviesItemList!!.isEmpty()) {
                    similarMoviesActivity.findViewById<View>(R.id.SimilarMoviesNoInternetLinearLayout).visibility =
                        View.VISIBLE
                } else {
                    similarMoviesActivity.findViewById<View>(R.id.SimilarMoviesNoInternetLinearLayout2).visibility =
                        View.VISIBLE
                }
            } else {
                similarMoviesActivity.findViewById<View>(R.id.SimilarMoviesNoInternetLinearLayout).visibility =
                    View.VISIBLE
            }
        }
    }
}