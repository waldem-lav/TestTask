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
import com.waldemlav.testtask.databinding.FragmentLogInScreenBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LogInScreen : Fragment() {

    private var _binding: FragmentLogInScreenBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLogInScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogIn.setOnClickListener {
            val login = binding.etLogin.text.toString()
            val password = binding.etPassword.text.toString()
            if (!viewModel.isLoginValid(login))
                Toast.makeText(context, R.string.invalid_login, Toast.LENGTH_LONG).show()
            else if (!viewModel.isPasswordValid(password))
                Toast.makeText(context, R.string.invalid_password, Toast.LENGTH_LONG).show()
            else
                viewModel.signIn(SignUserDtoIn(login, password))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}