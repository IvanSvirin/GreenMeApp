package com.greenme.data.repository

import android.location.Location
import com.greenme.data.datasource.LocationRemoteDataSource
import com.greenme.datasource.remote.LocationRemoteDataSourceImpl
import com.greenme.domain.repository.LocationRepository
import io.reactivex.Single

class LocationRepositoryImpl constructor(private val locationRemoteDataSource: LocationRemoteDataSource) :
    LocationRepository {
    override fun get(): Single<Location> =
        locationRemoteDataSource.get()

}