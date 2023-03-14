package com.example.tennis

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth


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