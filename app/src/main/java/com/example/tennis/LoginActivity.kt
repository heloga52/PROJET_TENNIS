package com.example.tennis

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.example.tennis.databinding.ActivityLoginBinding
import com.google.firebase.database.*
import com.google.firebase.FirebaseNetworkException

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var databaseRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        databaseRef = FirebaseDatabase.getInstance().reference

        binding.buttonAdmin.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        binding.logInButton.setOnClickListener {
            val email = binding.emailLogin.text.toString()
            val pass = binding.password.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty()) {
                firebaseAuth.signInWithEmailAndPassword(email, pass)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val currentUser = firebaseAuth.currentUser
                            val userRef = databaseRef.child("users").child(currentUser!!.uid)

                            userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(dataSnapshot: DataSnapshot) {
                                    val role = dataSnapshot.child("role").value
                                    if (role != null) {
                                        // Vérifier si l'utilisateur est admin
                                        val isAdmin = when (role) {
                                            is Boolean -> role
                                            is String -> role == "admin"
                                            is Map<*, *> -> role["isAdmin"] as? Boolean ?: false
                                            else -> false
                                        }
                                        if (isAdmin) {
                                            Log.d(ContentValues.TAG, "Redirecting to AdminActivity")
                                            val intent = Intent(this@LoginActivity, AdminActivity::class.java)
                                            startActivity(intent)
                                            finish()
                                        } else if (role == "adherent") {
                                            // Rediriger vers AdherentActivity si le rôle est "adherent"
                                            Log.d(ContentValues.TAG, "Redirecting to AdherentActivity")
                                            val intent = Intent(this@LoginActivity, AdherentActivity::class.java)
                                            intent.putExtra("user", currentUser!!.uid) // ajouter l'ID de l'utilisateur
                                            startActivity(intent)
                                            finish()
                                        } else {
                                            Log.d(ContentValues.TAG, "Redirecting to MainActivity")
                                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                            startActivity(intent)
                                            finish()
                                        }
                                    } else {
                                        Log.w(ContentValues.TAG, "Role data is null")
                                    }
                                }

                                override fun onCancelled(databaseError: DatabaseError) {
                                    Toast.makeText(
                                        this@LoginActivity,
                                        "Error: ${databaseError.message}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            })
                        } else {
                            Toast.makeText(this, task.exception.toString(), Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
            } else {
                Toast.makeText(this, "Empty Fields Are not Allowed !!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onStart() {
        super.onStart()

        if (firebaseAuth.currentUser != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
