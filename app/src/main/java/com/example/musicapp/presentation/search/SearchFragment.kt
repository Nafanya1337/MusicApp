package com.example.musicapp.presentation.search

import android.app.Activity
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.example.musicapp.app.MusicApp
import com.example.musicapp.presentation.adapter.ListTrackAdapter
import com.example.musicapp.presentation.main.MainActivity
import com.example.musicapp.databinding.FragmentSearchBinding
import com.example.musicapp.domain.models.tracks.TrackListVO
import com.example.musicapp.domain.models.search.SearchRequestVO
import com.example.musicapp.domain.models.search.StatusClassVO
import com.example.musicapp.presentation.adapter.DisplayMode
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment(), SearchHistoryAdapter.Clickable, ListTrackAdapter.TrackClickable {

    private lateinit var binding: FragmentSearchBinding

    private val searchViewModel: SearchViewModel by viewModel<SearchViewModel>()

    private var adapter : ListTrackAdapter? = null
    private val searchAdapter by lazy {
        SearchHistoryAdapter(emptyList(), this)
    }

    private val musicApp by lazy { activity?.application as MusicApp }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onItemClick(trackList: TrackListVO, position: Int) {
        (activity as MainActivity).startNewTrackList(trackList = trackList, position = position)
        hideKeyboard()
        binding.editText.isFocusableInTouchMode = true  // Make it focusable again
        binding.editText.isFocusable = true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchViewModel.getSearchHistory()

        val spacingInPixels = 10

        binding.recyclerView.addItemDecoration(VerticalSpaceItemDecoration(spacingInPixels))

        adapter = ListTrackAdapter(
            displayMode = DisplayMode.TRACK_LIST,
            trackClickableImpl = this
        )

        searchViewModel.status.observe(viewLifecycleOwner) {
            binding.searchPb.visibility = View.GONE
            binding.recyclerView.visibility =
                when (it) {
                    StatusClassVO.SUCCESS -> {
                        searchViewModel.saveSearchRequest(
                            request = SearchRequestVO(
                                binding.editText.text.toString()
                            )
                        )
                        binding.closeIcon.visibility = View.VISIBLE
                        binding.binIcon.visibility = View.INVISIBLE
                        binding.recyclerView.adapter = adapter

                        View.VISIBLE
                    }

                    StatusClassVO.NO_SEARCH -> {
                        searchViewModel.getSearchHistory()
                        binding.recyclerView.adapter = searchAdapter
                        binding.binIcon.visibility = View.VISIBLE
                        binding.closeIcon.visibility = View.INVISIBLE
                        View.VISIBLE
                    }

                    StatusClassVO.ERROR -> {
                        Toast.makeText(musicApp.applicationContext, StatusClassVO.ERROR.msg, Toast.LENGTH_LONG).show()
                        View.GONE
                    }

                    else -> {
                        binding.closeIcon.visibility = View.VISIBLE
                        binding.binIcon.visibility = View.INVISIBLE
                        View.GONE
                    }
                }

            binding.noInternet.root.visibility =
                if (it == StatusClassVO.INTERNET_FAIL) View.VISIBLE
                else View.GONE

            binding.noSearchResults.root.visibility =
                if (it == StatusClassVO.NO_TRACKS_FAIL) View.VISIBLE
                else View.GONE
        }

        searchViewModel.tracks.observe(viewLifecycleOwner) {
            adapter?.list = it
        }

        searchViewModel.history.observe(viewLifecycleOwner) {
            searchAdapter.searchHistory = it
            binding.recyclerView.adapter!!.notifyDataSetChanged()
        }

        binding.editText.addTextChangedListener {
            binding.recyclerView.visibility = View.GONE
            binding.noInternet.root.visibility = View.GONE
            binding.noSearchResults.root.visibility = View.GONE
            binding.searchPb.visibility = View.VISIBLE
            if (it?.length!! > 0) {
                lifecycleScope.launch {
                    searchViewModel.searchTrack(searchRequest = SearchRequestVO(binding.editText.text.toString()))
                }
            } else {
                searchViewModel.clear()
            }
        }

        binding.noInternet.button.setOnClickListener {
            lifecycleScope.launch {
                searchViewModel.searchTrack(searchRequest = SearchRequestVO(binding.editText.text.toString()))
            }
        }

        binding.closeIcon.setOnClickListener {
            hideKeyboard()
            binding.editText.clearFocus()
            binding.editText.text.clear()
            searchViewModel.clear()
        }

        binding.binIcon.setOnClickListener {
            searchViewModel.clearSearchHistory()
        }
    }

    private fun hideKeyboard() {
        val imm =
            requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as? InputMethodManager
        var view = requireActivity().currentFocus
        view?.let { View(activity) }
        imm?.let { imm -> imm.hideSoftInputFromWindow(view?.windowToken, 0) }
    }

    override fun onItemClick(request: SearchRequestVO) {
        binding.editText.setText(request.searchText)
    }

}


class VerticalSpaceItemDecoration(private val verticalSpaceHeight: Int) : ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.top = verticalSpaceHeight
        outRect.bottom = verticalSpaceHeight
    }
}