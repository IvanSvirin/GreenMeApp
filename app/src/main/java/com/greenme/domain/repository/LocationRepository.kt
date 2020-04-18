package com.greenme.domain.repository

import android.location.Location
import com.greenme.domain.model.Plant
import io.reactivex.Single

interface LocationRepository {
    fun get(): Single<Location>
}