package com.greenme.presentation.plantlist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.greenme.domain.model.Plant
import com.greenme.domain.usecase.PlantsUseCase
import com.greenme.util.Resource
import com.greenme.util.setError
import com.greenme.util.setLoading
import com.greenme.util.setSuccess
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class PlantListViewModel constructor(private val plantsUseCase: PlantsUseCase) :
    ViewModel() {

    val plants = MutableLiveData<Resource<List<Plant>>>()
    private val compositeDisposable = CompositeDisposable()

    fun get(refresh: Boolean = false) =
        compositeDisposable.add(plantsUseCase.get(refresh)
            .doOnSubscribe { plants.setLoading() }
            .subscribeOn(Schedulers.io())
            .subscribe({ plants.setSuccess(it) }, { plants.setError(it.message) })
        )

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }
}