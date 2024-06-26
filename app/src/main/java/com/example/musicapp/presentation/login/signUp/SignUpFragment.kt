package com.example.musicapp.presentation.login.signUp

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.musicapp.databinding.FragmentSignUpBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


class SignUpFragment : Fragment() {

    lateinit var binding: FragmentSignUpBinding

    val signUpViewModel by viewModel<SignUpViewModel>()

    private var imageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignUpBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imageUpload.setOnClickListener {
            choosePicture()
        }

        binding.signUpButton.setOnClickListener {

            val nickname = binding.nicknameTextInputSignUpEditText.text.toString()
            val email = binding.emailTextInputSignUpEditText.text.toString()
            val password = binding.passwordTextInputSignUpEditText.text.toString()
            val rePassword = binding.rePasswordTextInputSignUpEditText.text.toString()

            var isValid = true

            if (nickname.isEmpty()) {
                binding.nicknameTextInputSignUpLayout.error = "Nickname is required"
                isValid = false
            } else {
                binding.nicknameTextInputSignUpLayout.error = null
            }

            if (email.isEmpty()) {
                binding.emailTextInputSignUpLayout.error = "Email is required"
                isValid = false
            } else {
                binding.emailTextInputSignUpLayout.error = null
            }

            if (password.isEmpty()) {
                binding.passwordTextInputSignUpLayout.error = "Password is required"
                isValid = false
            } else {
                binding.passwordTextInputSignUpLayout.error = null
            }

            if (rePassword.isEmpty()) {
                binding.rePasswordTextInputSignUpLayout.error = "Please re-enter your password"
                isValid = false
            } else if (rePassword != password) {
                binding.rePasswordTextInputSignUpLayout.error = "Passwords do not match"
                isValid = false
            } else {
                binding.rePasswordTextInputSignUpLayout.error = null
            }

            if (isValid) {
                if (imageUri == null)
                    imageUri = Uri.parse("android.resource://com.example.musicapp/drawable/image")
                signUpViewModel.signUp(email = email, password = password, nickname = nickname, imageUri = imageUri.toString())
            }
        }

        binding.signInText.setOnClickListener {
            findNavController().popBackStack()
        }

        signUpViewModel.result.observe(viewLifecycleOwner) { result ->
            val text = if (result) "success" else "failed"
            Toast.makeText(requireContext(), "Sign up $text", Toast.LENGTH_SHORT).show()
            if (result) {
                findNavController().popBackStack()
            }
        }

    }

    private fun choosePicture() {
        val intent = Intent()
        intent.setType("image/*")
        intent.setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.data != null) {
            imageUri = data.data!!
            binding.imageUpload.setImageURI(imageUri)
        }
    }
}