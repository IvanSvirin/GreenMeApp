package com.greenme.presentation.plantdetails

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.greenme.R
import com.greenme.domain.model.Plant
import com.greenme.domain.di.injectFeature
import com.greenme.util.Navigation.PLANT_ID_KEY
import com.greenme.util.Resource
import com.greenme.util.ResourceState
import com.greenme.util.startRefreshing
import com.greenme.util.stopRefreshing
import kotlinx.android.synthetic.main.activity_plant_list.*
import org.koin.androidx.viewmodel.ext.viewModel

class PlantDetailsActivity : AppCompatActivity() {

    private val vm: PlantDetailsViewModel by viewModel()
    private var id: String = ""

    private val snackBar by lazy {
        Snackbar.make(swipeRefreshLayout, getString(R.string.error), Snackbar.LENGTH_INDEFINITE)
            .setAction(getString(R.string.retry)) { vm.get(id, refresh = true) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plant_details)
        title = "Plant Details"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        injectFeature()

        id = intent.getStringExtra(PLANT_ID_KEY)
        if (savedInstanceState == null) {
            vm.get(id, refresh = false)
        }


        vm.plant.observe(this, Observer { updatePlant(it) })
        swipeRefreshLayout.setOnRefreshListener { vm.get(id, refresh = true) }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun updatePlant(resource: Resource<Plant>?) {
        resource?.let {
            when (it.state) {
                ResourceState.LOADING -> swipeRefreshLayout.startRefreshing()
                ResourceState.SUCCESS -> swipeRefreshLayout.stopRefreshing()
                ResourceState.ERROR -> swipeRefreshLayout.stopRefreshing()
            }
            it.message?.let { snackBar.show() }
        }
    }
}