package com.greenme.presentation.addplant

import android.location.Location
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.greenme.domain.model.Plant
import com.greenme.domain.usecase.AddPlantUseCase
import com.greenme.domain.usecase.LocationUseCase
import com.greenme.util.Resource
import com.greenme.util.setError
import com.greenme.util.setLoading
import com.greenme.util.setSuccess
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class AddPlantViewModel constructor(private val addPlantUseCase: AddPlantUseCase, private val locationUseCase: LocationUseCase) :
    ViewModel() {
    val location = MutableLiveData<Resource<Location>>()
    private val compositeDisposableLocation = CompositeDisposable()

    val string = MutableLiveData<Resource<String>>()
    private val compositeDisposable = CompositeDisposable()


    fun get() =
        compositeDisposableLocation.add(locationUseCase.get()
            .doOnSubscribe { location.setLoading() }
            .subscribeOn(Schedulers.io())
            .subscribe({ location.setSuccess(it) }, { location.setError(it.message) })
        )

    fun put(plant: Plant) =
        compositeDisposable.add(addPlantUseCase.put(plant)
            .doOnSubscribe { string.setLoading() }
            .subscribeOn(Schedulers.io())
            .subscribe({ string.setSuccess(it) }, { string.setError(it.message) })
        )

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }
}