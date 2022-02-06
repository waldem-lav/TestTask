package com.waldemlav.testtask.ui.photos.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.waldemlav.testtask.R
import com.waldemlav.testtask.data.network.model.CommentDtoIn
import com.waldemlav.testtask.databinding.FragmentPhotoDetailsBinding
import com.waldemlav.testtask.ui.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PhotoDetailsFragment : Fragment() {

    private var _binding: FragmentPhotoDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPhotoDetailsBinding.inflate(inflater, container, false)
        binding.photo = this.arguments?.getParcelable("photoData")

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        binding.rvComments.adapter = CommentAdapter(
            onLongClick = {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle(resources.getString(R.string.alert_comment_title))
                    .setMessage(resources.getString(R.string.alert_message))
                    .setNegativeButton(resources.getString(R.string.cancel)) { dialog, _ ->
                        dialog.dismiss()
                    }
                    .setPositiveButton(resources.getString(R.string.accept)) { dialog, _ ->
                        viewModel.deleteComment(it)
                        dialog.dismiss()
                    }
                    .show()
            }
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lockNavigationView()
        lockFab()

        viewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            message.getContentIfNotHandled()?.let {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        }
        binding.rvComments.setHasFixedSize(true)
        binding.ivSend.setOnClickListener {
            val comment = binding.etComment.text.toString()
            if (comment.isNotEmpty())
                viewModel.uploadComment(CommentDtoIn(comment), binding.photo!!.id)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun lockNavigationView() {
        val drawerLayout = requireActivity().findViewById<DrawerLayout>(R.id.drawer_layout)
        val toolbar = requireActivity().findViewById<MaterialToolbar>(R.id.topAppBar)

        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        toolbar.setNavigationIcon(R.drawable.ic_back_24)
        toolbar.setNavigationOnClickListener {
            findNavController().navigate(R.id.action_photoDetailsFragment_to_photosFragment)
        }
    }

    private fun lockFab() {
        requireActivity().findViewById<FloatingActionButton>(R.id.fab).visibility = View.INVISIBLE
    }
}