package com.example.tennis

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tennis.databinding.CellDayBinding
import com.example.tennis.model.Reservation
import java.text.SimpleDateFormat
import java.util.*

class CalendarAdapter(val context: Context, val clickListener: (Reservation) -> Unit): RecyclerView.Adapter<CalendarAdapter.CellDayViewHolder>() {
    val firstDayIndex: Int

    init {
        val calendar: Calendar = Calendar.getInstance()
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
        val date = Date(Date().getTime() + (position * 1000 * 60 * 60 * 24))

        val dfHour = SimpleDateFormat("HH", Locale.FRANCE)
        val dfMonth = SimpleDateFormat("MM", Locale.FRANCE)
        val dfYear = SimpleDateFormat("YYYY", Locale.FRANCE)
        val dfDay = SimpleDateFormat("dd", Locale.FRANCE)
        val year = dfYear.format(date)
        val month = dfMonth.format(date)
        val hour = dfHour.format(date)
        val id = UUID.randomUUID().toString()
        val day = dfDay.format(date)

        holder.dayTitle.text = days[(position + firstDayIndex) % days.count()]
        holder.terrain1.layoutManager = GridLayoutManager(context, 5)
        holder.terrain1.adapter = TerrainAdapter() {

            val reservation = Reservation(id, day,hour.toInt() ,month ,year , "1", "")
            clickListener(reservation)
        }

        holder.terrain2.layoutManager = GridLayoutManager(context, 5)
        holder.terrain2.adapter = TerrainAdapter() {
            val reservation = Reservation(id, day,hour.toInt() ,month ,year , "2", "")
            clickListener(reservation)
        }
    }
}