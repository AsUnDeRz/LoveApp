package asunder.toche.loveapp

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.databinding.ObservableArrayList
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PorterDuff
import android.location.Location
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.github.ajalt.timberkt.Timber.d
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
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg
import kotlin.properties.Delegates


/**
 * Created by admin on 8/1/2017 AD.
 */

class LabFragment : Fragment(),GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks,
        OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener,
        LocationListener,
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnInfoWindowCloseListener,
        GoogleMap.OnInfoWindowClickListener {

    val dataClinic = ObservableArrayList<Model.Clinic>().apply {
        add(Model.Clinic(123213, "Test 1", "Test snippet", "10-20", R.drawable.clinic_img))
        add(Model.Clinic(123213, "Test 2", "Test snippet", "10-20", R.drawable.clinic_img))

    }


    //override activity/fragment
    lateinit var mGoogleApiClient: GoogleApiClient
    var googleMap: GoogleMap? = null
    val PLACE_PICKER_REQUEST = 1
    lateinit var mLocationRequest: LocationRequest
    var location: Location? = null
    lateinit var options: MarkerOptions
    var mPermissionDenied = false

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
                .setFastestInterval(1 * 1000) // 1 second, in milliseconds
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        d { "Visible to user =$isVisibleToUser" }

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.lab, container, false)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView.onCreate(savedInstanceState)

        //Custom wording and font
        title_app.typeface = MyApp.typeFace.heavy
        title_app.text = "NEAR\nBY PLACE"
        txt_search.typeface = MyApp.typeFace.medium
        txt_search.hint = "Search..."
        btn_showlist.setOnClickListener {
            ActivityMain.vp_main.setCurrentItem(KEYPREFER.CLINIC, false)
        }


    }


    override fun onResume() {
        super.onResume()
        d {
            "onResume" +
                    "mGoogleApiclient connect" +
                    "Check Permission" +
                    "mapView onResume" +
                    "Maps Initializer" +
                    "MapView getMapAsync"
        }
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
        d {
            "onPase" +
                    "mapView onPase"
        }
        //LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this)
        mapView.onPause()

    }

    override fun onDestroy() {
        super.onDestroy()
        d {
            "onDestroy" +
                    "mGoogleApiClient stopAutomanager and disconnect"
        }
        mGoogleApiClient.stopAutoManage(activity)
        mGoogleApiClient.disconnect()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }
    //end override

    //implement and listener google Map

    override fun onMapReady(map: GoogleMap?) {
        d { "onMap Ready" }
        googleMap = map
        googleMap?.setInfoWindowAdapter(InfoMapAdapter(dataClinic, activity))
        googleMap?.setOnMyLocationButtonClickListener(this)
        googleMap?.setOnInfoWindowClickListener(this)
        googleMap?.setOnInfoWindowCloseListener(this)
        enableMyLocation()
    }
    override fun onInfoWindowClose(marker: Marker?) {
        Toast.makeText(context, "I'am close " + marker?.title, Toast.LENGTH_LONG).show()
    }

    override fun onInfoWindowClick(marker: Marker?) {
        Toast.makeText(context, "I'am click " + marker?.title, Toast.LENGTH_LONG).show()
    }

    override fun onLocationChanged(location: Location?) {
        d { "onLocation Changed" }
        handleNewLocation(location)


    }

    override fun onConnected(p0: Bundle?) {
        d { "onConntected" }
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(activity, 123, Manifest.permission.ACCESS_FINE_LOCATION, true)
        } else if (mLocationRequest != null) {
            d { "mLocationRequest = null" }
            // Access to the location has been granted to the app.
            location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient)

            if (location == null) {
                d { "Location = null and request" }
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this)
            } else {
                d { "Location != null" }
                handleNewLocation(location)
            }

        }
    }
    override fun onConnectionFailed(p0: ConnectionResult) {
    }

    override fun onMarkerClick(marker: Marker?): Boolean {
        return false
    }

    override fun onConnectionSuspended(p0: Int) {
    }

    override fun onMyLocationButtonClick(): Boolean {
        return false
    }







    //function

    fun handleNewLocation(location: Location?) {
        d { "handle New location [Current location]" }
        for(i in 0..1){
            addMarker(location!!.latitude+i,location!!.longitude+i,i)
        }
    }

    private fun showMissingPermissionError() {
        d { "ShowMissingPermissionError" }
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(fragmentManager, "dialog")
    }

    fun enableMyLocation() {
        d { "enableMyLocation" }
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            d { "check permissionutils" }
            PermissionUtils.requestPermission(activity, 123, Manifest.permission.ACCESS_FINE_LOCATION, true)
        } else if (googleMap != null) {
            // Access to the location has been granted to the app.
            //googleMap!!.isMyLocationEnabled = true
            if (mGoogleApiClient.isConnected && location != null) {
                for(i in 0..1){
                    addMarker(location!!.latitude+i,location!!.longitude+i,i)
                }
            }


        }
    }


    fun addMarker(lat:Double,lng:Double,position:Int){
        d{"addMarker from data position[$position]"}
        val zoomLevel = 16.0f //This goes up to 21
        val latLng = LatLng(lat, lng)
        async(UI){
            val data: Deferred<MarkerOptions> = bg {
                MarkerOptions()
                        .position(latLng)
                        .snippet(dataClinic[position].testName)
                        .title(dataClinic[position].name)
                        .icon(BitmapDescriptorFactory.fromBitmap(CustomPin()))
            }
            googleMap?.addMarker(data.await())
            googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoomLevel))
        }
    }




    fun CustomPin():Bitmap{
        val layout = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val customMarkerView = layout.inflate(R.layout.little_pin, null)
        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        customMarkerView.layout(0, 0, customMarkerView.measuredWidth, customMarkerView.measuredHeight)
        customMarkerView.buildDrawingCache()
        val returnedBitmap = Bitmap.createBitmap(customMarkerView.measuredWidth, customMarkerView.measuredHeight,
                Bitmap.Config.ARGB_8888)
        val canvas = Canvas(returnedBitmap)
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN)
        val drawable = customMarkerView.background
        drawable?.draw(canvas)
        customMarkerView.draw(canvas)
        return returnedBitmap
    }

     fun getMarkerBitmapFromView(resId :Int) :Bitmap {
            d{"set Marker Bitmap to Current location"}
         val layout = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
         val customMarkerView = layout.inflate(R.layout.custom_marker, null)
         val markerImageView = customMarkerView.findViewById<ImageView>(R.id.profile_image)
         val nameClinic = customMarkerView.findViewById<TextView>(R.id.txt_name)
         val descClinic = customMarkerView.findViewById<TextView>(R.id.txt_desc)

         // set Text
        // set Image circle
         markerImageView.setImageResource(resId)
         customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
         customMarkerView.layout(0, 0, customMarkerView.measuredWidth, customMarkerView.measuredHeight)
         customMarkerView.buildDrawingCache()
         val returnedBitmap = Bitmap.createBitmap(customMarkerView.measuredWidth, customMarkerView.measuredHeight,
                 Bitmap.Config.ARGB_8888)
         val canvas = Canvas(returnedBitmap)
         canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN)
         val drawable = customMarkerView.background
         drawable?.draw(canvas)
         customMarkerView.draw(canvas)
         return returnedBitmap
    }

}