package com.example.tennis

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class DataBaseHelper {
    companion object {
        val url = Firebase.database("https://tennisfirebase-edd01-default-rtdb.europe-west1.firebasedatabase.app/").reference
    }
}