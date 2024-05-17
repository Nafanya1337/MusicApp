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
import com.example.musicapp.data.remote.models.home.RadioDTO
import com.example.musicapp.data.utils.RoundedCornersTransformation
import com.example.musicapp.data.utils.ToDomainUtil.toDomain
import com.example.musicapp.databinding.FragmentPlaylistBinding
import com.example.musicapp.domain.models.AlbumInfoVO
import com.example.musicapp.domain.models.PlaylistType
import com.example.musicapp.domain.models.TrackListVO
import com.example.musicapp.domain.models.home.RadioVO
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

    private var adapter: ListTrackAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = PlaylistFragmentArgs.fromBundle(requireArguments())
        val currentPlaylist = args.currentPlaylist.toDomain()

        if (currentPlaylist.type == PlaylistType.ALBUM.text)
            playlistViewModel.setAlbumInfo(AlbumInfoVO(
                id = currentPlaylist.id,
                title = currentPlaylist.title,
                picture = currentPlaylist.picture,
                pictureBig = currentPlaylist.picture
            ))
        playlistViewModel.downloadTrackList(currentPlaylist.id, currentPlaylist.title, when (currentPlaylist.type) {
            PlaylistType.ALBUM.text -> PlaylistType.ALBUM
            PlaylistType.ALL_ARTIST_TRACKS.text -> PlaylistType.ALL_ARTIST_TRACKS
            else -> PlaylistType.RADIO
        })

        adapter = ListTrackAdapter(title = currentPlaylist.title, this, true)

        binding.playlistRecyclerView.adapter = adapter

        binding.playlistRecyclerView.addItemDecoration(SpaceItemDecorationUtil(10, 0))

        playlistViewModel.trackList.observe(viewLifecycleOwner) { tracks ->
            binding.progressBarPlaylist.visibility = View.GONE
            adapter?.list = listOf(currentPlaylist) + tracks.list
        }
    }

    override fun onItemClick(trackList: TrackListVO, position: Int) {
        (activity as MainActivity).startNewTrackList(trackList = trackList, position = position)
        playlistViewModel.setCurrentPosition(position)
    }

    fun RadioDTO.toDomain(): RadioVO {
        return RadioVO(
            id = this.id,
            title = this.title,
            picture = this.picture,
            trackList = this.trackList,
            type = this.type,
            contributor = this.constributor?.map { it.toDomain() }
        )
    }

}