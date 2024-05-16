package com.example.musicapp.presentation.playlist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.marginTop
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.musicapp.ListTrackAdapter
import com.example.musicapp.MainActivity
import com.example.musicapp.R
import com.example.musicapp.data.utils.RoundedCornersTransformation
import com.example.musicapp.databinding.FragmentPlaylistBinding
import com.example.musicapp.domain.models.AlbumInfoVO
import com.example.musicapp.domain.models.PlaylistType
import com.example.musicapp.domain.models.TrackListVO
import com.example.musicapp.presentation.utils.SpaceItemDecorationUtil


class PlaylistFragment : Fragment(), ListTrackAdapter.Clickable {

    private lateinit var binding: FragmentPlaylistBinding

    private val playlistViewModel: PlaylistViewModel by viewModels { PlaylistViewModel.Factory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaylistBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = PlaylistFragmentArgs.fromBundle(requireArguments())
        val currentPlaylist = args.currentPlaylist

        if (currentPlaylist.type == PlaylistType.ALBUM.text)
            playlistViewModel.setAlbumInfo(AlbumInfoVO(
                id = currentPlaylist.id,
                title = currentPlaylist.title,
                picture = currentPlaylist.picture,
                pictureBig = currentPlaylist.picture
            ))

        if (currentPlaylist.type == PlaylistType.ALL_ARTIST_TRACKS.text) {
            binding.playlistImage.visibility = View.GONE
            binding.playlistPlaylistName.visibility = View.GONE
            binding.playlistArtistName.visibility = View.GONE
        }
        else {
            binding.playlistPlaylistName.text = currentPlaylist.title
            binding.playlistArtistName.visibility = if (currentPlaylist.constributor != null) {
                binding.playlistArtistName.text =
                    currentPlaylist.constributor!!.joinToString(separator = ", ") { it.name }
                View.VISIBLE
            } else {
                View.GONE
            }
            Glide
                .with(view)
                .load(currentPlaylist.picture)
                .transform(RoundedCornersTransformation(20))
                .into(binding.playlistImage)
        }
        playlistViewModel.downloadTrackList(currentPlaylist.id, currentPlaylist.title, when (currentPlaylist.type) {
            PlaylistType.ALBUM.text -> PlaylistType.ALBUM
            PlaylistType.ALL_ARTIST_TRACKS.text -> PlaylistType.ALL_ARTIST_TRACKS
            else -> PlaylistType.RADIO
        })
        binding.playlistRecyclerView.adapter =
            ListTrackAdapter(TrackListVO(title = currentPlaylist.title, emptyList()), this)
        binding.playlistRecyclerView.addItemDecoration(SpaceItemDecorationUtil(10, 0))

        playlistViewModel.trackList.observe(viewLifecycleOwner) { tracks ->
            binding.progressBarPlaylist.visibility = View.GONE
            (binding.playlistRecyclerView.adapter as ListTrackAdapter).trackList = tracks
            (binding.playlistRecyclerView.adapter as ListTrackAdapter).notifyDataSetChanged()
        }
    }

    override fun onItemClick(trackList: TrackListVO, position: Int) {
        (activity as MainActivity).startNewTrackList(trackList = trackList, position = position)
        playlistViewModel.setCurrentPosition(position)
    }

}