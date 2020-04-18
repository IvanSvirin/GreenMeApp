package com.greenme.util

import android.content.Context
import android.content.Intent

object Navigation : DynamicFeature<Intent> {

    const val PLANT_ID_KEY = "PLANT_ID_KEY"

    private const val PLANT_LIST = "com.greenme.presentation.plantlist.PlantListActivity"
    private const val PLANT_DETAILS = "com.greenme.presentation.plantdetails.PlantDetailsActivity"
    private const val ADD_PLANT = "com.greenme.presentation.addplant.AddPlantActivity"

    override val dynamicStart: Intent?
        get() = null

    fun plantDetails(context: Context, plantId: String): Intent? =
        PLANT_DETAILS.loadIntentOrNull(context)
            ?.apply {
                putExtra(PLANT_ID_KEY, plantId)
            }

    fun addPlant(context: Context): Intent? =
        ADD_PLANT.loadIntentOrNull(context)
            ?.apply {
            }
}