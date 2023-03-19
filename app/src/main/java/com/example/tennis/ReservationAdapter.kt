package com.example.tennis

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tennis.databinding.CellReservationBinding
import com.example.tennis.model.Reservation


class ReservationAdapter(
    private var reservations: List<Reservation>,
    private val clickListener: (Reservation) -> Unit
) : RecyclerView.Adapter<ReservationAdapter.ReservationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservationViewHolder {
        val binding = CellReservationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReservationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReservationViewHolder, position: Int) {
        val reservation = reservations[position]
        holder.bind(reservation, clickListener)
    }

    override fun getItemCount() = reservations.size

    fun setReservations(newReservations: List<Reservation>) {
        reservations = newReservations
        notifyDataSetChanged()
    }

    class ReservationViewHolder(private val binding: CellReservationBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(reservation: Reservation, clickListener: (Reservation) -> Unit) {
            binding.reservation = reservation
            binding.root.setOnClickListener { clickListener(reservation) }
        }
    }
}
