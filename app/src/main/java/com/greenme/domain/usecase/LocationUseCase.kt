package com.greenme.domain.usecase

import android.location.Location
import com.greenme.domain.repository.LocationRepository
import io.reactivex.Single

class LocationUseCase constructor(
    private val locationRepository: LocationRepository
) {

    fun get(): Single<Location> =
        locationRepository.get()
}
