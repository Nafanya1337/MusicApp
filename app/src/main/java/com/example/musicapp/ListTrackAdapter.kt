package com.example.musicapp


import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.musicapp.data.utils.RoundedCornersTransformation
import com.example.musicapp.databinding.PlaylistHeaderBinding
import com.example.musicapp.databinding.SongCardLayoutBinding
import com.example.musicapp.domain.models.Playlistable
import com.example.musicapp.domain.models.TrackListVO
import com.example.musicapp.domain.models.TrackVO
import com.example.musicapp.domain.models.home.RadioVO

class ListTrackAdapter(
    var title: String,
    val clickableImpl: Clickable,
    val isPlaylist: Boolean = false,
    val diffUtilCallback: DiffUtilCallback<Playlistable> = DiffUtilCallback(
        emptyList(),
        emptyList()
    ),
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var list: List<Playlistable> = emptyList()
        set(value) {
            diffUtilCallback.oldList = field
            diffUtilCallback.newList = value
            val result = DiffUtil.calculateDiff(diffUtilCallback)
            field = value
            result.dispatchUpdatesTo(this)
        }

    interface Clickable {
        fun onItemClick(trackList: TrackListVO, position: Int)
    }

    inner class TrackViewHolder(val binding: SongCardLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class HeaderViewHoler(val binding: PlaylistHeaderBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            PLAYLIST_VIEW -> {
                val binding = PlaylistHeaderBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                HeaderViewHoler(binding)
            }

            TRACK_VIEW -> {
                val binding = SongCardLayoutBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                TrackViewHolder(binding)
            }

            else -> throw ViewHolderNotFoundExepction()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (isPlaylist && position == PLAYLIST_VIEW) PLAYLIST_VIEW else TRACK_VIEW
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is TrackViewHolder) {
            val track = list[position]
            (track as? TrackVO)?.let {
                holder.binding.trackName.text = track.title
                holder.binding.artistName.text = track.artist.name
                val imageUrl = track.album?.picture
                    ?: "https://e-cdns-images.dzcdn.net/images/misc/235ec47f2b21c3c73e02fce66f56ccc5/500x500-000000-80-0-0.jpg"
                Glide
                    .with(holder.itemView)
                    .load(imageUrl)
                    .transform(RoundedCornersTransformation(20))
                    .into(holder.binding.trackImage)
                if (!track.explicitLyrics) {
                    holder.binding.explicitContentIcon.visibility = View.GONE
                }
                if (track.title.count() >= 75)
                    holder.binding.trackName.isSelected = true
                holder.itemView.setOnClickListener {
                    clickableImpl.onItemClick(TrackListVO(title, list), position)
                }
            }
        }

        if (holder is HeaderViewHoler) {
            val header = list[position]
            (header as? RadioVO)?.let {
                holder.binding.playlistArtistName.text =
                    it.contributor?.joinToString(", ") { it.name }
                holder.binding.playlistPlaylistName.text = it.title
                if (it.picture == "")
                    holder.binding.playlistImage.visibility = View.GONE
                else
                    holder.binding.playlistImage.visibility = View.VISIBLE
                Glide
                    .with(holder.itemView)
                    .load(it.picture)
                    .transform(RoundedCornersTransformation(20))
                    .into(holder.binding.playlistImage)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    companion object {
        const val PLAYLIST_VIEW = 0
        const val TRACK_VIEW = 1
    }
}

class ViewHolderNotFoundExepction : RuntimeException()

