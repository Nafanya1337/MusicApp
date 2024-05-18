package com.example.musicapp.presentation.account

import android.content.Context
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.musicapp.DARK_THEME_ENABLED_KEY
import com.example.musicapp.MainActivity
import com.example.musicapp.MusicApp
import com.example.musicapp.R
import com.example.musicapp.THEME_PREFERENCES
import com.example.musicapp.databinding.FragmentAccountBinding

class AccountFragment : Fragment() {

    companion object {
        fun newInstance() = AccountFragment()
    }

    private val viewModel: AccountViewModel by viewModels { AccountViewModel.Factory }

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

        val sharedPreferences =
            requireActivity().getSharedPreferences(THEME_PREFERENCES, Context.MODE_PRIVATE)
        val isDarkThemeEnabled = sharedPreferences.getBoolean(DARK_THEME_ENABLED_KEY, false)

        binding.themeSwitcher.isChecked = isDarkThemeEnabled

        binding.themeSwitcher.setOnCheckedChangeListener { _, isChecked ->
            sharedPreferences.edit().putBoolean(DARK_THEME_ENABLED_KEY, isChecked).apply()
            (binding.root.context.applicationContext as MusicApp).applyTheme(isChecked)
        }


        MusicApp.user.observe(viewLifecycleOwner) { user ->
            user?.let {
                binding.userNickname.text = user.nickname
                Glide.with(binding.accountUserImage).load(user.photo)
                    .into(binding.accountUserImage)
                binding.userEmail.text = user.email
            }
        }

        binding.button2.setOnClickListener {
            viewModel.signOut()
            (activity as MainActivity).hidePlayer()
            findNavController().popBackStack()
            findNavController().navigate(R.id.action_homeFragment_to_auth_nav_graph)
        }

    }


}

