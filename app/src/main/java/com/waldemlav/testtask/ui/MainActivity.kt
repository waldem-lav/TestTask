package com.waldemlav.testtask.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Base64
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.Task
import com.waldemlav.testtask.R
import com.waldemlav.testtask.data.network.model.ImageDtoIn
import com.waldemlav.testtask.databinding.ActivityMainBinding
import com.waldemlav.testtask.ui.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.InputStream
import java.util.*

private const val REQUEST_CHECK_SETTINGS = 0x1

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    private var coord: Pair<Double?, Double?> = Pair(null, null) // lat, lon
    private lateinit var imageUri: Uri
    private val fusedLocationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(this)
    }
    private var cancellationTokenSource = CancellationTokenSource()

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()) {}

    private val viewModel: MainViewModel by viewModels()

    private val takePicture = registerForActivityResult(
        ActivityResultContracts.TakePicture()) { success: Boolean ->

        if (success) {
            val base64Image = encodeImageToBase64String(imageUri)
            val timestamp = Calendar.getInstance(TimeZone.getDefault()).timeInMillis / 1000
            viewModel.uploadImage(ImageDtoIn(base64Image, timestamp, coord.first!!, coord.second!!))
        }
        else
            Toast.makeText(this, "Unknown error", Toast.LENGTH_LONG).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityMainBinding = DataBindingUtil.setContentView(
            this, R.layout.activity_main)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_photos -> {
                    if (!menuItem.isChecked)
                        navController.navigate(R.id.action_mapFragment_to_photosFragment)
                    binding.drawerLayout.close()
                    true
                }
                else -> {
                    if (!menuItem.isChecked)
                        navController.navigate(R.id.action_photosFragment_to_mapFragment)
                    binding.drawerLayout.close()
                    true
                }
            }
        }

        binding.fab.setOnClickListener {
            requestCurrentLocation()

            val photoFile = File.createTempFile(
                "IMG_", ".jpg", this
                    .getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            )
            imageUri = FileProvider.getUriForFile(
                this,
                "com.waldemlav.testtask.fileprovider",
                photoFile
            )

            // Check camera and Location permissions
            if (ContextCompat.checkSelfPermission(
                    this, Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                    this, Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                    this, Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                // Check if GPS is on
                val locationRequest = LocationRequest.create().setPriority(PRIORITY_HIGH_ACCURACY)
                val builder = LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest)
                LocationServices.getSettingsClient(this).checkLocationSettings(builder.build())
                    .addOnSuccessListener {
                        // Check if coord are already available
                        if (coord != Pair(null, null))
                            takePicture.launch(imageUri)
                        else
                            Toast.makeText(this, "Loading location...\n" +
                                    "Please try again", Toast.LENGTH_LONG).show()

                    }
                    .addOnFailureListener {
                        if (it is ResolvableApiException){
                            // Location settings are not satisfied, but this can be fixed
                            // by showing the user a dialog.
                            try {
                                it.startResolutionForResult(this, REQUEST_CHECK_SETTINGS)
                            } catch (sendEx: IntentSender.SendIntentException) {
                                // Ignore the error.
                            }
                        }
                    }
            } else {
                requestPermissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
                Toast.makeText(this, "Sorry, you can't take photo without " +
                        "camera and location permissions", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun encodeImageToBase64String(imageUri: Uri): String {
        val imageStream: InputStream = this.contentResolver
            .openInputStream(imageUri)!!
        val bitmap = BitmapFactory.decodeStream(imageStream)
        val resized = Bitmap.createScaledBitmap(bitmap, 1280, 1280, true)
        val baos = ByteArrayOutputStream()
        resized.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val byteArray = baos.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    @SuppressLint("MissingPermission")
    private fun requestCurrentLocation() {
        val currentLocationTask: Task<Location> = fusedLocationClient.getCurrentLocation(
            PRIORITY_HIGH_ACCURACY,
            cancellationTokenSource.token
        )
        currentLocationTask.addOnCompleteListener { task: Task<Location> ->
            if (task.isSuccessful) {
                val result: Location = task.result
                coord = Pair(result.latitude, result.longitude)
            }
        }
    }

    override fun onStop() {
        super.onStop()
        // Cancels location request
        cancellationTokenSource.cancel()
    }
}