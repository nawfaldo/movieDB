package com.example.nbsmovie.ui.detail

import androidx.lifecycle.LiveData
import com.example.nbsmovie.data.api.api.TheMovieDBInterface
import com.example.nbsmovie.data.api.repository.Popular.MovieDetailsNetworkDataSource
import com.example.nbsmovie.data.api.repository.NetworkState
import com.example.nbsmovie.data.api.vo.MovieDetails
import io.reactivex.disposables.CompositeDisposable

class MovieDetailRepository(private val apiService: TheMovieDBInterface) {
    lateinit var movieDetailsNetworkDataSource: MovieDetailsNetworkDataSource

    fun fetchMovieDetail(compositeDisposable: CompositeDisposable, movieId: Int) : LiveData<MovieDetails> {
        movieDetailsNetworkDataSource = MovieDetailsNetworkDataSource(apiService, compositeDisposable)
        movieDetailsNetworkDataSource.fetchMovieDetails(movieId)

        return movieDetailsNetworkDataSource.downloadedMovieResponse
    }

    fun getMovieDetailsNetworkState(): LiveData<NetworkState> {
        return  movieDetailsNetworkDataSource.networkState
    }
}