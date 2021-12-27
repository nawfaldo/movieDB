package com.example.nbsmovie.ui.home.upcoming

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.example.nbsmovie.data.api.repository.NetworkState
import com.example.nbsmovie.data.api.vo.Movie
import io.reactivex.disposables.CompositeDisposable

class HomeUpcomingMovieViewModel(private val upcomingMovieRepository : HomeUpcomingMoviePagedListRepository) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    val  upcomingMoviePagedList : LiveData<PagedList<Movie>> by lazy {
        upcomingMovieRepository.fetchLiveUpcomingMoviePagedList(compositeDisposable)
    }

    val  networkState : LiveData<NetworkState> by lazy {
        upcomingMovieRepository.getNetworkState()
    }

    fun listIsEmpty(): Boolean {
        return upcomingMoviePagedList.value?.isEmpty() ?: true
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}