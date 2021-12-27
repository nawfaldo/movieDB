package com.example.nbsmovie.data.firebase.firestore

import android.util.Log
import com.example.nbsmovie.data.firebase.model.Favorite
import com.example.nbsmovie.ui.detail.MovieDetailActivity
import com.example.nbsmovie.util.Constants
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class FirestoreClass {
    private val mFireStore = FirebaseFirestore.getInstance()

    fun addFavoriteItems(activity: MovieDetailActivity, addToFavorite: Favorite) {
        mFireStore.collection(Constants.FAVORITE_ITEMS)
            .document()
            .set(addToFavorite, SetOptions.merge())
            .addOnSuccessListener {
                activity.addToFavoriteSuccess()
            }
            .addOnFailureListener { e ->
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while creating the document for favorite item.",
                    e
                )
            }
    }
}