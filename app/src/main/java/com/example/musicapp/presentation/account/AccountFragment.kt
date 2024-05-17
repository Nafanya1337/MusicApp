package com.example.musicapp.presentation.account

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.musicapp.MusicApp
import com.example.musicapp.R
import com.example.musicapp.databinding.FragmentAccountBinding

class AccountFragment : Fragment() {

    companion object {
        fun newInstance() = AccountFragment()
    }

    private val viewModel: AccountViewModel by viewModels()

    lateinit var binding: FragmentAccountBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAccountBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        MusicApp.user.observe(viewLifecycleOwner) {user ->
            user?.let {
                binding.userNickname.text = user.nickname
                Glide.with(binding.accountUserImage).load(user.photo)
                    .into(binding.accountUserImage)
            }
        }

    }

}