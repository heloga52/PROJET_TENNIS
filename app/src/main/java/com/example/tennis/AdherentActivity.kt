package com.example.tennis

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tennis.databinding.ActivityAdherentBinding
import com.example.tennis.model.Reservation

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class AdherentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdherentBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var reservationAdapter: ReservationAdapter
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialise la vue grâce au binding
        binding = ActivityAdherentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialise Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Initialise Firebase Realtime Database
        database = FirebaseDatabase.getInstance().reference

        // Initialise le RecyclerView pour afficher les réservations
        recyclerView = binding.cellReservation
        recyclerView.layoutManager = LinearLayoutManager(this)


        // Initialise l'adaptateur pour le RecyclerView
        reservationAdapter = ReservationAdapter(emptyList()) { onReservationClick(it) }
        recyclerView.adapter = reservationAdapter

        // Récupère l'UID de l'utilisateur actuel depuis Firebase Auth
        val user = auth.currentUser?.uid
        Log.d("AdherentActivity", "Uid de l'utilisateur : $user")
        if (user != null) {

            // Récupère les réservations de l'utilisateur actuel depuis Firebase Realtime Database
            database.child("reservations")
                .orderByChild("user")
                .equalTo(user)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val reservations = mutableListOf<Reservation>()
                        for (reservationSnapshot in snapshot.children) {
                            val reservation = reservationSnapshot.getValue(Reservation::class.java)
                            if (reservation != null) {
                                reservation.id = reservationSnapshot.key!!
                                reservations.add(reservation)
                            }
                        }
                        Log.d("AdherentActivity", "Nombre de réservations : ${reservations.size}")
                        Log.d("AdherentActivity", "Détails de la réservation : $reservations")
                        reservationAdapter.setReservations(reservations)
                        reservationAdapter.notifyDataSetChanged()

                        binding.cellReservation.visibility = if (reservations.isEmpty()) View.GONE else View.VISIBLE
                        binding.noReservationsTextView.visibility = if (reservations.isEmpty()) View.VISIBLE else View.GONE
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(this@AdherentActivity, "Erreur lors de la récupération des réservations", Toast.LENGTH_SHORT).show()
                    }
                })
        } else {
            Toast.makeText(this, "Utilisateur non identifié", Toast.LENGTH_SHORT).show()
        }

        // Initialise le bouton de déconnexion
        binding.logoutButton.setOnClickListener {
            auth.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        // Initialise le bouton du calendrier
        binding.calendarButton.setOnClickListener {
            val intent = Intent(this, CalendarActivity::class.java)
            startActivity(intent)
        }
    }

    // Action réalisée lorsqu'on clique sur une réservation dans le RecyclerView
    private fun onReservationClick(reservation: Reservation) {
        Toast.makeText(this, "Clique sur la réservation ${reservation.id}", Toast.LENGTH_SHORT).show()
    }
}
