package com.example.nbsmovie.ui.popular

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.example.nbsmovie.R
import com.example.nbsmovie.data.api.api.TheMovieDBClient
import com.example.nbsmovie.data.api.api.TheMovieDBInterface
import com.example.nbsmovie.data.api.repository.NetworkState
import kotlinx.android.synthetic.main.fragment_popular.*

class PopularFragment : Fragment() {
    private lateinit var viewModel: PopularViewModel
    lateinit var movieRepository: MoviePagedListRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_popular, container, false)
    }

    override fun onResume() {
        super.onResume()

        showPopularList()
    }

    private fun showPopularList() {
        val apiService : TheMovieDBInterface = TheMovieDBClient.getClient()
        movieRepository = MoviePagedListRepository(apiService)
        viewModel = getViewModel()

        val adapter = this.context?.let { PopularListAdapter(it) }

        rv_popular.layoutManager = GridLayoutManager(activity, 2)
        rv_popular.setHasFixedSize(true)
        rv_popular.adapter = adapter

        viewModel.moviePagedList.observe(this, Observer {
            adapter?.submitList(it)
        })

        viewModel.networkState.observe(this, Observer {
            pb_popular.visibility = if (viewModel.listIsEmpty() && it == NetworkState.LOADING) View.VISIBLE else View.GONE
            tv_popular_error.visibility = if (viewModel.listIsEmpty() && it == NetworkState.ERROR) View.VISIBLE else View.GONE

            if (!viewModel.listIsEmpty()) {
                adapter?.setNetworkState(it)
            }
        })
    }

    private fun getViewModel(): PopularViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return PopularViewModel(movieRepository) as T
            }
        })[PopularViewModel::class.java]
    }
}