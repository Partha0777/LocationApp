package com.curiozing.locationapp

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Looper
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import java.security.Permission
import java.util.Locale

const val NO_ADDRESS_FOUND = "No Address Found"
class LocationUtils(var context: Context) {

    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)


    @Suppress("MissingPermission")
    fun requestUpdatedLocation(locationViewModel: LocationViewModel) {
        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            1000
        ).build()

        val callBack = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation?.let {
                    val locationData = LocationData(it.latitude, it.longitude)
                    locationViewModel.update(locationData)
                }
            }
        }

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            callBack,
            Looper.getMainLooper()
        )
    }

    fun hasLocationPermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun getAddressFromLatLng(locationData: LocationData): String {
        val geocoder = Geocoder(context, Locale.getDefault())
        val coordinator = LatLng(locationData.latitude, locationData.longitude)

        val addresses: MutableList<Address>? =
            geocoder.getFromLocation(coordinator.latitude, coordinator.longitude, 1)

        addresses?.let {
            return it[0].toString()
        }
        return NO_ADDRESS_FOUND
    }
}