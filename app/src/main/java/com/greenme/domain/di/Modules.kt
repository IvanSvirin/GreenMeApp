package com.greenme.domain.di

import android.app.Application
import android.content.Context
import com.greenme.App
import com.greenme.BuildConfig
import com.greenme.data.datasource.LocationRemoteDataSource
import com.greenme.data.datasource.PlantCacheDataSource
import com.greenme.data.datasource.PlantRemoteDataSource
import com.greenme.data.repository.LocationRepositoryImpl
import com.greenme.data.repository.PlantRepositoryImpl
import com.greenme.datasource.cache.PlantCacheDataSourceImpl
import com.greenme.datasource.cache.ReactiveCache
import com.greenme.datasource.model.PlantEntity
import com.greenme.datasource.remote.LocationRemoteDataSourceImpl
import com.greenme.datasource.remote.PlantRemoteDataSourceImpl
import com.greenme.datasource.remote.PlantsApi
import com.greenme.datasource.remote.createNetworkClient
import com.greenme.domain.repository.LocationRepository
import com.greenme.domain.repository.PlantRepository
import com.greenme.domain.usecase.AddPlantUseCase
import com.greenme.domain.usecase.LocationUseCase
import com.greenme.domain.usecase.PlantUseCase
import com.greenme.domain.usecase.PlantsUseCase
import com.greenme.presentation.addplant.AddPlantViewModel
import com.greenme.presentation.plantdetails.PlantDetailsViewModel
import com.greenme.presentation.plantlist.PlantListViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import retrofit2.Retrofit

fun injectFeature() = loadFeature

private val loadFeature by lazy {
    loadKoinModules(
        viewModelModule,
        useCaseModule,
        repositoryModule,
        dataSourceModule,
        networkModule,
        cacheModule
    )
}

val viewModelModule: Module = module {
    viewModel { PlantListViewModel(plantsUseCase = get()) }
    viewModel { PlantDetailsViewModel(plantUseCase = get()) }
    viewModel { AddPlantViewModel(addPlantUseCase = get(), locationUseCase = get()) }
}

val useCaseModule: Module = module {
    factory { PlantsUseCase(plantRepository = get()) }
    factory { PlantUseCase(plantRepository = get()) }
    factory { AddPlantUseCase(plantRepository = get()) }
    factory { LocationUseCase(locationRepository = get()) }
}

val repositoryModule: Module = module {
    single {
        PlantRepositoryImpl(
            cacheDataSource = get(),
            remoteDataSource = get()
        ) as PlantRepository
    }
    single { LocationRepositoryImpl(locationRemoteDataSource = get()) as LocationRepository }
}

val dataSourceModule: Module = module {
    single { PlantCacheDataSourceImpl(cache = get(PLANT_CACHE)) as PlantCacheDataSource }
    single { PlantRemoteDataSourceImpl(api = plantsApi) as PlantRemoteDataSource }
    single { LocationRemoteDataSourceImpl(context = androidContext()) as LocationRemoteDataSource }
}

val networkModule: Module = module {
    single { plantsApi }
}

val cacheModule: Module = module {
    single(name = PLANT_CACHE) { ReactiveCache<List<PlantEntity>>() }
}

private const val BASE_URL = "http://185.159.130.50/green/"

private val retrofit: Retrofit = createNetworkClient(
    BASE_URL,
    BuildConfig.DEBUG
)

private val plantsApi: PlantsApi = retrofit.create(PlantsApi::class.java)

private const val PLANT_CACHE = "PLANT_CACHE"

