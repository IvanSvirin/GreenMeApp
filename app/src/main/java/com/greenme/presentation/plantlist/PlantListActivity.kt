package com.greenme.presentation.plantlist

import android.Manifest
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.greenme.R
import com.greenme.domain.model.Plant
import com.greenme.domain.di.injectFeature
import com.greenme.util.*
import kotlinx.android.synthetic.main.activity_plant_list.*
import org.koin.androidx.viewmodel.ext.viewModel

class PlantListActivity : AppCompatActivity() {
    val REQUEST_CODE_ASK_PERMISSIONS = 1

    private val vm: PlantListViewModel by viewModel()
    private val itemClick: (Plant) -> Unit =
        { startActivity(Navigation.plantDetails(this, plantId = it.id)) }

    private val adapter = PlantListAdapter(itemClick)
    private val snackBar by lazy {
        Snackbar.make(swipeRefreshLayout, getString(R.string.error), Snackbar.LENGTH_INDEFINITE)
            .setAction(getString(R.string.retry)) { vm.get(refresh = true) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plant_list)

        requestPermissions()
        injectFeature()

        if (savedInstanceState == null) {
            vm.get(refresh = false)
        }

        postsRecyclerView.adapter = adapter

        vm.plants.observe(this, Observer { updatePlants(it) })
        swipeRefreshLayout.setOnRefreshListener { vm.get(refresh = true) }

        fabAdd.setOnClickListener { startActivity(Navigation.addPlant(this)) }
    }

    private fun updatePlants(resource: Resource<List<Plant>>?) {
        resource?.let {
            when (it.state) {
                ResourceState.LOADING -> swipeRefreshLayout.startRefreshing()
                ResourceState.SUCCESS -> swipeRefreshLayout.stopRefreshing()
                ResourceState.ERROR -> swipeRefreshLayout.stopRefreshing()
            }
            it.data?.let { adapter.submitList(it) }
            it.message?.let { snackBar.show() }
        }
    }

    private fun requestPermissions() {
        val requiredSDKPermissions = arrayOfNulls<String>(6)
        requiredSDKPermissions[0] = Manifest.permission.ACCESS_FINE_LOCATION
        requiredSDKPermissions[1] = Manifest.permission.WRITE_EXTERNAL_STORAGE
        requiredSDKPermissions[2] = Manifest.permission.INTERNET
        requiredSDKPermissions[3] = Manifest.permission.ACCESS_WIFI_STATE
        requiredSDKPermissions[4] = Manifest.permission.ACCESS_NETWORK_STATE
        requiredSDKPermissions[5] = Manifest.permission.ACCESS_COARSE_LOCATION
        ActivityCompat.requestPermissions(
            this,
            requiredSDKPermissions,
            REQUEST_CODE_ASK_PERMISSIONS
        )
    }
}