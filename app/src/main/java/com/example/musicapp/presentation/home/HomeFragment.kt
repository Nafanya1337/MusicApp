package com.example.musicapp.presentation.home

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.example.musicapp.ListTrackAdapter
import com.example.musicapp.MainActivity
import com.example.musicapp.data.utils.ToDomainUtil.toData
import com.example.musicapp.databinding.FragmentHomeBinding
import com.example.musicapp.domain.models.TrackListVO
import com.example.musicapp.domain.models.home.RadioVO
import com.example.musicapp.presentation.utils.SpaceItemDecorationUtil

class HomeFragment : Fragment(), ListTrackAdapter.Clickable, PlaylistAdapter.Clickable {

    private lateinit var binding: FragmentHomeBinding

    private val homeViewModel: HomeViewModel by viewModels { HomeViewModel.Factory }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.profileImage.setOnClickListener {

        }
        binding.chartsRecyclerView.adapter = ListTrackAdapter(TrackListVO(title = "Charts", list = emptyList()), this)
        binding.recommendationsRecyclerView.adapter = PlaylistAdapter(emptyList(), this)
        binding.chartsRecyclerView.layoutManager = GridLayoutManager(activity, 3, GridLayoutManager.HORIZONTAL, false)
        homeViewModel.init()
        homeViewModel
        homeViewModel.chartList.observe(viewLifecycleOwner) {chart->
            ((binding.chartsRecyclerView.adapter) as ListTrackAdapter).trackList.list = chart.list
            (binding.chartsRecyclerView.adapter)!!.notifyDataSetChanged()
            if (chart.list.isNotEmpty())
                binding.chartProgressBar.visibility = View.GONE
        }
        homeViewModel.recommendationsList.observe(viewLifecycleOwner) {radio ->
            ((binding.recommendationsRecyclerView.adapter) as PlaylistAdapter).playlistList = radio
            ((binding.recommendationsRecyclerView.adapter) as PlaylistAdapter).notifyDataSetChanged()
            if (radio.isNotEmpty())
                binding.radioProgrssBar.visibility = View.GONE
        }
        binding.chartsRecyclerView.addItemDecoration(SpaceItemDecorationUtil(5, 10))
        binding.recommendationsRecyclerView.addItemDecoration(SpaceItemDecorationUtil(20,10))
        binding.recommendationsRecyclerView.layoutManager = GridLayoutManager(activity, 2, GridLayoutManager.HORIZONTAL, false)
        PagerSnapHelper().attachToRecyclerView(binding.chartsRecyclerView)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onItemClick(trackList: TrackListVO, position: Int) {
        (activity as MainActivity).startNewTrackList(trackList = trackList, position = position)
    }

    override fun onItemClicked(radio: RadioVO) {
        val action = HomeFragmentDirections.actionToPlaylistFragment(radio.toData())
        findNavController().navigate(action)
    }
}