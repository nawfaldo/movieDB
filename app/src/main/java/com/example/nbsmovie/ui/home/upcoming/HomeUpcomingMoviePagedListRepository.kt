package com.example.nbsmovie.ui.home.upcoming

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.nbsmovie.data.api.api.POST_PER_PAGE
import com.example.nbsmovie.data.api.api.TheMovieDBInterface
import com.example.nbsmovie.data.api.repository.NetworkState
import com.example.nbsmovie.data.api.repository.upcoming.UpcomingMovieDataSource
import com.example.nbsmovie.data.api.repository.upcoming.UpcomingMovieDataSourceFactory
import com.example.nbsmovie.data.api.vo.Movie
import io.reactivex.disposables.CompositeDisposable

class HomeUpcomingMoviePagedListRepository(private val apiService : TheMovieDBInterface) {
    lateinit var upcomingMoviePagedList: LiveData<PagedList<Movie>>
    lateinit var upcomingMoviesDataSourceFactory: UpcomingMovieDataSourceFactory

    fun fetchLiveUpcomingMoviePagedList (compositeDisposable: CompositeDisposable) : LiveData<PagedList<Movie>> {
        upcomingMoviesDataSourceFactory = UpcomingMovieDataSourceFactory(apiService, compositeDisposable)

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(POST_PER_PAGE)
            .build()

        upcomingMoviePagedList = LivePagedListBuilder(upcomingMoviesDataSourceFactory, config).build()

        return upcomingMoviePagedList
    }

    fun getNetworkState(): LiveData<NetworkState> {
        return Transformations.switchMap<UpcomingMovieDataSource, NetworkState>(
            upcomingMoviesDataSourceFactory.upcomingMoviesLiveDataSource, UpcomingMovieDataSource::networkState)
    }
}