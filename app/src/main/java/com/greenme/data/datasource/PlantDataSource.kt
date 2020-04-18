package com.greenme.data.datasource

import com.greenme.domain.model.Plant
import io.reactivex.Single

interface PlantCacheDataSource {

    fun get(): Single<List<Plant>>

    fun set(item: Plant): Single<Plant>

    fun get(id: String): Single<Plant>

    fun set(list: List<Plant>): Single<List<Plant>>
}

interface PlantRemoteDataSource {

    fun get(): Single<List<Plant>>

    fun get(id: String): Single<Plant>

    fun set(plant: Plant): Single<String>

}