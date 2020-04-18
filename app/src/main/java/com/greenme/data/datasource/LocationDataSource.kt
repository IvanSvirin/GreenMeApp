package com.greenme.data.datasource

import android.location.Location
import io.reactivex.Single

interface LocationRemoteDataSource {
    fun get():Single<Location>
}