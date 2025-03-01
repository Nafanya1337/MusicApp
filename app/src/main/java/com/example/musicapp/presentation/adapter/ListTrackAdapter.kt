package com.example.musicapp.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions.withCrossFade
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import com.example.musicapp.data.utils.RoundedCornersTransformation
import com.example.musicapp.databinding.PlaylistHeaderBinding
import com.example.musicapp.databinding.PlaylistLayoutBinding
import com.example.musicapp.databinding.SongCardLayoutBinding
import com.example.musicapp.domain.models.tracks.Playlistable
import com.example.musicapp.domain.models.tracks.TrackListVO
import com.example.musicapp.domain.models.tracks.TrackVO
import com.example.musicapp.domain.models.home.RadioVO
import com.example.musicapp.presentation.utils.DiffUtilCallback

class ListTrackAdapter(
    private val displayMode: DisplayMode,
    private val trackClickableImpl: TrackClickable? = null,
    private val playlistCardClickableImpl: PlaylistCardClickable? = null,
    private val diffUtilCallback: DiffUtilCallback<Playlistable> = DiffUtilCallback(
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

    interface TrackClickable {
        fun onItemClick(trackList: TrackListVO, position: Int)
    }

    interface PlaylistCardClickable {
        fun onItemClick(radio: RadioVO)
    }

    sealed class DisplayTypeViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            HEADER_VIEW -> {
                val binding = PlaylistHeaderBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                HeaderViewHoler(binding)
            }

            TRACKLIST_VIEW -> {
                val binding = SongCardLayoutBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                TrackViewHolder(binding)
            }

            CARD_PLAYLIST_VIEW -> {
                val binding = PlaylistLayoutBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                PlaylistCardViewHoled(binding)
            }

            else -> throw ViewHolderNotFoundExepction()
        }

    override fun getItemViewType(position: Int): Int = when (displayMode) {
        DisplayMode.FULL_PLAYLIST -> if (position == 0) HEADER_VIEW else TRACKLIST_VIEW
        DisplayMode.TRACK_LIST -> TRACKLIST_VIEW
        DisplayMode.PLAYLIST_CARDS -> CARD_PLAYLIST_VIEW
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
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .skipMemoryCache(false)
                    .transform(RoundedCornersTransformation(20))
                    .into(holder.binding.trackImage)

                if (!track.explicitLyrics) {
                    holder.binding.explicitContentIcon.visibility = View.GONE
                }
                if (track.title.count() >= 75)
                    holder.binding.trackName.isSelected = true

                var pos = position

                val tracklist = if (displayMode == DisplayMode.FULL_PLAYLIST) {
                    pos--
                    list.subList(1, list.size)
                } else list

                holder.itemView.setOnClickListener {
                    trackClickableImpl?.onItemClick(
                        TrackListVO(
                            "",
                            tracklist
                        ), pos
                    )
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

        if (holder is PlaylistCardViewHoled) {
            val playlistCard = list[position]
            (playlistCard as? RadioVO)?.let {
                holder.binding.playlistName.text =
                    it.title
                if (it.contributor != null) {
                    holder.binding.playlistArtist.text =
                        it.contributor!!.joinToString(separator = ", ") { it.name }
                } else {
                    holder.binding.playlistArtist.visibility = View.GONE
                }
                holder.binding.playlistImage

                Glide.with(holder.itemView)
                    .load(it.picture)
                    .transform(RoundedCornersTransformation(20))
                    .into(holder.binding.playlistImage)

                holder.itemView.setOnClickListener {
                    playlistCardClickableImpl?.onItemClick(playlistCard)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    private companion object {
        const val HEADER_VIEW = 0
        const val TRACKLIST_VIEW = 1
        const val CARD_PLAYLIST_VIEW = 2
    }
}

private class TrackViewHolder(val binding: SongCardLayoutBinding) :
    ListTrackAdapter.DisplayTypeViewHolder(view = binding.root)

private class HeaderViewHoler(val binding: PlaylistHeaderBinding) :
    ListTrackAdapter.DisplayTypeViewHolder(view = binding.root)

private class PlaylistCardViewHoled(val binding: PlaylistLayoutBinding) :
    ListTrackAdapter.DisplayTypeViewHolder(view = binding.root)

private class ViewHolderNotFoundExepction : RuntimeException()

enum class DisplayMode {
    FULL_PLAYLIST,
    TRACK_LIST,
    PLAYLIST_CARDS
}
