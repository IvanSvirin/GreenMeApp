package com.greenme.presentation.plantdetails

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.greenme.domain.model.Plant
import com.greenme.domain.usecase.PlantUseCase
import com.greenme.util.Resource
import com.greenme.util.setError
import com.greenme.util.setLoading
import com.greenme.util.setSuccess
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class PlantDetailsViewModel constructor(private val plantUseCase: PlantUseCase) :
    ViewModel() {

    val plant = MutableLiveData<Resource<Plant>>()
    private val compositeDisposable = CompositeDisposable()

    fun get(id:String, refresh: Boolean = false) =
        compositeDisposable.add(plantUseCase.get(id, refresh)
            .doOnSubscribe { plant.setLoading() }
            .subscribeOn(Schedulers.io())
            .subscribe({ plant.setSuccess(it) }, { plant.setError(it.message) })
        )

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }
}