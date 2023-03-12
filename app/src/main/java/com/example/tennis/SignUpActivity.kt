package com.example.tennis

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.databinding.DataBindingUtil.setContentView
import com.example.tennis.databinding.ActivitySignupBinding
import com.example.tennis.model.User
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.ktx.Firebase




class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private lateinit var auth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.buttonSignUp.setOnClickListener{
            signUp(binding.emailSignup.text.toString(), binding.createPassword.text.toString())
        }
    }

    private fun signUp(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                updateUI(auth.currentUser)
            } else {
                Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            val createName = binding.createName.text.toString()
            val numberPhone = binding.numberPhone.text.toString()
            if (createName.isNotEmpty() && binding.emailSignup.text.toString().isNotEmpty() && binding.createPassword.text.toString().isNotEmpty() && binding.numberPhone.text.toString().isNotEmpty()) {
                Firebase.database.getReference("users").child(user.uid).setValue(User(user.uid, createName, numberPhone))
                startActivity(Intent(this, TimeTable::class.java))
                Snackbar.make(binding.root, "User created", Snackbar.LENGTH_SHORT).show()
            }else{
                Snackbar.make(binding.root, "Please fill all the fields", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

}