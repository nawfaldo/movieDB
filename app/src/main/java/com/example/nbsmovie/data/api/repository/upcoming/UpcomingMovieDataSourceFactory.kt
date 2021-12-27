package com.example.nbsmovie.data.api.repository.upcoming

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.nbsmovie.data.api.api.TheMovieDBInterface
import com.example.nbsmovie.data.api.vo.Movie
import io.reactivex.disposables.CompositeDisposable

class UpcomingMovieDataSourceFactory(private val apiService : TheMovieDBInterface, private val compositeDisposable: CompositeDisposable)
    : DataSource.Factory<Int, Movie>() {
    val upcomingMoviesLiveDataSource =  MutableLiveData<UpcomingMovieDataSource>()

    override fun create(): DataSource<Int, Movie> {
        val upcomingMovieDataSource = UpcomingMovieDataSource(apiService,compositeDisposable)

        upcomingMoviesLiveDataSource.postValue(upcomingMovieDataSource)
        return upcomingMovieDataSource
    }
}