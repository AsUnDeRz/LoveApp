package toche.asunder.loveapp.fragment

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PorterDuff
import android.location.Location
import android.os.Bundle
import android.support.annotation.DrawableRes
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.places.Places
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.header_logo_white.*
import kotlinx.android.synthetic.main.lab.*
import toche.asunder.loveapp.MyApp
import toche.asunder.loveapp.PermissionUtils
import toche.asunder.loveapp.R

/**
 * Created by admin on 8/1/2017 AD.
 */
class LabFragment : Fragment(),GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks,
        OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener,
        LocationListener,
        GoogleMap.OnMarkerClickListener{
    override fun onMarkerClick(marker: Marker?): Boolean {
        googleMap?.clear()
        val mark = MarkerOptions()
                .position(marker!!.position)
                .title("I am Here")
        googleMap?.addMarker(mark)

        return false
    }

    override fun onLocationChanged(location: Location?) {
        handleNewLocation(location)


    }

    lateinit var mGoogleApiClient: GoogleApiClient
    var googleMap : GoogleMap? = null
    val PLACE_PICKER_REQUEST = 1
    lateinit var mLocationRequest : LocationRequest
    var mPermissionDenied = false
    var location : Location? = null
    lateinit var options : MarkerOptions


    fun handleNewLocation(location: Location?) {

        val currentLatitude = location!!.latitude
        val currentLongitude = location!!.longitude
        val zoomLevel = 16.0f //This goes up to 21
        val latLng = LatLng(currentLatitude, currentLongitude)

        //mMap.addMarker(new MarkerOptions().position(new LatLng(currentLatitude, currentLongitude)).title("Current Location"));
        options = MarkerOptions()
                .position(latLng)
                .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(R.drawable.icon_account)))


        //googleMap?.setOnMarkerClickListener(this)
        googleMap?.addMarker(options)
        googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoomLevel))
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
    }

    override fun onConnected(p0: Bundle?) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(activity, 123,Manifest.permission.ACCESS_FINE_LOCATION, true)
        } else if (mLocationRequest != null) {
            // Access to the location has been granted to the app.
            location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient)

            if (location == null) {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this)
            } else {
                handleNewLocation(location)
            }

        }
    }

    override fun onConnectionSuspended(p0: Int) {
    }

    override fun onMapReady(map: GoogleMap?) {
        googleMap = map
        googleMap?.setOnMyLocationButtonClickListener(this)
        enableMyLocation()
    }

    override fun onMyLocationButtonClick(): Boolean {
        return false
    }

    companion object {
        fun newInstance(): LabFragment {
            return LabFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mGoogleApiClient = GoogleApiClient.Builder(context)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .enableAutoManage(activity, this)
                .build()

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.lab, container, false)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView.onCreate(savedInstanceState)
        title_app.typeface =MyApp.typeFace.heavy
        title_app.text ="NEAR\nBY PLACE"


    }



    override fun onResume() {
        super.onResume()
        mGoogleApiClient.connect()
        if (mPermissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError()
            mPermissionDenied = false
        }
        mapView.onResume()
        try {
            MapsInitializer.initialize(activity.applicationContext)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        mapView.getMapAsync(this)


    }

    override fun onPause() {
        super.onPause()
        //LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this)

        mapView.onPause()

    }

    override fun onDestroy() {
        super.onDestroy()
        mGoogleApiClient.stopAutoManage(activity)
        mGoogleApiClient.disconnect()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    private fun showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(fragmentManager, "dialog")
    }

    private fun enableMyLocation() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(activity, 123, Manifest.permission.ACCESS_FINE_LOCATION, true)
        } else if (googleMap != null) {
            // Access to the location has been granted to the app.

            //googleMap!!.isMyLocationEnabled = true
            if(mGoogleApiClient.isConnected && location != null){
                val latLng = LatLng(location!!.latitude,location!!.longitude)
                val options =  MarkerOptions()
                        .position(latLng)
                        .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(R.drawable.icon_account)))
                googleMap?.addMarker(options)
                googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,16.0f))

            }


        }
    }




    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode != 123) {
            return
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            //enableMyLocation()
            /*
            val result = Places.PlaceDetectionApi.getCurrentPlace(mGoogleApiClient, null)
            result.setResultCallback { likelyPlaces ->
                for (placeLikelihood in likelyPlaces) {
                    d{String.format("Place " +placeLikelihood.place.name +placeLikelihood.likelihood)}
                }
                likelyPlaces.release()
            }
            */
        } else {
            // Display the missing permission error dialog when the fragments resume.
            mPermissionDenied = true
        }
    }

     fun getMarkerBitmapFromView(resId :Int) :Bitmap {

        val layout = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
         val customMarkerView = layout.inflate(R.layout.custom_marker, null)
        val markerImageView = customMarkerView.findViewById<ImageView>(R.id.profile_image)
         val nameClinic = customMarkerView.findViewById<TextView>(R.id.txt_name)
         val descClinic = customMarkerView.findViewById<TextView>(R.id.txt_desc)

         nameClinic.typeface = MyApp.typeFace.heavy
         descClinic.typeface = MyApp.typeFace.medium

         markerImageView.setImageResource(resId)
        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        customMarkerView.layout(0, 0, 500, 200)
        customMarkerView.buildDrawingCache()
        val returnedBitmap = Bitmap.createBitmap(500, 200,
                Bitmap.Config.ARGB_8888)
        val canvas = Canvas(returnedBitmap)
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN)
        val drawable = customMarkerView.background
        if (drawable != null)
            drawable.draw(canvas)
        customMarkerView.draw(canvas)
        return returnedBitmap
    }

}