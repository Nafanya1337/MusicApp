package com.example.musicapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.musicapp.data.utils.RoundedCornersTransformation
import com.example.musicapp.domain.models.TrackListVO
import com.example.musicapp.domain.models.TrackVO

class ListTrackAdapter(var trackList: TrackListVO, val clickableImpl: Clickable) : RecyclerView.Adapter<ListTrackAdapter.TrackViewHolder>() {

    interface Clickable {
        fun onItemClick(trackList: TrackListVO, position: Int)
    }

    fun setTracklist(tracklist: List<TrackVO>) {
        this.trackList.list = tracklist
        notifyDataSetChanged()
    }

    inner class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val trackImage: ImageView = itemView.findViewById(R.id.trackImage)
        val trackName: TextView = itemView.findViewById(R.id.trackName)
        val artistName: TextView = itemView.findViewById(R.id.artistName)
        val explicitContentIcon: ImageView = itemView.findViewById(R.id.explicit_content_icon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.song_card_layout, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = trackList.list[position]
        // Здесь установите данные из объекта Track в соответствующие Views
        holder.trackName.text = track.title
        holder.artistName.text = track.artist.name
        // Загрузка изображения trackImage с помощью Picasso, Glide или другой библиотеки для загрузки изображений
        val imageUrl = track.album?.picture ?: "https://e-cdns-images.dzcdn.net/images/misc/235ec47f2b21c3c73e02fce66f56ccc5/500x500-000000-80-0-0.jpg"
        Glide
            .with(holder.itemView)
            .load(imageUrl)
            .transform(RoundedCornersTransformation(20))
            .into(holder.trackImage)
        if (!track.explicitLyrics) { holder.explicitContentIcon.visibility = View.GONE }
        if (holder.trackName.width >= 100)
            holder.trackName.isSelected = true
        holder.itemView.setOnClickListener{
            clickableImpl.onItemClick(trackList, position)
        }
    }

    override fun getItemCount(): Int {
        return trackList.list.size
    }
}

