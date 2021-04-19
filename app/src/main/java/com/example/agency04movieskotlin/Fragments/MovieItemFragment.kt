package com.example.agency04movieskotlin.Fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.agency04movieskotlin.Activity.MovieDetailActivity
import com.example.agency04movieskotlin.Constants.Companion.SELECTED_MOVIE_ID
import com.example.agency04movieskotlin.Constants.Companion.SELECTED_MOVIE_TITLE
import com.example.agency04movieskotlin.Models.MoviesItem
import com.example.agency04movieskotlin.R
import com.example.agency04movieskotlin.RecyclerViewAdapter.FragmentMovieListAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieItemFragment : Fragment(), FragmentMovieListAdapter.OnItemClickListener {

    private lateinit var list: List<MoviesItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        list = requireArguments().getParcelableArrayList("MovieList")!!
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.movie_list_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = view.findViewById(R.id.MovieListFragmentRecyclerView)
        recyclerView.layoutManager = GridLayoutManager(requireActivity().applicationContext, 2)
        recyclerView.setHasFixedSize(true)

        val adapter = FragmentMovieListAdapter(requireActivity().applicationContext, list, this)
        recyclerView.adapter = adapter
    }

    override fun onItemClick(moviesItem: MoviesItem) {
        val intent = Intent(activity, MovieDetailActivity::class.java).apply {
            SELECTED_MOVIE_ID = moviesItem.id.toString()
            SELECTED_MOVIE_TITLE = moviesItem.title.toString()
        }
        startActivity(intent)
    }
}