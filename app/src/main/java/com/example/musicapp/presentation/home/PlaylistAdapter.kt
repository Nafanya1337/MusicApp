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
import com.example.musicapp.databinding.PlaylistHeaderBinding
import com.example.musicapp.databinding.PlaylistLayoutBinding
import com.example.musicapp.domain.models.home.RadioVO

class PlaylistAdapter(var playlistList: List<RadioVO>, val clickable: Clickable) :
    RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder>() {

    inner class PlaylistViewHolder(val binding: PlaylistLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val binding = PlaylistLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PlaylistViewHolder(binding)
    }

    interface Clickable {
        fun onItemClicked(radio: RadioVO)
    }

    override fun getItemCount(): Int = playlistList.size

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        val playlist = playlistList[position]
        if (playlist.contributor != null) {
            holder.binding.playlistArtist.text =
                playlist.contributor!!.joinToString(separator = ", ") { it.name }
        } else {
            holder.binding.playlistArtist.visibility = View.GONE
        }
        holder.binding.playlistName.text = playlist.title
        Glide.with(holder.itemView)
            .load(playlist.picture)
            .transform(RoundedCornersTransformation(20))
            .into(holder.binding.playlistImage)

        holder.itemView.setOnClickListener {
            clickable.onItemClicked(playlist)
        }

    }

}