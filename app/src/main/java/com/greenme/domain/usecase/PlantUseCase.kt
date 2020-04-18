package com.greenme.domain.usecase

import com.greenme.domain.model.Plant
import com.greenme.domain.repository.PlantRepository
import io.reactivex.Single

class PlantsUseCase constructor(
    private val plantRepository: PlantRepository
) {

    fun get(refresh: Boolean): Single<List<Plant>> =
        plantRepository.get(refresh)
}

class PlantUseCase constructor(
    private val plantRepository: PlantRepository
) {

    fun get(id: String, refresh: Boolean): Single<Plant> =
        plantRepository.get(id, refresh)
}
class AddPlantUseCase constructor(
    private val plantRepository: PlantRepository
) {

    fun put(plant: Plant): Single<String> =
        plantRepository.put(plant)
}