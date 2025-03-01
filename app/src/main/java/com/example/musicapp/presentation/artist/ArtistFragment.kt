package com.example.musicapp.presentation.artist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.musicapp.presentation.adapter.ListTrackAdapter
import com.example.musicapp.presentation.main.MainActivity
import com.example.musicapp.data.remote.models.home.RadioDTO
import com.example.musicapp.data.utils.ToDomainUtil.toData
import com.example.musicapp.databinding.FragmentArtstCardBinding
import com.example.musicapp.domain.models.tracks.PlaylistType
import com.example.musicapp.domain.models.tracks.TrackListVO
import com.example.musicapp.domain.models.home.RadioVO
import com.example.musicapp.presentation.adapter.DisplayMode
import com.example.musicapp.presentation.utils.DiffUtilCallback
import com.example.musicapp.presentation.utils.SpaceItemDecorationUtil
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.properties.Delegates


class ArtistFragment : Fragment(), ListTrackAdapter.TrackClickable, ListTrackAdapter.PlaylistCardClickable {

    private lateinit var binding: FragmentArtstCardBinding

    private val artistFragmentViewModel: ArtistFragmentViewModel by viewModel<ArtistFragmentViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentArtstCardBinding.inflate(inflater)
        return binding.root
    }

    var artist_id by Delegates.notNull<Long>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backArrow.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.artistAlbumsRecyclerView.adapter = ListTrackAdapter(
            displayMode = DisplayMode.PLAYLIST_CARDS,
            playlistCardClickableImpl = this,
            diffUtilCallback = DiffUtilCallback(emptyList(), emptyList()
            ) { old, new ->
                if (old is RadioVO && new is RadioVO)
                    old.id == new.id
                else
                    old == new
            }
        )

        val args = ArtistFragmentArgs.fromBundle(requireArguments())
        artist_id = args.id
        initArtistCard(id = artist_id)

        binding.artistAlbumsRecyclerView.addItemDecoration(SpaceItemDecorationUtil(0, 10))

        binding.topTracksRecyclerView.adapter = ListTrackAdapter(displayMode = DisplayMode.TRACK_LIST , trackClickableImpl = this)

        binding.topTracksRecyclerView.layoutManager =
            GridLayoutManager(activity, 5, GridLayoutManager.HORIZONTAL, false)

        artistFragmentViewModel.artist.observe(viewLifecycleOwner) { artist ->
            binding.artistName.text = artist.name
            Glide.with(binding.artistImage).load(artist.picture).into(binding.artistImage)
        }

        artistFragmentViewModel.tracks.observe(viewLifecycleOwner) {tracks ->
            binding.topArtistTracksProgressBar.visibility = View.GONE
            (binding.topTracksRecyclerView.adapter as ListTrackAdapter).list = tracks
        }

        artistFragmentViewModel.albums.observe(viewLifecycleOwner) {albums ->
            binding.albumArtistProgressBar.visibility = View.GONE
            (binding.artistAlbumsRecyclerView.adapter as ListTrackAdapter).list = albums
        }

        binding.buttonSeeAllTracks.setOnClickListener {

            val action = ArtistFragmentDirections.actionToPlaylistFragment(RadioDTO(
                id = this.artist_id,
                title = "${binding.artistName.text}",
                picture = "",
                trackList = "",
                type = PlaylistType.ALL_ARTIST_TRACKS.text,
                constributor = null
            ))
            findNavController().navigate(action)
        }
    }

    private fun initArtistCard(id: Long) {
        artistFragmentViewModel.getArtistInfo(id = id)
        artistFragmentViewModel.getArtistTopTrack(id = id)
        artistFragmentViewModel.getArtistAlbums(id = id)
    }



    override fun onItemClick(trackList: TrackListVO, position: Int) {
        (activity as MainActivity).startNewTrackList(trackList = trackList, position = position)
    }

    override fun onItemClick(radio: RadioVO) {
        val action = ArtistFragmentDirections.actionToPlaylistFragment(radio.toData())
        findNavController().navigate(action)
    }

}