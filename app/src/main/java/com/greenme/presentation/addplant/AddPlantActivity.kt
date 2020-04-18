package com.greenme.presentation.addplant

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.greenme.R
import com.greenme.domain.model.Plant
import com.greenme.domain.di.injectFeature
import com.greenme.util.*
import kotlinx.android.synthetic.main.activity_add_plant.*
import kotlinx.android.synthetic.main.activity_plant_list.swipeRefreshLayout
import org.koin.androidx.viewmodel.ext.viewModel
import java.io.File

class AddPlantActivity : AppCompatActivity() {

    private val vm: AddPlantViewModel by viewModel()
    private var plant: Plant = Plant("", "", "", "", "", 0f, 0f)
    private var imageUri: Uri? = null
    private val requestCodeShoot = 20

    private val snackBar by lazy {
        Snackbar.make(swipeRefreshLayout, getString(R.string.error), Snackbar.LENGTH_INDEFINITE)
            .setAction(getString(R.string.retry)) { vm.put(plant) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_plant)
        title = "Add Plant"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        injectFeature()

        textViewDateValue.text = getStringTimestampFromLong(System.currentTimeMillis())

        buttonAdd.setOnClickListener {
            plant.name = editTextNameValue.text.toString()
            plant.date = System.currentTimeMillis().toString()

            vm.put(plant)
            vm.string.observe(this, Observer { addPlant(it) })
        }
        startLocationService()
        buttonShoot.setOnClickListener { v ->
            run {
                val values = ContentValues()
                values.put(MediaStore.Images.Media.TITLE, "New Picture")
                values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera")
                imageUri =
                    contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
                startActivityForResult(intent, requestCodeShoot)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun addPlant(resource: Resource<String>?) {
        resource?.let {
            when (it.state) {
                ResourceState.LOADING -> swipeRefreshLayout.startRefreshing()
                ResourceState.SUCCESS -> {
                    swipeRefreshLayout.stopRefreshing()
                    finish()
                }
                ResourceState.ERROR -> swipeRefreshLayout.stopRefreshing()
            }
            it.message?.let { snackBar.show() }
        }
    }

    private fun startLocationService() {
        vm.get()
        vm.location.observe(this, Observer { updateLocation(it) })
    }

    private fun updateLocation(resource: Resource<Location>?) {
        resource?.let {
            when (it.state) {
                ResourceState.LOADING -> swipeRefreshLayout.startRefreshing()
                ResourceState.SUCCESS -> {
                    if (it.data!!.latitude == 0.0 && it.data.longitude == 0.0) {
                        vm.get()
                    } else {
                        swipeRefreshLayout.stopRefreshing()
                        plant.latitude = it.data.latitude.toFloat()
                        plant.longitude = it.data.longitude.toFloat()
                        textViewLatitudeValue.text = plant.latitude.toString()
                        textViewLongitudeValue.text = plant.longitude.toString()
                    }
                }
                ResourceState.ERROR -> swipeRefreshLayout.stopRefreshing()
            }
            it.message?.let { snackBar.show() }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCodeShoot == requestCode && resultCode == Activity.RESULT_OK) {
            var bitmap: Bitmap? = null//(Bitmap) data.getExtras().get("data");
            try {
                bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
                val imageUrl: String
                imageUrl = getRealPathFromURI(imageUri!!)
                File(imageUrl).delete()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            if (bitmap != null) {
//                imageViewPhoto.visibility = View.VISIBLE
//                buttonShoot.visibility = View.GONE
//                    val b = Bitmap.createBitmap(
//                        bitmap,
//                        0,
//                        0,
//                        imageViewPhoto.width,
//                        imageViewPhoto.height
//                    )
//                    imageViewPhoto.setImageBitmap(b)
//                plant.photo = (bitmapToBase64(bitmap))
                Glide.with(this)
                    .load(bitmap).into(imageViewPhoto)
            }

        }
    }

    fun getRealPathFromURI(contentUri: Uri): String {
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = managedQuery(contentUri, proj, null, null, null)
        val column_index = cursor
            .getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        return cursor.getString(column_index)
    }

}