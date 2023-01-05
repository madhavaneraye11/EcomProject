package com.spam.arcgisgithub

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.esri.arcgisruntime.ArcGISRuntimeEnvironment
import com.esri.arcgisruntime.mapping.ArcGISMap
import com.esri.arcgisruntime.mapping.BasemapStyle
import com.esri.arcgisruntime.mapping.view.LocationDisplay
import com.esri.arcgisruntime.mapping.view.MapView
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {

    private lateinit var mapView: MapView
    private lateinit var locationDisplay: LocationDisplay

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar()?.hide() // hide the title bar
        this.getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        ); //enable full screen
        setContentView(R.layout.activity_main)

        //This key use only for development
        ArcGISRuntimeEnvironment.setApiKey("AAPKe9663900def147d489a9bf1bba8f5b5dDiRxgAxeCdhugmP3iBlYfJsA2HXM0f7GUhkSV18ytpCuqJyTXK9RJhqCtqq8WshF")
        mapView=findViewById(R.id.mapView)
        val l=mapView.mapScale
        locationDisplay= mapView.locationDisplay
        setupMap()

    }

    // set up your map here. You will call this method from onCreate()
    private fun setupMap() {
        // create a map with the BasemapStyle streets
        val map = ArcGISMap(BasemapStyle.ARCGIS_IMAGERY) // this code and line change UI and  view of map
        // set the map to be displayed in the layout's MapView
        mapView.map = map
        // set the viewpoint, Viewpoint(latitude, longitude, scale)
        //     mapView.setViewpoint(Viewpoint(19.041900, 77.643400, 72000.0))

        locationDisplay.addDataSourceStatusChangedListener {
            // if LocationDisplay isn't started or has an error
            if (!it.isStarted && it.error != null) {
                // check permissions to see if failure may be due to lack of permissions
                requestPermissions(it)

            }

        }

        locationDisplay= mapView.locationDisplay

        map.addLoadStatusChangedListener {
            Log.d("print1","print")
        }

        locationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.RECENTER)

        locationDisplay.startAsync()

        if(locationDisplay.startAsync().equals("true")){
            if (!locationDisplay.isStarted){
                locationDisplay.startAsync()

                val c=mapView.mapScale
                Log.d("ZoomLevel",c.toString())
                //  mapView.setViewpointScaleAsync(c+2500.00)
                mapView.setViewpointScaleAsync(c)
                locationDisplay.autoPanMode= LocationDisplay.AutoPanMode.RECENTER

                var hAcc=locationDisplay.location.horizontalAccuracy.toString()
                var vAcc=locationDisplay.location.verticalAccuracy.toString()
                val df = DecimalFormat("#")
            //    accuracyTextView.setText(Math.round(hAcc.toDouble()).toString()+"M")
                locationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.RECENTER)
            }

            if (locationDisplay.isStarted){
                val c=mapView.mapScale
                Log.d("ZoomLevel",c.toString())
                //  mapView.setViewpointScaleAsync(c+2500.00)
                mapView.setViewpointScaleAsync(c)
                val hAcc=locationDisplay.location.horizontalAccuracy.toString()
                var vAcc=locationDisplay.location.verticalAccuracy.toString()
                val df = DecimalFormat("#")
             //   accuracyTextView.setText(Math.round(hAcc.toDouble()).toString()+"M")
                locationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.RECENTER)
            }
        }

    }


    private fun requestPermissions(dataSourceStatusChangedEvent: LocationDisplay.DataSourceStatusChangedEvent) {
        val requestCode = 2
        val reqPermissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        // fine location permission
        val permissionCheckFineLocation =
            ContextCompat.checkSelfPermission(this@MainActivity, reqPermissions[0]) ==
                    PackageManager.PERMISSION_GRANTED
        // coarse location permission
        val permissionCheckCoarseLocation =
            ContextCompat.checkSelfPermission(this@MainActivity, reqPermissions[1]) ==
                    PackageManager.PERMISSION_GRANTED
        if (!(permissionCheckFineLocation && permissionCheckCoarseLocation)) { // if permissions are not already granted, request permission from the user
            ActivityCompat.requestPermissions(this@MainActivity, reqPermissions, requestCode)
        } else {
            // report other unknown failure types to the user - for example, location services may not
            // be enabled on the device.
            val message = String.format(
                "Error in DataSourceStatusChangedListener: %s", dataSourceStatusChangedEvent
                    .source.locationDataSource.error.message
            )
            Log.d("errormg",
                dataSourceStatusChangedEvent.source.locationDataSource.error.message.toString()
            )
            // Toast.makeText(this@MainActivity, message, Toast.LENGTH_LONG).show()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // if request is cancelled, the results array is empty
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            locationDisplay.startAsync()
        } else {
            //     Toast.makeText(this@MainActivity, resources.getString(R.string.location_permission_denied), Toast.LENGTH_SHORT).show()
            Log.d("Error",resources.getString(R.string.location_permission_denied))
        }

    }


}