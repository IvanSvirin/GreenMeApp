package com.greenme.domain.repository

import com.greenme.domain.model.Plant
import io.reactivex.Single

interface PlantRepository {
    fun get(refresh: Boolean): Single<List<Plant>>

    fun get(id: String, refresh: Boolean): Single<Plant>

    fun put(plant: Plant): Single<String>
}