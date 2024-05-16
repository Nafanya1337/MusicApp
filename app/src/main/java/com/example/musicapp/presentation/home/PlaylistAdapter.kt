package com.example.musicapp.presentation.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.musicapp.R
import com.example.musicapp.data.utils.RoundedCornersTransformation
import com.example.musicapp.domain.models.home.RadioVO

class PlaylistAdapter(var playlistList: List<RadioVO>, val clickable: Clickable) :
    RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder>() {

    inner class PlaylistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val playlistImage = itemView.findViewById<ImageView>(R.id.playlistImage)
        val playlistName = itemView.findViewById<TextView>(R.id.playlistName)
        val playlistArtist = itemView.findViewById<TextView>(R.id.playlistArtist)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val item = LayoutInflater.from(parent.context.applicationContext)
            .inflate(R.layout.playlist_layout, parent, false)
        return PlaylistViewHolder(item)
    }

    interface Clickable {
        fun onItemClicked(radio: RadioVO)
    }

    override fun getItemCount(): Int = playlistList.size

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        val playlist = playlistList[position]
        if (playlist.contributor != null) {
            holder.playlistArtist.text =
                playlist.contributor!!.joinToString(separator = ", ") { it.name }
        } else {
            holder.playlistArtist.visibility = View.GONE
        }
        holder.playlistName.text = playlist.title
        Glide.with(holder.itemView)
            .load(playlist.picture)
            .transform(RoundedCornersTransformation(20))
            .into(holder.playlistImage)

        holder.itemView.setOnClickListener {
            clickable.onItemClicked(playlist)
        }

    }

}