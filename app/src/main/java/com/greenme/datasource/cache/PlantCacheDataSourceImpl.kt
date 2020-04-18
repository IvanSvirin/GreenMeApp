package com.greenme.datasource.cache

import com.greenme.data.datasource.PlantCacheDataSource
import com.greenme.domain.model.Plant
import io.reactivex.Single

class PlantCacheDataSourceImpl constructor(
    private val cache: ReactiveCache<List<Plant>>
) : PlantCacheDataSource {

    val key = "Plant List"

    override fun get(): Single<List<Plant>> =
        cache.load(key)

    override fun set(list: List<Plant>): Single<List<Plant>> =
        cache.save(key, list)

    override fun get(id: String): Single<Plant> =
        cache.load(key)
            .map { it.first { it.id.equals(id) } }

    override fun set(item: Plant): Single<Plant> =
        cache.load(key)
            .map { it.filter { it.id != item.id }.plus(item) }
            .flatMap { set(it) }
            .map { item }
}