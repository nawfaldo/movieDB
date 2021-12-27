package com.example.nbsmovie.data.api.vo


import com.google.gson.annotations.SerializedName

data class UpcomingMovieDetails(
    val id: Int,
    val title: String,
    @SerializedName("poster_path")
    val posterPath: String,
    @SerializedName("release_date")
    val releaseDate: String,
    val overview: String,
)