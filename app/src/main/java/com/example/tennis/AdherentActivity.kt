package com.example.tennis

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.tennis.R.*
import com.example.tennis.databinding.ActivityAdherentBinding
import com.google.firebase.auth.FirebaseAuth


class AdherentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdherentBinding


    private lateinit var mAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityAdherentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mAuth = FirebaseAuth.getInstance()

        // Récupération du bouton de déconnexion
        val signOutButton: Button = findViewById(id.logout_button)
        signOutButton.setOnClickListener {
            // Déconnexion de l'utilisateur
            mAuth.signOut()
            // Redirection vers la page de login
            val intent = Intent(this@AdherentActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        val calendarButton: Button  = binding.calendarButton
        calendarButton.setOnClickListener {
            val intent = Intent(this, CalendarActivity::class.java)
            startActivity(intent)
        }
    }
}