package com.example.musicapp.presentation.home

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.bumptech.glide.Glide
import com.example.musicapp.presentation.utils.DiffUtilCallback
import com.example.musicapp.presentation.adapter.ListTrackAdapter
import com.example.musicapp.presentation.main.MainActivity
import com.example.musicapp.app.MusicApp
import com.example.musicapp.R
import com.example.musicapp.data.utils.ToDomainUtil.toData
import com.example.musicapp.databinding.FragmentHomeBinding
import com.example.musicapp.domain.models.tracks.TrackListVO
import com.example.musicapp.domain.models.tracks.TrackVO
import com.example.musicapp.domain.models.home.RadioVO
import com.example.musicapp.presentation.utils.SpaceItemDecorationUtil
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment(), ListTrackAdapter.Clickable, PlaylistAdapter.Clickable {

    private lateinit var binding: FragmentHomeBinding

    private val homeViewModel: HomeViewModel by viewModel<HomeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    private var adapter: ListTrackAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.profileImage.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_accountFragment2)
        }

        adapter = ListTrackAdapter(
            title = "Charts",
            this,
            diffUtilCallback = DiffUtilCallback(emptyList(), emptyList()
            ) { old, new ->
                if (old is TrackVO && new is TrackVO)
                    old.id == new.id
                else
                    old == new
            }
        )

        binding.chartsRecyclerView.adapter = adapter
        binding.recommendationsRecyclerView.adapter = PlaylistAdapter(emptyList(), this)
        binding.chartsRecyclerView.layoutManager =
            GridLayoutManager(activity, 3, GridLayoutManager.HORIZONTAL, false)
        homeViewModel.init()

        MusicApp.user.observe(viewLifecycleOwner) { user ->
            user?.let {
                Glide
                    .with(requireActivity().applicationContext)
                    .load(user.photo)
                    .into(binding.profileImage)
            }
        }
        homeViewModel.chartList.observe(viewLifecycleOwner) { chart ->
            adapter?.list = chart.list
            if (chart.list.isNotEmpty())
                binding.chartProgressBar.visibility = View.GONE
        }
        homeViewModel.recommendationsList.observe(viewLifecycleOwner) { radio ->
            ((binding.recommendationsRecyclerView.adapter) as PlaylistAdapter).playlistList = radio
            ((binding.recommendationsRecyclerView.adapter) as PlaylistAdapter).notifyDataSetChanged()
            if (radio.isNotEmpty())
                binding.radioProgrssBar.visibility = View.GONE
        }
        binding.chartsRecyclerView.addItemDecoration(SpaceItemDecorationUtil(5, 10))
        binding.recommendationsRecyclerView.addItemDecoration(SpaceItemDecorationUtil(20, 10))
        binding.recommendationsRecyclerView.layoutManager =
            GridLayoutManager(activity, 2, GridLayoutManager.HORIZONTAL, false)
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