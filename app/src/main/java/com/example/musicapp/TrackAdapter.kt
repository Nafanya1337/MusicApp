package com.example.musicapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.musicapp.data.remote.common.TrackItem

class TrackAdapter(var tracks: List<TrackItem>) : RecyclerView.Adapter<TrackAdapter.TrackViewHolder>() {

    inner class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val trackImage: ImageView = itemView.findViewById(R.id.trackImage)
        val trackName: TextView = itemView.findViewById(R.id.trackName)
        val artistName: TextView = itemView.findViewById(R.id.artistName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.song_card_layout, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = tracks[position]
        // Здесь установите данные из объекта Track в соответствующие Views
        holder.trackName.text = track.name
        holder.artistName.text = track.artist
        // Загрузка изображения trackImage с помощью Picasso, Glide или другой библиотеки для загрузки изображений
        val imageUrl = track.image[0].text
        Glide.with(holder.itemView).load(imageUrl).into(holder.trackImage)
    }

    public fun clearTrackList() {
        tracks = listOf()
    }

    override fun getItemCount(): Int {
        return tracks.size
    }
}

