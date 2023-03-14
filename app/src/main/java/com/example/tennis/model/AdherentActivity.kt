package com.example.tennis.model

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.tennis.LoginActivity
import com.example.tennis.R
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class AdherentActivity : AppCompatActivity() {


    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adherent)
        mAuth = FirebaseAuth.getInstance()

        // Récupération du bouton de déconnexion
        val signOutButton: Button = findViewById(R.id.logout_button)
        signOutButton.setOnClickListener {
            // Déconnexion de l'utilisateur
            mAuth.signOut()
            // Redirection vers la page de login
            val intent = Intent(this@AdherentActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}