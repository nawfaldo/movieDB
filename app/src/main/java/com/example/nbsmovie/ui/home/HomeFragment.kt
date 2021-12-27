package com.example.nbsmovie.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.nbsmovie.R
import com.example.nbsmovie.data.api.api.TheMovieDBClient
import com.example.nbsmovie.data.api.api.TheMovieDBInterface
import com.example.nbsmovie.data.api.repository.NetworkState
import com.example.nbsmovie.ui.home.banner.HomeViewPager
import com.example.nbsmovie.ui.home.banner.HomeViewPagerAdapter
import com.example.nbsmovie.ui.home.popular.HomePopularListAdapter
import com.example.nbsmovie.ui.home.upcoming.HomeUpcomingListAdapter
import com.example.nbsmovie.ui.home.upcoming.HomeUpcomingMoviePagedListRepository
import com.example.nbsmovie.ui.home.upcoming.HomeUpcomingMovieViewModel
import com.example.nbsmovie.ui.popular.MoviePagedListRepository
import com.example.nbsmovie.ui.popular.PopularListAdapter
import com.example.nbsmovie.ui.popular.PopularViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_popular.*

class HomeFragment : Fragment() {
    private lateinit var viewModel: PopularViewModel
    lateinit var movieRepository: MoviePagedListRepository

    private lateinit var upcomingMovieViewModel: HomeUpcomingMovieViewModel
    lateinit var upcomingMovieRepository: HomeUpcomingMoviePagedListRepository

    private lateinit var vpHome: ViewPager2
    private lateinit var llSliderDots: LinearLayout

    private val listHome: ArrayList<HomeViewPager> = arrayListOf()

    private lateinit var sliderDots: Array<ImageView?>

    private val slidingCallBack = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            for (dots in 0 until listHome.size) {
                sliderDots[dots]?.setImageDrawable(
                    context?.let { ContextCompat.getDrawable(it, R. drawable.non_active_dot) }
                )
            }
            sliderDots[position]?.setImageDrawable(
                context?.let { ContextCompat.getDrawable(it, R. drawable.active_dot) }
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initData()
        initView()
        setupViewPager()
    }

    override fun onResume() {
        super.onResume()

        showHomePopularList()
        showHomeUpcomingList()
    }

    private fun showHomeUpcomingList() {
        val apiService : TheMovieDBInterface = TheMovieDBClient.getClient()
        upcomingMovieRepository = HomeUpcomingMoviePagedListRepository(apiService)
        upcomingMovieViewModel = getUpcomingMovieViewModel()

        val adapter = this.context?.let { HomeUpcomingListAdapter(it) }

        rv_home_upcoming.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL ,false)
//        rv_home_popular.setNestedScrollingEnabled(false);
        rv_home_upcoming.setHasFixedSize(true)
        rv_home_upcoming.adapter = adapter

        upcomingMovieViewModel.upcomingMoviePagedList.observe(this, Observer {
            adapter?.submitList(it)
        })

        viewModel.networkState.observe(this, Observer {
            pb_home.visibility = if (viewModel.listIsEmpty() && it == NetworkState.LOADING) View.VISIBLE else View.GONE
            tv_home_error.visibility = if (viewModel.listIsEmpty() && it == NetworkState.ERROR) View.VISIBLE else View.GONE

            if (!viewModel.listIsEmpty()) {
                adapter?.setNetworkState(it)
            }
        })
    }

    private fun getUpcomingMovieViewModel(): HomeUpcomingMovieViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return HomeUpcomingMovieViewModel(upcomingMovieRepository) as T
            }
        })[HomeUpcomingMovieViewModel::class.java]
    }

    private fun showHomePopularList() {
        val apiService : TheMovieDBInterface = TheMovieDBClient.getClient()
        movieRepository = MoviePagedListRepository(apiService)
        viewModel = getViewModel()

        val adapter = this.context?.let { HomePopularListAdapter(it) }

        rv_home_popular.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL ,false)
//        rv_home_popular.setNestedScrollingEnabled(false);
        rv_home_popular.setHasFixedSize(true)
        rv_home_popular.adapter = adapter

        viewModel.moviePagedList.observe(this, Observer {
            adapter?.submitList(it)
        })

        viewModel.networkState.observe(this, Observer {
            pb_home.visibility = if (viewModel.listIsEmpty() && it == NetworkState.LOADING) View.VISIBLE else View.GONE
            tv_home_error.visibility = if (viewModel.listIsEmpty() && it == NetworkState.ERROR) View.VISIBLE else View.GONE

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

    private fun initView() {
        vpHome = requireView().findViewById(R.id.vp_home)
        llSliderDots = requireView().findViewById(R.id.ll_slider_dots)
    }

    private fun setupViewPager() {
        vpHome.adapter = HomeViewPagerAdapter(listHome)
        vpHome.registerOnPageChangeCallback(slidingCallBack)

        sliderDots = arrayOfNulls(listHome.size)

        sliderDots[0]?.setImageDrawable(
            context?.let { ContextCompat.getDrawable(it, R. drawable.active_dot) }
        )

        for (i in 0 until listHome.size) {
            sliderDots[i] = ImageView(context)
            sliderDots[i]?.setImageDrawable(
                context?.let { ContextCompat.getDrawable(it, R. drawable.non_active_dot) }
            )

            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(8, 0, 8, 0)
            llSliderDots.addView(sliderDots[i], params)
        }
    }

    private fun initData() {
        val titleArtikel = resources.getStringArray(R.array.arr_title_artikel)
        val imgArtikel = resources.obtainTypedArray(R.array.arr_img_artikel)

        listHome.clear()
        for (data in titleArtikel.indices) {
            val home = HomeViewPager(
                titleArtikel[data],
                imgArtikel.getResourceId(data, 0)
            )
            listHome.add(home)
        }
        imgArtikel.recycle()
    }
}