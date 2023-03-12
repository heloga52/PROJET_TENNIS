package com.example.tennis

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.example.tennis.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.buttonAdmin.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
        binding.logInButton.setOnClickListener{
            signIn(binding.emailLogin.text.toString(), binding.password.text.toString())
        }
    }


    private fun signIn(email: String, password: String) {
        if(email.isNotEmpty() && password.isNotEmpty()) {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Log.d(ContentValues.TAG, "signInWithEmail:success")
                        val user = auth.currentUser
                        updateUI(user)
                    } else {
                        Log.w(ContentValues.TAG, "signInWithEmail:failure", task.exception)
                        Snackbar.make(binding.root, "Authentication failed.", Snackbar.LENGTH_SHORT)
                            .show()
                        updateUI(null)
                    }
                }
        }else{
            Snackbar.make(binding.root, "Please fill all the fields", Snackbar.LENGTH_SHORT).show()
        }

    }
    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            Snackbar.make(binding.root, "User connected", Snackbar.LENGTH_SHORT).show()
            startActivity(Intent(this, TimeTable::class.java))
        }
    }

}