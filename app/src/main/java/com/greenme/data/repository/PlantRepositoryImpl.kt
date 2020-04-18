package com.greenme.data.repository

import com.greenme.data.datasource.PlantCacheDataSource
import com.greenme.data.datasource.PlantRemoteDataSource
import com.greenme.domain.model.Plant
import com.greenme.domain.repository.PlantRepository
import io.reactivex.Single

class PlantRepositoryImpl constructor(
    private val cacheDataSource: PlantCacheDataSource,
    private val remoteDataSource: PlantRemoteDataSource
) : PlantRepository {

    override fun get(refresh: Boolean): Single<List<Plant>> =
        when (refresh) {
            true -> remoteDataSource.get()
                .flatMap { cacheDataSource.set(it) }
            false -> cacheDataSource.get()
                .onErrorResumeNext { get(true) }
        }

    override fun get(id: String, refresh: Boolean): Single<Plant> =
        when (refresh) {
            true -> remoteDataSource.get(id)
                .flatMap { cacheDataSource.set(it) }
            false -> cacheDataSource.get(id)
                .onErrorResumeNext { get(id, true) }
        }

    override fun put(plant: Plant): Single<String> =
        remoteDataSource.set(plant)
//            .flatMap { cacheDataSource.set(plant) }


}