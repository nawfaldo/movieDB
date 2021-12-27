package com.example.nbsmovie.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.example.nbsmovie.R
import com.example.nbsmovie.data.api.api.POSTER_BASE_URL
import com.example.nbsmovie.data.api.api.TheMovieDBClient
import com.example.nbsmovie.data.api.api.TheMovieDBInterface
import com.example.nbsmovie.data.api.repository.NetworkState
import com.example.nbsmovie.data.api.vo.MovieDetails
import com.example.nbsmovie.data.firebase.firestore.FirestoreClass
import com.example.nbsmovie.data.firebase.model.Favorite
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_movie_detail.*

class MovieDetailActivity : AppCompatActivity() {
    private lateinit var viewModel: MovieDetailViewModel
    private lateinit var movieDetailRepository: MovieDetailRepository
    private lateinit var mMovieDetails: MovieDetails

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)

        val movieId: Int = intent.getIntExtra("id",1)

        val apiService : TheMovieDBInterface = TheMovieDBClient.getClient()
        movieDetailRepository = MovieDetailRepository(apiService)

        viewModel = getViewModel(movieId)

        viewModel.movieDetails.observe(this, Observer {
            bindUI(it)
        })

        viewModel.networkState.observe(this, Observer {
            progress_bar.visibility = if (it == NetworkState.LOADING) View.VISIBLE else View.GONE
            txt_error.visibility = if (it == NetworkState.ERROR) View.VISIBLE else View.GONE
        })

        iv_movie_back.setOnClickListener {
            finish()
        }

        var isFavorite = false

        ll_detail_add.setOnClickListener {
            if(!isFavorite) {
                showErrorSnackBar("Movie Have Been Updated to Favorite", false)
                tv_detail_isFavoriteText.text = "Remove From Favorite"
                isFavorite = true
            } else {
                showErrorSnackBar("Movie Have Been Removed From Favorite", true)
                tv_detail_isFavoriteText.text = "Add to Favorite"
                isFavorite = false
            }
        }
    }



    private fun addToFavorite() {
        val addToFavorite = Favorite(
            mMovieDetails.id,
            mMovieDetails.title,
            mMovieDetails.overview,
            POSTER_BASE_URL + mMovieDetails.posterPath,
            mMovieDetails.releaseDate
        )

        FirestoreClass().addFavoriteItems(this@MovieDetailActivity, addToFavorite)
    }

    fun addToFavoriteSuccess() {
        Toast.makeText(
            this@MovieDetailActivity,
            resources.getString(R.string.success_message_item_added_to_favorite),
            Toast.LENGTH_SHORT
        ).show()
    }

    fun showErrorSnackBar(message: String, errorMessage: Boolean) {
        val snackBar =
            Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
        val snackBarView = snackBar.view

        if (errorMessage) {
            snackBarView.setBackgroundColor(
                ContextCompat.getColor(
                    this@MovieDetailActivity,
                    R.color.colorSnackBarError
                )
            )
        }else{
            snackBarView.setBackgroundColor(
                ContextCompat.getColor(
                    this@MovieDetailActivity,
                    R.color.colorSnackBarSuccess
                )
            )
        }
        snackBar.show()
    }

    fun bindUI(it: MovieDetails) {
        tv_detail_title.text = it.title
//        tv_detail_tagline.text = it.genreIds.toString()
        tv_detail_overview.text = it.overview

        val moviePosterURL: String = POSTER_BASE_URL + it.posterPath
        Glide.with(this)
            .load(moviePosterURL)
            .into(iv_detail_poster)
    }

    private fun getViewModel(movieId: Int) : MovieDetailViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return MovieDetailViewModel(movieDetailRepository,movieId) as T
            }
        })[MovieDetailViewModel::class.java]
    }
}