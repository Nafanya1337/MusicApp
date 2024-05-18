package com.example.musicapp.presentation.artist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.bumptech.glide.Glide
import com.example.musicapp.ListTrackAdapter
import com.example.musicapp.MainActivity
import com.example.musicapp.R
import com.example.musicapp.data.remote.models.home.RadioDTO
import com.example.musicapp.data.utils.ToDomainUtil.toData
import com.example.musicapp.databinding.FragmentArtstCardBinding
import com.example.musicapp.domain.models.PlaylistType
import com.example.musicapp.domain.models.TrackListVO
import com.example.musicapp.domain.models.home.RadioVO
import com.example.musicapp.presentation.home.HomeFragmentDirections
import com.example.musicapp.presentation.home.PlaylistAdapter
import com.example.musicapp.presentation.playlist.PlaylistFragmentArgs
import com.example.musicapp.presentation.utils.SpaceItemDecorationUtil
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.properties.Delegates


class ArtistFragment : Fragment(), ListTrackAdapter.Clickable, PlaylistAdapter.Clickable {

    private lateinit var binding: FragmentArtstCardBinding

    private val artistFragmentViewModel: ArtistFragmentViewModel by viewModel<ArtistFragmentViewModel>()

    private var adapter: ListTrackAdapter? = null

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

        val args = ArtistFragmentArgs.fromBundle(requireArguments())
        artist_id = args.id
        initArtistCard(id = artist_id)

        binding.artistAlbumsRecyclerView.addItemDecoration(SpaceItemDecorationUtil(0, 10))

        adapter = ListTrackAdapter(title = "Top 5", this)

        binding.topTracksRecyclerView.adapter = adapter

        binding.topTracksRecyclerView.layoutManager =
            GridLayoutManager(activity, 5, GridLayoutManager.HORIZONTAL, false)
        binding.topTracksRecyclerView.addItemDecoration(SpaceItemDecorationUtil(5, 10))
        PagerSnapHelper().attachToRecyclerView(binding.topTracksRecyclerView)

        artistFragmentViewModel.artist.observe(viewLifecycleOwner) { artist ->
            binding.artistName.text = artist.name
            Glide.with(binding.artistImage).load(artist.picture).into(binding.artistImage)
        }

        artistFragmentViewModel.tracks.observe(viewLifecycleOwner) {tracks ->
            binding.topArtistTracksProgressBar.visibility = View.GONE
            adapter?.list = tracks
        }

        artistFragmentViewModel.albums.observe(viewLifecycleOwner) {albums ->
            binding.albumArtistProgressBar.visibility = View.GONE
            binding.artistAlbumsRecyclerView.adapter = PlaylistAdapter(albums, this)
            (binding.artistAlbumsRecyclerView.adapter as PlaylistAdapter).notifyDataSetChanged()
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

    override fun onItemClicked(radio: RadioVO) {
        val action = ArtistFragmentDirections.actionToPlaylistFragment(radio.toData())
        findNavController().navigate(action)
    }

}