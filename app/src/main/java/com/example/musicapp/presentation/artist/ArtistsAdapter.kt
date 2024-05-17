package com.example.musicapp.presentation.artist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.musicapp.R
import com.example.musicapp.data.utils.RoundedCornersTransformation
import com.example.musicapp.domain.models.ContributorsVO

class ArtistsAdapter(
    val artists: List<ContributorsVO>,
    private val clickable: Clickable
) : RecyclerView.Adapter<ArtistsAdapter.ArtistView>() {

    interface Clickable {
        fun onItemClick(artist: ContributorsVO)
    }

    inner class ArtistView(view: View) : RecyclerView.ViewHolder(view) {
        val image = view.findViewById<ImageView>(R.id.roundedArtistImage)
        val button = view.findViewById<Button>(R.id.roundedArtistButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistView {
        val item = LayoutInflater.from(parent.context.applicationContext)
            .inflate(R.layout.artist_round_circle_horizontal, parent, false)
        return ArtistView(item)
    }

    override fun getItemCount(): Int {
        return artists.size
    }

    override fun onBindViewHolder(holder: ArtistView, position: Int) {
        val artist = artists[position]
        holder.button.text = artist.name

        holder.button.setOnClickListener {
            clickable.onItemClick(artist)
        }

        holder.itemView.setBackgroundResource(
            if (artists.size > 1) {
                when (position) {
                    0 -> R.drawable.first_item_background
                    artists.size - 1 -> R.drawable.last_item_background
                    else -> R.drawable.middle_item_background
                }
            } else {
                R.drawable.single_item_bg
            }
        )

        Glide
            .with(holder.itemView)
            .load(artist.picture)
            .transform(RoundedCornersTransformation(80))
            .into(holder.image)
    }

}