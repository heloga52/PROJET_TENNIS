package com.example.tennis

import android.content.Intent
import android.net.Uri.Builder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tennis.databinding.ActivityCalendarBinding
import com.example.tennis.model.Reservation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class CalendarActivity : AppCompatActivity() {
    lateinit var binding: ActivityCalendarBinding

    private lateinit var firebaseAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //binding.recyclerView.layoutManager = GridLayoutManager(this, 8)
        //binding.recyclerView.adapter = HourAdapter()
        firebaseAuth = FirebaseAuth.getInstance()

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = CalendarAdapter(this) { reservation ->
            // test pour les horaires passées etc / tous les tests ici
            val builder = AlertDialog.Builder(this)

            builder.setTitle("Confirmation")
            builder.setMessage("${reservation.day}-${reservation.month}-${reservation.year} : ${reservation.hour}h; terrain : ${reservation.plot}")

            builder.setPositiveButton("Yes") { dialog, which ->
                Toast.makeText(this, "${reservation.day} ${reservation.hour}h terrain : ${reservation.plot}", Toast.LENGTH_LONG).show()
                saveReservation(reservation, firebaseAuth.currentUser)
            }
            builder.setNegativeButton("No") { dialog, which ->
                dialog.dismiss()
            }

            val dialog = builder.create()
            dialog.show()
        }
    }

    fun saveReservation(reservation: Reservation, user: FirebaseUser?){
        val database = DataBaseHelper.url.database
        val ReservationRef =  database.getReference("reservations")
        val user = user?.uid
        val id = reservation.id
        if (user != null){
            reservation.user = user
        }
        if (id != null) {
            ReservationRef.child(id).setValue(reservation)
        }
    }
}