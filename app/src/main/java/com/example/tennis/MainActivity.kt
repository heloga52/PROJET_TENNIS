package com.example.tennis

<<<<<<< Updated upstream
import android.annotation.SuppressLint
=======
>>>>>>> Stashed changes
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class MainActivity : AppCompatActivity() {
<<<<<<< Updated upstream
   /*private var database = FirebaseDatabase.getInstance()
=======
    /*private var database = FirebaseDatabase.getInstance()
>>>>>>> Stashed changes
    var myRef = database.getReference("message")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        myRef.setValue("Hello, World!");*/

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mAuth = FirebaseAuth.getInstance()

        // Récupération du bouton de déconnexion
        val signOutButton: Button = findViewById(R.id.logout_button)
        signOutButton.setOnClickListener {
            // Déconnexion de l'utilisateur
            mAuth.signOut()
            // Redirection vers la page de login
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Code existant
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("message")
        myRef.setValue("Hello, test");

    }
}