package com.example.tennis

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tennis.databinding.CellTerrainBinding

class TerrainAdapter(val clickListener: (Int) -> Unit): RecyclerView.Adapter<TerrainAdapter.TerrainViewHolder>() {
    class TerrainViewHolder(binding: CellTerrainBinding): RecyclerView.ViewHolder(binding.root) {
        val button = binding.button
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TerrainViewHolder {
        val binding = CellTerrainBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TerrainViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return 15
    }

    override fun onBindViewHolder(holder: TerrainViewHolder, position: Int) {
        holder.button.text = "${position + 7}h"
        holder.button.setOnClickListener {
            clickListener(position)
        }
    }
}