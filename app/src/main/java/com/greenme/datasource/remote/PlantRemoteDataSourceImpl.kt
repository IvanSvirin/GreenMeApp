package com.greenme.datasource.remote

import com.greenme.data.datasource.PlantRemoteDataSource
import com.greenme.datasource.model.mapToData
import com.greenme.datasource.model.mapToDomain
import com.greenme.domain.model.Plant
import io.reactivex.Single

class PlantRemoteDataSourceImpl constructor(
    private val api: PlantsApi
) : PlantRemoteDataSource {

    override fun get(): Single<List<Plant>> =
        api.getPlants()
            .map { it.mapToDomain() }

    override fun get(id: String): Single<Plant> =
        api.getPlant(id)
            .map { it.mapToDomain() }

    override fun set(plant: Plant): Single<String> {
        return api.setPlant(plant.mapToData())
    }

}