package com.example.tennis

import android.content.Intent
import android.net.Uri.Builder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tennis.databinding.ActivityCalendarBinding
import com.example.tennis.model.Reservation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CalendarActivity : AppCompatActivity() {
    lateinit var binding: ActivityCalendarBinding

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val backButton = findViewById<Button>(R.id.btn_back)
        backButton.setOnClickListener {
            onBackPressed()
        }

        //binding.recyclerView.layoutManager = GridLayoutManager(this, 8)
        //binding.recyclerView.adapter = HourAdapter()
        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = CalendarAdapter(this) { reservation ->
            // test pour les horaires passées etc / tous les tests ici
            val builder = AlertDialog.Builder(this)

            builder.setTitle("Confirmation")
            builder.setMessage("${reservation.day}-${reservation.month}-${reservation.year} : ${reservation.hour}h; terrain : ${reservation.plot}")

            builder.setPositiveButton("Yes") { dialog, which ->
                if (checkIfUserAlreadyReservedTwoHours(reservation, firebaseAuth.currentUser)){
                    Toast.makeText(this, "Vous avez déjà réservé deux créneaux horaires pour cette journée", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "${reservation.day} ${reservation.hour}h terrain : ${reservation.plot}", Toast.LENGTH_LONG).show()
                    saveReservation(reservation, firebaseAuth.currentUser)
                }
            }
            builder.setNegativeButton("No") { dialog, which ->
                dialog.dismiss()
            }

            val dialog = builder.create()
            dialog.show()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    private fun checkIfUserAlreadyReservedTwoHours(reservation: Reservation, user: FirebaseUser?): Boolean {
        var isReserved = false
        val reservationsRef = database.child("reservations")
        val userId = user?.uid
        val date = "${reservation.day}-${reservation.month}-${reservation.year}"
        val query = reservationsRef.orderByChild("user_date").equalTo("${userId}_${date}")

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.childrenCount == 2L) {
                    isReserved = true
                }
            }

            override fun onCancelled(error: DatabaseError) {
                isReserved = true
            }
        })

        return isReserved
    }


    private fun saveReservation(reservation: Reservation, user: FirebaseUser?){
        val reservationRef = database.child("reservations")
        val user = user?.uid
        val id = reservation.id
        if (user != null){
            reservation.user = user
        }
        if (id != null) {
            reservationRef.child(id).setValue(reservation)
        }
    }
}
