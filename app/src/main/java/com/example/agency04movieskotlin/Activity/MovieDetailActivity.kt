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
import com.bumptech.glide.Glide
import com.example.agency04movieskotlin.BroadcastReceivers.MovieDetailActivityBroadcastReceiver
import com.example.agency04movieskotlin.Constants.Companion.SELECTED_MOVIE_ID
import com.example.agency04movieskotlin.Constants.Companion.SELECTED_MOVIE_TITLE
import com.example.agency04movieskotlin.Constants.Companion.TAG
import com.example.agency04movieskotlin.Constants.Companion.image_base_url
import com.example.agency04movieskotlin.Models.MoviesItemGenreIDs
import com.example.agency04movieskotlin.R
import com.example.agency04movieskotlin.Viewmodel.MovieDetailActivityViewModel
import com.example.agency04movieskotlin.databinding.ActivityMovieDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMovieDetailBinding
    private lateinit var receiver: MovieDetailActivityBroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val toolbar = binding.MovieDetailAppBar
        setSupportActionBar(toolbar)
        binding.MovieDetailsAppBarTextView.text = SELECTED_MOVIE_TITLE
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        binding.MovieDetailProgressBar.visibility = View.VISIBLE
        binding.MovieDetailCardView.visibility = View.GONE

        val movieDetailActivityViewModel: MovieDetailActivityViewModel by viewModels()

        movieDetailActivityViewModel.getThrowableLiveData().observe(this) { throwable ->
            binding.MovieDetailProgressBar.visibility = View.GONE
            val builder =
                AlertDialog.Builder(this)
            builder.setMessage(getString(R.string.errorDescription))
            builder.setTitle(getString(R.string.errorTitle))
            builder.setPositiveButton(
                getString(R.string.dismiss)
            ) { dialogInterface: DialogInterface, i: Int -> dialogInterface.dismiss() }
            val dialog = builder.create()
            dialog.show()
            Log.d(TAG, "handleErrorGetMovieDetailFromServer: $throwable")
        }

        movieDetailActivityViewModel.getMoviesItemLiveData().observe(this) { moviesItem ->
            receiver.setMoviesItem(moviesItem)
            val moviesItemGenreIDsList: List<MoviesItemGenreIDs>? =
                moviesItem.genres
            Glide.with(this).load(image_base_url + moviesItem.backdrop_path)
                .placeholder(R.drawable.ic_no_image_available).centerCrop()
                .into(binding.BackdropImageView)
            Glide.with(this).load(image_base_url + moviesItem.poster_path)
                .placeholder(R.drawable.ic_no_image_available).centerCrop()
                .into(binding.PosterImageView)
            binding.TitleTextView.text = getString(R.string.title) + " " + moviesItem.title
            binding.ReleaseDateTextView.text =
                getString(R.string.releaseDate) + " " + moviesItem.release_date
            binding.OverviewText.text = moviesItem.overview
            val genres =
                StringBuilder(getString(R.string.genres) + " ")
            if (moviesItemGenreIDsList != null) {
                for (i in moviesItemGenreIDsList.indices) {
                    if (i != moviesItemGenreIDsList.size - 1) {
                        genres.append(moviesItemGenreIDsList[i].name).append(", ")
                    } else {
                        genres.append(moviesItemGenreIDsList[i].name)
                    }
                }
            }
            binding.GenresTextView.text = genres.toString()
            binding.MovieDetailsAppBarTextView.text = moviesItem.title
            binding.MovieDetailProgressBar.visibility = View.GONE
            binding.MovieDetailCardView.visibility = View.VISIBLE
        }

        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        receiver =
            MovieDetailActivityBroadcastReceiver(
                movieDetailActivityViewModel,
                SELECTED_MOVIE_ID,
                this
            )
        this.registerReceiver(receiver, filter)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.activity_movie_detail_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.ShowSimilarMoviesMenuButton) {
            val intent = Intent(this, SimilarMoviesActivity::class.java)
            startActivity(intent)
            return true
        } else if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }
}