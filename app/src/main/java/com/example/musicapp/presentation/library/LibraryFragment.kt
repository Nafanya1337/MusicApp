package com.example.musicapp.presentation.library

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.musicapp.ListTrackAdapter
import com.example.musicapp.MainActivity
import com.example.musicapp.MusicApp
import com.example.musicapp.R
import com.example.musicapp.databinding.FragmentLibraryBinding
import com.example.musicapp.domain.models.TrackListVO
import com.example.musicapp.presentation.utils.SpaceItemDecorationUtil


class LibraryFragment : Fragment(), ListTrackAdapter.Clickable {

    lateinit var binding: FragmentLibraryBinding

    private val libraryFragmentViewModel by viewModels<LibraryFragmentViewModel> { LibraryFragmentViewModel.Factory }

    private var adapter: ListTrackAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLibraryBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ListTrackAdapter(title = "Favourites", this, false)

        binding.favRecyclerView.addItemDecoration(SpaceItemDecorationUtil(10, 10))
        binding.favRecyclerView.adapter = adapter
        MusicApp.userFav.observe(viewLifecycleOwner) { list ->
            Log.d("mymy", adapter?.list?.size.toString())
            Log.d("mymy", list?.size.toString())
            adapter?.list = list
        }

    }

    override fun onItemClick(trackList: TrackListVO, position: Int) {
        (activity as MainActivity).startNewTrackList(trackList = trackList, position = position)
    }
}