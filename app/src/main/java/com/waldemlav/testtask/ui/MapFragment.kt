package com.waldemlav.testtask.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.navigation.NavigationView
import com.waldemlav.testtask.R
import com.waldemlav.testtask.databinding.FragmentMapBinding
import com.waldemlav.testtask.ui.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapFragment : Fragment() {

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val supportMapFragment = SupportMapFragment.newInstance()
        childFragmentManager.beginTransaction().replace(R.id.map_fragment, supportMapFragment)
            .commit()

        supportMapFragment.getMapAsync {
            with(it) {
                viewModel.photos.observe(viewLifecycleOwner) { list ->
                    if (list.isNotEmpty()) {
                        list.forEach { photo ->
                            addMarker(MarkerOptions().position(LatLng(photo.lat, photo.lng)))
                        }
                        moveCamera(CameraUpdateFactory.newLatLngZoom(
                            LatLng(list.first().lat, list.first().lng), 5F))
                    }
                }
            }
        }

        setMapMenuItemChecked()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setMapMenuItemChecked() {
        requireActivity().findViewById<NavigationView>(R.id.navigationView)
            .menu.findItem(R.id.menu_map).isChecked = true
    }
}