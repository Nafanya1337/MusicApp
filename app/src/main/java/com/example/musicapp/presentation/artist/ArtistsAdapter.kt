package com.example.musicapp.presentation.artist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.musicapp.R
import com.example.musicapp.data.utils.RoundedCornersTransformation
import com.example.musicapp.databinding.ArtistRoundCircleHorizontalBinding
import com.example.musicapp.domain.models.tracks.ContributorsVO

class ArtistsAdapter(
    val artists: List<ContributorsVO>,
    private val clickable: Clickable
) : RecyclerView.Adapter<ArtistsAdapter.ArtistView>() {

    interface Clickable {
        fun onItemClick(artist: ContributorsVO)
    }

    inner class ArtistView(val binding: ArtistRoundCircleHorizontalBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistView {
        val binding = ArtistRoundCircleHorizontalBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ArtistView(binding)
    }

    override fun getItemCount(): Int {
        return artists.size
    }

    override fun onBindViewHolder(holder: ArtistView, position: Int) {
        val artist = artists[position]
        holder.binding.roundedArtistButton.text = artist.name

        holder.binding.roundedArtistButton.setOnClickListener {
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
            .into(holder.binding.roundedArtistImage)
    }

}