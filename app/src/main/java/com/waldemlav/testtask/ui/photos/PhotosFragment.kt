package com.waldemlav.testtask.ui.photos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.waldemlav.testtask.R
import com.waldemlav.testtask.databinding.FragmentPhotosBinding
import com.waldemlav.testtask.ui.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PhotosFragment : Fragment() {

    private var _binding: FragmentPhotosBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPhotosBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        binding.photosGrid.adapter = PhotoGridAdapter(
            onItemClicked = {
                viewModel.getCommentList(it.id)
                val bundle = bundleOf("photoData" to it)
                findNavController().navigate(
                    R.id.action_photosFragment_to_photoDetailsFragment, bundle
                )
            },
            onLongClick = {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle(resources.getString(R.string.alert_title))
                    .setMessage(resources.getString(R.string.alert_message))
                    .setNegativeButton(resources.getString(R.string.cancel)) { dialog, _ ->
                        dialog.dismiss()
                    }
                    .setPositiveButton(resources.getString(R.string.accept)) { dialog, _ ->
                        viewModel.deleteImage(it)
                        dialog.dismiss()
                    }
                    .show()
            }
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.checkIfCacheIsEmpty()
        unlockFab()
        unlockNavigationView()
        setPhotosMenuItemChecked()

        if (viewModel.isCacheAvailable && !viewModel.isCacheEmpty)
            viewModel.getPhotoList()
        else {
            viewModel.cacheData()
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            message.getContentIfNotHandled()?.let {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        }

        binding.photosGrid.setHasFixedSize(true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Ensure Navigation drawer is available only when user is logged in
    private fun unlockNavigationView() {
        val drawerLayout = requireActivity().findViewById<DrawerLayout>(R.id.drawer_layout)
        val toolbar = requireActivity().findViewById<MaterialToolbar>(R.id.topAppBar)

        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        toolbar.setNavigationIcon(R.drawable.ic_menu_24)
        toolbar.setNavigationOnClickListener {
            drawerLayout.open()
        }
    }

    // Ensure FAB is available only when user is logged in
    private fun unlockFab() {
        requireActivity().findViewById<FloatingActionButton>(R.id.fab).visibility = View.VISIBLE
    }

    private fun setPhotosMenuItemChecked() {
        requireActivity().findViewById<NavigationView>(R.id.navigationView)
            .menu.findItem(R.id.menu_photos).isChecked = true
    }
}