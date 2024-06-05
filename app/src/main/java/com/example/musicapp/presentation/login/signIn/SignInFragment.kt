package com.example.musicapp.presentation.login.signIn

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.musicapp.R
import com.example.musicapp.databinding.FragmentSignInBinding
import com.example.musicapp.presentation.main.MainActivity
import org.koin.androidx.viewmodel.ext.android.viewModel


class SignInFragment : Fragment() {

    private val signInViewModel by viewModel<SignInViewModel>()

    lateinit var binding: FragmentSignInBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignInBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        signInViewModel.result.observe(viewLifecycleOwner) { success ->
            val text =
                if (success) {
                    "Login successful"

                } else {
                    "Authentication failed"
                }
            Toast.makeText(activity?.applicationContext, text, Toast.LENGTH_SHORT)
                .show()
            binding.buttonSignIn.isEnabled = true

            if (success) {
                (activity as MainActivity).initUser()
                findNavController().popBackStack()
            }
        }

        binding.buttonSignIn.setOnClickListener {

            val email = binding.emailTextInputSignInEditText.text.toString()
            val password = binding.passwordTextInputSignInEditText.text.toString()

            var isValid = true

            if (email.isEmpty()) {
                binding.emailTextInputSignInLayout.error = "Email is required"
                isValid = false
            } else {
                binding.emailTextInputSignInLayout.error = null
            }

            if (password.isEmpty()) {
                binding.passwordTextInputSignInLayout.error = "Password is required"
                isValid = false
            } else {
                binding.passwordTextInputSignInLayout.error = null
            }

            if (isValid) {
                binding.buttonSignIn.isEnabled = false
                signInViewModel.signIn(email = email, password = password)
            }
        }

        binding.signUpText.setOnClickListener {
            findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
        }
    }
}