package com.example.musicapp.presentation.login.signUp

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.musicapp.R
import com.example.musicapp.databinding.FragmentSignUpBinding


class SignUpFragment : Fragment() {

    lateinit var binding: FragmentSignUpBinding

    val signUpViewModel by viewModels<SignUpViewModel> { SignUpViewModel.Factory }

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

            // Проверка поля Nickname
            if (nickname.isEmpty()) {
                binding.nicknameTextInputSignUpLayout.error = "Nickname is required"
                isValid = false
            } else {
                binding.nicknameTextInputSignUpLayout.error = null
            }

            // Проверка поля Email
            if (email.isEmpty()) {
                binding.emailTextInputSignUpLayout.error = "Email is required"
                isValid = false
            } else {
                binding.emailTextInputSignUpLayout.error = null
            }

            // Проверка поля Password
            if (password.isEmpty()) {
                binding.passwordTextInputSignUpLayout.error = "Password is required"
                isValid = false
            } else {
                binding.passwordTextInputSignUpLayout.error = null
            }

            // Проверка поля Re-enter Password
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

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SignUpFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}