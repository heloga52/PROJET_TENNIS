package com.example.tennis

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tennis.databinding.CellDayBinding
import com.example.tennis.model.Reservation
import java.text.SimpleDateFormat
import java.util.*

class CalendarAdapter(val context: Context, val clickListener: (Reservation) -> Unit): RecyclerView.Adapter<CalendarAdapter.CellDayViewHolder>() {
    val firstDayIndex: Int

    init {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 7)
        val day: Int = calendar.get(Calendar.DAY_OF_WEEK)
        firstDayIndex = day -1
    }

    private val days = listOf("Dimanche", "Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi")
    class CellDayViewHolder(binding: CellDayBinding) : RecyclerView.ViewHolder(binding.root) {
        val dayTitle = binding.textViewDayTitle
        val terrain1 = binding.terrainRecyclerView
        val terrain2 = binding.terrain2RecyclerView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CellDayViewHolder {
        val binding = CellDayBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CellDayViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return days.count()
    }

    override fun onBindViewHolder(holder: CellDayViewHolder, position: Int) {
        val df = SimpleDateFormat("yyyy-MM-dd", Locale.FRANCE)
        val calendar: Calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH, position - firstDayIndex)
        val datePlusSixHours = calendar.time
        val date = Date(datePlusSixHours.time)
        date.time = date.time + (position - firstDayIndex) * 24 * 60 * 60 * 1000


        holder.dayTitle.text = days[(position + firstDayIndex) % days.count()]
        holder.terrain1.layoutManager = GridLayoutManager(context, 5)
        holder.terrain1.adapter = TerrainAdapter() {
            val dfHour = SimpleDateFormat("HH", Locale.FRANCE)
            val dfMonth = SimpleDateFormat("MM", Locale.FRANCE)
            val dfYear = SimpleDateFormat("YYYY", Locale.FRANCE)
            val dfDay = SimpleDateFormat("dd", Locale.FRANCE)
            val year = dfYear.format(date)
            val month = dfMonth.format(date)
            val hour = (it + 7) % 24
            val id = UUID.randomUUID().toString()
            val day = dfDay.format(date)

            if (hour in 10..17) {
                Toast.makeText(context, "Les réservations sont impossibles de 10h à 18h.", Toast.LENGTH_LONG).show()
            } else {
                val reservation = Reservation(id, day, hour.toInt(), month, year, "1", "")
                clickListener(reservation)
            }
        }

        holder.terrain2.layoutManager = GridLayoutManager(context, 5)
        holder.terrain2.adapter = TerrainAdapter() {
            val dfHour = SimpleDateFormat("HH", Locale.FRANCE)
            val dfMonth = SimpleDateFormat("MM", Locale.FRANCE)
            val dfYear = SimpleDateFormat("YYYY", Locale.FRANCE)
            val dfDay = SimpleDateFormat("dd", Locale.FRANCE)
            val year = dfYear.format(date)
            val month = dfMonth.format(date)
            val hour = (it + 7) % 24
            val id = UUID.randomUUID().toString()
            val day = dfDay.format(date)
            if (hour in 10..17) {
                Toast.makeText(context, "Les réservations sont impossibles de 10h à 18h.", Toast.LENGTH_LONG).show()
            } else {
                val reservation = Reservation(id, day, hour.toInt(), month, year, "2", "")
                clickListener(reservation)
            }}
    }

}