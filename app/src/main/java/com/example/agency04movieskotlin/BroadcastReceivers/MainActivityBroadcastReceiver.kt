package com.example.agency04movieskotlin.BroadcastReceivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.view.View
import androidx.fragment.app.Fragment
import com.example.agency04movieskotlin.Activity.MainActivity
import com.example.agency04movieskotlin.Constants.Companion.api_key
import com.example.agency04movieskotlin.R
import com.example.agency04movieskotlin.Viewmodel.MainActivityViewModel
import java.util.*

class MainActivityBroadcastReceiver(
    private val mainActivity: MainActivity,
    private val mainActivityViewModel: MainActivityViewModel
) : BroadcastReceiver() {

    //lockVariable is responsible for deciding the outcome of no internet textViews that is what variation of it will show depending
    // if there is data in fragments or there is no data
    private var lockVariable = false
    private var fragmentArrayList = ArrayList<Fragment>()

    fun setLockVariable(lockVariable: Boolean) {
        this.lockVariable = lockVariable
    }

    fun setFragmentArrayList(fragmentArrayList: ArrayList<Fragment>) {
        this.fragmentArrayList = fragmentArrayList
    }

    override fun onReceive(context: Context, intent: Intent?) {
        val conn = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = conn.activeNetworkInfo
        if (networkInfo != null && (networkInfo.type == ConnectivityManager.TYPE_WIFI || networkInfo.type == ConnectivityManager.TYPE_MOBILE)) {
            if (fragmentArrayList.size == 0) {
                mainActivity.findViewById<View>(R.id.MainActivityNoInternetLinearLayout).visibility =
                    View.GONE
            } else {
                mainActivity.findViewById<View>(R.id.MainActivityNoInternetLinearLayout2).visibility =
                    View.GONE
            }
            if (fragmentArrayList.size == 0 && lockVariable) {
                mainActivity.findViewById<View>(R.id.MainActivityProgressBar).visibility =
                    View.VISIBLE
                mainActivityViewModel.getPopularMovies(api_key)
            }
        } else {
            if (fragmentArrayList.size == 0) {
                mainActivity.findViewById<View>(R.id.MainActivityNoInternetLinearLayout).visibility =
                    View.VISIBLE

            } else {
                mainActivity.findViewById<View>(R.id.MainActivityNoInternetLinearLayout2).visibility =
                    View.VISIBLE

            }
        }
    }
}