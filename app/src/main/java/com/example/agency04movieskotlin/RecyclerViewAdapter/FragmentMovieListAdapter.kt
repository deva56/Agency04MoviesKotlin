package com.example.agency04movieskotlin.RecyclerViewAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.agency04movieskotlin.Constants.Companion.image_base_url
import com.example.agency04movieskotlin.Models.MoviesItem
import com.example.agency04movieskotlin.R

class FragmentMovieListAdapter(
    private val context: Context,
    private val records: List<MoviesItem>,
    private val listener: OnItemClickListener
) :
    RecyclerView.Adapter<FragmentMovieListAdapter.FragmentMovieListHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FragmentMovieListHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.movie_list_fragment_item, parent, false)
        return FragmentMovieListHolder(itemView)
    }

    inner class FragmentMovieListHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val headerText: TextView = itemView.findViewById(R.id.MovieListFragmentItemTitle)
        val imageView: ImageView = itemView.findViewById(R.id.MovieListFragmentItemImageView)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(records[position])
                }
            }
        }
    }

    override fun onBindViewHolder(holder: FragmentMovieListHolder, position: Int) {
        val currentRecord = records[position]
        holder.headerText.text = currentRecord.title
        Glide.with(context).load(image_base_url + currentRecord.poster_path)
            .placeholder(R.drawable.ic_no_image_available).centerCrop().into(holder.imageView)
    }

    override fun getItemCount(): Int {
        return records.size
    }

    interface OnItemClickListener {
        fun onItemClick(moviesItem: MoviesItem)
    }
}