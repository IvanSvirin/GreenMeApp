package com.greenme.datasource.remote

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.google.android.gms.tasks.OnSuccessListener
import com.greenme.data.datasource.LocationRemoteDataSource
import io.reactivex.Single
import java.util.concurrent.TimeUnit

class LocationRemoteDataSourceImpl constructor(private val context: Context) :
    LocationRemoteDataSource {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var mLocationCallback: LocationCallback
    private var location: Location? = null

    override fun get(): Single<Location> {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    this.location = location
                } else {
                    val mLocationRequest = LocationRequest.create()
                    mLocationRequest.setInterval(1000)
                    mLocationRequest.setFastestInterval(1000)
                    mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                    mLocationCallback = object : LocationCallback() {
                        override fun onLocationResult(locationResult: LocationResult?) {
                            if (locationResult == null) {
                                return
                            }
                            for (location1 in locationResult!!.getLocations()) {
                            }
                        }
                    }
                    fusedLocationClient.requestLocationUpdates(
                        mLocationRequest,
                        mLocationCallback,
                        null
                    )
                    fusedLocationClient.lastLocation
                        .addOnSuccessListener { location2: Location? ->
                            if (location2 != null) {
                                this.location = location2
                            }
                        }
                }
            }
        return Single.timer(2, TimeUnit.SECONDS).map { t: Long -> this.location }
    }
}