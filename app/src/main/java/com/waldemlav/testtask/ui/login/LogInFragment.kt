package com.waldemlav.testtask.ui.login

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.drawerlayout.widget.DrawerLayout
import androidx.drawerlayout.widget.DrawerLayout.LOCK_MODE_LOCKED_CLOSED
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayoutMediator
import com.waldemlav.testtask.R
import com.waldemlav.testtask.ui.login.screens.LogInScreen
import com.waldemlav.testtask.ui.login.screens.RegisterScreen
import com.waldemlav.testtask.databinding.FragmentLogInBinding
import com.waldemlav.testtask.ui.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LogInFragment : Fragment() {

    private var _binding: FragmentLogInBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var prefs: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLogInBinding.inflate(inflater, container, false)

        val adapter = ViewPagerAdapter(
            arrayListOf(LogInScreen(), RegisterScreen()),
            requireActivity().supportFragmentManager,
            lifecycle
        )
        binding.pager.adapter = adapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prefs = requireActivity().getPreferences(Context.MODE_PRIVATE)

        TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
            when (position) {
                0 -> tab.text = "Login"
                1 -> tab.text = "Register"
            }
        }.attach()

        lockFab()
        lockNavigationView()

        viewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            message.getContentIfNotHandled()?.let {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        }
        viewModel.isUserLoggedIn.observe(viewLifecycleOwner) { isLogged ->
            isLogged.getContentIfNotHandled()?.let { isUserLogged ->
                if (isUserLogged) {
                    viewModel.isCacheAvailable = false
                    if (isCacheAvailable())
                        viewModel.isCacheAvailable = true
                    saveCurrentUserToPrefs()
                    findNavController().navigate(R.id.action_logInFragment_to_photosFragment)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun isCacheAvailable() = prefs.getString("currentUser", "") == viewModel.login

    private fun saveCurrentUserToPrefs() {
        val editor = prefs.edit()
        editor.putString("currentUser", viewModel.login)
        editor.apply()
    }

    private fun lockNavigationView() {
        requireActivity().findViewById<DrawerLayout>(R.id.drawer_layout)
            .setDrawerLockMode(LOCK_MODE_LOCKED_CLOSED)
        requireActivity().findViewById<MaterialToolbar>(R.id.topAppBar).navigationIcon = null
    }

    private fun lockFab() {
        requireActivity().findViewById<FloatingActionButton>(R.id.fab).visibility = View.INVISIBLE
    }
}