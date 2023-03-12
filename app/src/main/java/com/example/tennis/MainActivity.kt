package com.example.tennis

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.database.FirebaseDatabase


class MainActivity : AppCompatActivity() {
    private var database = FirebaseDatabase.getInstance()
    var myRef = database.getReference("message")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        myRef.setValue("Hello, World!");

    }
}