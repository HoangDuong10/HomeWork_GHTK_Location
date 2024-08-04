package com.example.homework_ghtk_location

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.homework_ghtk_location.databinding.ActivityMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val requestPermissionsLauncher: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val allPermissionsGranted = permissions[ACCESS_FINE_LOCATION] == true
            if (allPermissionsGranted) {
                getCurrentLocation()
                // Quyền đã được cấp, bạn có thể thực hiện các hành động yêu cầu quyền truy cập vị trí
                Toast.makeText(this,"Location permission granted.",Toast.LENGTH_SHORT).show()
            } else {
                // Quyền chưa được cấp
                Toast.makeText(this,"Location permission is not granted.",Toast.LENGTH_SHORT).show()
            }
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        binding.btnStartPermission.setOnClickListener{
            checkAndRequestLocationPermission()
        }
    }

    private fun getCurrentLocation() {
        if (ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    val latitude = location.latitude
                    val longitude = location.longitude
                    binding.tvLat.text = "Lat:${latitude}"
                    binding.tvLng.text = "Lng:${longitude}"
                    binding.tvLat.visibility = View.VISIBLE
                    binding.tvLng.visibility = View.VISIBLE
                    binding.btnStartPermission.setBackgroundColor(ContextCompat.getColor(this, R.color.green))
                    binding.tvStatusPermission.text = "Granted"
                    binding.tvStatusPermission.setTextColor(ContextCompat.getColor(this,R.color.green))
                } else {
                    Toast.makeText(this,"Location not available",Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(this,"Location permission is not granted.",Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkAndRequestLocationPermission() {
        val locationPermission = ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION)
        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            // Yêu cầu quyền nếu chưa được cấp
            requestPermissionsLauncher.launch(arrayOf(ACCESS_FINE_LOCATION))
        } else {
            // Quyền đã được cấp, thực hiện hành động cần thiết
            getCurrentLocation()
        }
    }
}