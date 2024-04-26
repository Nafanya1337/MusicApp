package com.example.musicapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.example.musicapp.databinding.FragmentSettingsBinding


class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.themeSwitcher.isChecked = requireActivity().darkThemeIsChecked()

        binding.backImage.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.themeSwitcher.setOnCheckedChangeListener {
            switcher, checked ->
            requireActivity().saveTheme(checked)
            (binding.root.context.applicationContext as LastFmApp).applyTheme()
        }
    }


}