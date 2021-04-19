package com.example.agency04movieskotlin.Activity

import android.content.DialogInterface
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.agency04movieskotlin.BroadcastReceivers.MainActivityBroadcastReceiver
import com.example.agency04movieskotlin.Constants
import com.example.agency04movieskotlin.R
import com.example.agency04movieskotlin.ViewPagerAdapter.ViewPagerAdapter
import com.example.agency04movieskotlin.Viewmodel.MainActivityViewModel
import com.example.agency04movieskotlin.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewPager: ViewPager2
    private lateinit var pagerAdapter: ViewPagerAdapter
    private lateinit var tabLayout: TabLayout
    private lateinit var receiver: MainActivityBroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val toolbar = binding.MainAppBar
        setSupportActionBar(toolbar)
        Objects.requireNonNull(supportActionBar)!!.title = getString(R.string.movies)

        viewPager = binding.MainViewPager
        tabLayout = binding.MainTabLayout
        binding.MainActivityProgressBar.visibility = View.VISIBLE

        val mainActivityViewModel: MainActivityViewModel by viewModels()

        mainActivityViewModel.getThrowableLiveData().observe(this, { throwable ->
            receiver.setLockVariable(true)
            binding.MainActivityProgressBar.visibility = View.GONE
            val builder = AlertDialog.Builder(this)
            builder.setMessage(getString(R.string.errorDescription))
            builder.setTitle(getString(R.string.errorTitle))
            builder.setPositiveButton(
                getString(R.string.dismiss)
            ) { dialogInterface: DialogInterface, i: Int -> dialogInterface.dismiss() }
            val dialog = builder.create()
            dialog.show()
            Log.d(Constants.TAG, "handleErrorGetListFromServer: error $throwable")
        })

        mainActivityViewModel.getFragments().observe(this, { fragments ->
            receiver.setFragmentArrayList(fragments)
            if (fragments.size != 0) {
                receiver.setLockVariable(true)
            }
            pagerAdapter = ViewPagerAdapter(this, fragments)
            viewPager.adapter = pagerAdapter
            binding.MainActivityProgressBar.visibility = View.GONE
            TabLayoutMediator(
                tabLayout, viewPager
            ) { tab: TabLayout.Tab, position: Int ->
                when (position) {
                    0 -> tab.text = getString(R.string.popular)
                    1 -> tab.text = getString(R.string.topRated)
                }
            }.attach()
        })

        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        receiver = MainActivityBroadcastReceiver(this, mainActivityViewModel)
        this.registerReceiver(receiver, filter)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }
}