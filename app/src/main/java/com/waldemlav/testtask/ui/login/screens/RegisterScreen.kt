package com.waldemlav.testtask.ui.login.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.waldemlav.testtask.R
import com.waldemlav.testtask.data.network.model.SignUserDtoIn
import com.waldemlav.testtask.ui.viewmodel.MainViewModel
import com.waldemlav.testtask.databinding.FragmentRegisterScreenBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterScreen : Fragment() {

    private var _binding: FragmentRegisterScreenBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSignUp.setOnClickListener {
            val login = binding.etLogin.text.toString()
            val password = binding.etPassword.text.toString()
            val rePassword = binding.etRepeatPassword.text.toString()
            if (!viewModel.isLoginValid(login))
                Toast.makeText(context, R.string.invalid_login, Toast.LENGTH_LONG).show()
            else if (!viewModel.isPasswordValid(password))
                Toast.makeText(context, R.string.invalid_password, Toast.LENGTH_LONG).show()
            else if (password != rePassword)
                Toast.makeText(context, R.string.invalid_repeat_password, Toast.LENGTH_LONG).show()
            else
                viewModel.signUp(SignUserDtoIn(login, password))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}