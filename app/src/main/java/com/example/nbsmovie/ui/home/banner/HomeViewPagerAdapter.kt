package com.example.nbsmovie.ui.home.banner

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.nbsmovie.R

class HomeViewPagerAdapter(
    private val listHomeViewPager: ArrayList<HomeViewPager>
) : RecyclerView.Adapter<HomeViewPagerAdapter.MyViewHolder>() {
    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgArtikel: ImageView = view.findViewById(R.id.iv_home_viewpager)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MyViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_home_viewpager, parent, false)
    )

    override fun getItemCount() = listHomeViewPager.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.imgArtikel.setImageResource(listHomeViewPager[position].image)
    }
}