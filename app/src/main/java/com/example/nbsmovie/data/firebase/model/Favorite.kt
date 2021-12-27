package com.example.nbsmovie.data.firebase.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Favorite(
    var id: Int,
    val title: String,
    val overview: String,
    val posterPath: String,
    val releaseDate: String
) : Parcelable