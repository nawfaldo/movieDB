package com.example.nbsmovie.data.api.vo

import com.google.gson.annotations.SerializedName

data class MovieDetails(
    val id: Int,
    val title: String,
    @SerializedName("poster_path")
    val posterPath: String,
    @SerializedName("release_date")
    val releaseDate: String,
    val overview: String,
    val isFavorite: Int = 1
)
