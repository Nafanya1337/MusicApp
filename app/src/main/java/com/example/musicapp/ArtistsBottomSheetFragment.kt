package com.example.musicapp

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.setFragmentResult
import com.example.musicapp.data.remote.models.ContributorsDTO
import com.example.musicapp.data.utils.ToDomainUtil.toData
import com.example.musicapp.data.utils.ToDomainUtil.toDomain
import com.example.musicapp.databinding.FragmentArtistsBottomSheetBinding
import com.example.musicapp.domain.models.ContributorsVO
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

const val CONTRIBUTORS_KEY = "Contributors"
const val CONTRIBUTORS_CHOOSE_KEY = "Contributors_bottom_sheet"
const val CONTRIBUTORS_REQUEST_KEY = "Contributors_chosen"

class ArtistsBottomSheetFragment : BottomSheetDialogFragment(), ArtistsAdapter.Clickable {

    lateinit var binding: FragmentArtistsBottomSheetBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentArtistsBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        fun newInstance(artists: List<ContributorsVO>): ArtistsBottomSheetFragment {
            val args = Bundle()
            args.putParcelableArrayList(CONTRIBUTORS_KEY, ArrayList(artists.map { it.toData() }))
            val fragment = ArtistsBottomSheetFragment()
            fragment.arguments = args
            return fragment
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val contributors: ArrayList<ContributorsDTO>? = arguments?.getParcelableArrayList(CONTRIBUTORS_KEY, ContributorsDTO::class.java)
        val list = contributors ?: emptyList<ContributorsDTO>()
        binding.artistsRecycleView.adapter = ArtistsAdapter(artists = list.map { it.toDomain() }, clickable = this)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState)
    }

    override fun getTheme(): Int {
        return super.getTheme()
    }

    override fun onItemClick(artist: ContributorsVO) {
        setFragmentResult(CONTRIBUTORS_CHOOSE_KEY, bundleOf(CONTRIBUTORS_REQUEST_KEY to artist.toData()))
        this.dismiss()
    }


}