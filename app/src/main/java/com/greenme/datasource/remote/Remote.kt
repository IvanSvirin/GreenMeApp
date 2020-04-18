package com.greenme.datasource.remote

import com.greenme.datasource.model.PlantEntity
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface PlantsApi {

    @GET("all")
    fun getPlants(): Single<List<PlantEntity>>

    @GET("plants/{id}")
    fun getPlant(@Path("id") id: String): Single<PlantEntity>

    @POST("add")
    fun setPlant(@Body plantEntity: PlantEntity): Single<String>
}