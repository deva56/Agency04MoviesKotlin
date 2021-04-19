package com.example.agency04movieskotlin.Activity

import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.agency04movieskotlin.BroadcastReceivers.SimilarMoviesActivityBroadcastReceiver
import com.example.agency04movieskotlin.Constants.Companion.SELECTED_MOVIE_ID
import com.example.agency04movieskotlin.Constants.Companion.SELECTED_MOVIE_TITLE
import com.example.agency04movieskotlin.Constants.Companion.TAG
import com.example.agency04movieskotlin.Models.MoviesItem
import com.example.agency04movieskotlin.R
import com.example.agency04movieskotlin.RecyclerViewAdapter.FragmentMovieListAdapter
import com.example.agency04movieskotlin.Viewmodel.SimilarMoviesActivityViewModel
import com.example.agency04movieskotlin.databinding.ActivitySimilarMoviesBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SimilarMoviesActivity : AppCompatActivity(), FragmentMovieListAdapter.OnItemClickListener {

    private lateinit var binding: ActivitySimilarMoviesBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var receiver: SimilarMoviesActivityBroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySimilarMoviesBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val toolbar = binding.SimilarMoviesAppBar
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        recyclerView = binding.SimilarMoviesRecyclerView
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.setHasFixedSize(true)

        binding.SimilarMoviesAppBarTextView.text =
            getString(R.string.similarMovies) + " " + SELECTED_MOVIE_TITLE
        binding.SimilarMoviesProgressBar.visibility = View.VISIBLE

        val similarMoviesActivityViewModel: SimilarMoviesActivityViewModel by viewModels()

        similarMoviesActivityViewModel.getThrowableLiveData().observe(this, { throwable ->
            receiver.setLockVariable(true)
            binding.SimilarMoviesProgressBar.visibility = View.GONE
            val builder = AlertDialog.Builder(this)
            builder.setMessage(getString(R.string.errorDescription))
            builder.setTitle(getString(R.string.errorTitle))
            builder.setPositiveButton(
                getString(R.string.dismiss)
            ) { dialogInterface: DialogInterface, i: Int -> dialogInterface.dismiss() }
            val dialog = builder.create()
            dialog.show()
            Log.d(TAG, "handleErrorGetMovieDetailFromServer: $throwable")
        })

        similarMoviesActivityViewModel.getMoviesListItemLiveData()
            .observe(this, { moviesListItem ->
                receiver.setMoviesListItem(moviesListItem)
                receiver.setLockVariable(true)
                binding.SimilarMoviesProgressBar.visibility = View.GONE
                if (moviesListItem.moviesItemList!!.isNotEmpty()) {
                    binding.SimilarMoviesTextView.visibility = View.GONE
                    val adapter = FragmentMovieListAdapter(
                        applicationContext,
                        moviesListItem.moviesItemList,
                        this
                    )
                    recyclerView.adapter = adapter
                } else {
                    binding.SimilarMoviesTextView.visibility = View.VISIBLE
                }
            })

        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        receiver =
            SimilarMoviesActivityBroadcastReceiver(
                similarMoviesActivityViewModel,
                this,
                SELECTED_MOVIE_ID
            )
        this.registerReceiver(receiver, filter)
    }

    override fun onItemClick(moviesItem: MoviesItem) {
        val intent = Intent(applicationContext, MovieDetailActivity::class.java)
        SELECTED_MOVIE_ID = moviesItem.id.toString()
        SELECTED_MOVIE_TITLE = moviesItem.title.toString()
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.activity_similar_movies_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        } else if (item.itemId == R.id.BackToMainScreenMenuButton) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finishAffinity()
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }
}