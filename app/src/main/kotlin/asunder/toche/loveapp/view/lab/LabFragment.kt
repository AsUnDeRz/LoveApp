package asunder.toche.loveapp

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.databinding.ObservableArrayList
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PorterDuff
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
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
import com.google.android.gms.maps.model.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.header_logo_white.*
import kotlinx.android.synthetic.main.lab.*
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg
import java.io.IOException
import java.util.*
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


    var service : LoveAppService = LoveAppService.create()

    private var _compoSub = CompositeDisposable()
    private val compoSub: CompositeDisposable
        get() {
            if (_compoSub.isDisposed) {
                _compoSub = CompositeDisposable()
            }
            return _compoSub
        }

    protected final fun manageSub(s: Disposable) = compoSub.add(s)

    fun unsubscribe() { compoSub.dispose() }

    lateinit var utils : Utils

    fun loadHospital(){
        manageSub(
                service.getHospitalsOnMap(location?.latitude.toString(),location?.longitude.toString(),"8")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ c -> run {
                            val rawData = ObservableArrayList<Model.RepositoryHospital>()
                            val data = ObservableArrayList<Model.Clinic>().apply{
                                c.forEach {
                                    item -> add(Model.Clinic(item.id.toLong(),utils.txtLocale(item.name_th,item.name_eng),
                                        utils.txtLocale(item.address_th,item.address_eng),utils.txtLocale(item.service_th,item.service_eng),
                                        utils.txtLocale(item.open_hour_th,item.open_hour_eng),item.phone,item.email,item.province,
                                        item.locx,item.locy,item.version,"","","item.hospital_id",
                                        "http://backend.loveapponline.com/"+item.file_path.replace("images","")+"o.png",
                                        "http://backend.loveapponline.com/"+item.file_path+"/"+item.file_name+"_o.png",
                                        if(item.promotion_id != null){item.promotion_id}else{""},if(item.promotion_th != null && item.promotion_eng != null){utils.txtLocale(item.promotion_th,item.promotion_eng)}else{""},
                                        if(item.start_date !=null){utils.getDateSlash(item.start_date)}else{""},
                                        if(item.end_date !=null){utils.getDateSlash(item.end_date)}else{""}))
                                    //d { "Add ["+item.name_th+"] to arraylist" }
                                    rawData.add(item)
                                }
                            }
                            hospitalList = data
                            repohospitalList = rawData
                            //
                            loadMarker()
                            d { "check response [" + c.size + "]" }
                        }},{
                            d { it.message!! }
                        })
        )
    }





    //override activity/fragment
    lateinit var mGoogleApiClient: GoogleApiClient
    var googleMap: GoogleMap? = null
    var latlngWhenPause : LatLng? =null
    val PLACE_PICKER_REQUEST = 1
    lateinit var mLocationRequest: LocationRequest
    var location: Location? = null
    lateinit var options: MarkerOptions
    var mPermissionDenied = false
    val zoomLevel = 14f
    lateinit var appDb : AppDatabase


    companion object {
        fun newInstance(): LabFragment {
            return LabFragment()
        }

        var repohospitalList = ObservableArrayList<Model.RepositoryHospital>()
        var hospitalList = ObservableArrayList<Model.Clinic>()
        var city : String=""
        var subCity : String =""
        var latlngCurrent : LatLng? =null


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


        utils = Utils(activity)
        appDb = AppDatabase(activity)
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        d { "Visible to user =$isVisibleToUser" }
        if(isVisibleToUser && location != null && hospitalList.size == 0){
            loadHospital()

        }else{
            if(isVisibleToUser && location != null && hospitalList.size > 0){
                val data = ObservableArrayList<Model.Clinic>().apply {
                    repohospitalList.forEach {
                        item -> add(Model.Clinic(item.id.toLong(),utils.txtLocale(item.name_th,item.name_eng),
                            utils.txtLocale(item.address_th,item.address_eng),utils.txtLocale(item.service_th,item.service_eng),
                            utils.txtLocale(item.open_hour_th,item.open_hour_eng),item.phone,item.email,item.province,
                            item.locx,item.locy,item.version,"","","item.hospital_id",
                            "http://backend.loveapponline.com/"+item.file_path.replace("images","")+"o.png",
                            "http://backend.loveapponline.com/"+item.file_path+"/"+item.file_name+"_o.png",
                            if(item.promotion_id != null){item.promotion_id}else{""},if(item.promotion_th != null && item.promotion_eng != null){utils.txtLocale(item.promotion_th,item.promotion_eng)}else{""},
                            if(item.start_date !=null){utils.getDateSlash(item.start_date)}else{""},
                            if(item.end_date !=null){utils.getDateSlash(item.end_date)}else{""}))
                        d { "Add ["+item.name_th+"] to arraylist" }
                    }
                }
                hospitalList = data

            }
        }

        //addmarker


    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.lab, container, false)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView.onCreate(savedInstanceState)
        val provinces = appDb.getProvince()

        //Custom wording and font
        title_app.typeface = MyApp.typeFace.heavy
        title_app.text = "NEARBY \nPLACE"
        txt_search.typeface = MyApp.typeFace.medium
        txt_search.hint = "Search..."
        btn_showlist.setOnClickListener {
            ActivityMain.vp_main.setCurrentItem(KEYPREFER.CLINIC, false)
        }

        btn_clear.setOnClickListener {
            txt_search.setText("")
        }

        txt_search.setOnFocusChangeListener { view, b ->
            if(b){
                val proAdapter = ProvinceAdapter(activity,provinces)
                txt_search.setAdapter(proAdapter)
                txt_search.setOnItemClickListener { adapterView, view, position, id ->
                    val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    val v = activity.currentFocus
                    imm.hideSoftInputFromWindow(v.windowToken, 0)
                    provinces
                            .filter { it.province_id == id.toString() }
                            .forEach {
                                //d{ it.province_eng+" // "}
                                val latlng = LatLng(it.locx,it.locy)
                               googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng,10f))
                            }


                    d{ "i == $position///l == $id" }
                }
            }
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
        d { "onPause mapView"
        }
        //LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this)
        mapView.onPause()
        unsubscribe()

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
        googleMap?.setOnMyLocationButtonClickListener(this)
        googleMap?.setOnInfoWindowClickListener(this)
        googleMap?.setOnInfoWindowCloseListener(this)
        enableMyLocation()
    }
    override fun onInfoWindowClose(marker: Marker?) {
        //Toast.makeText(context, "I'am close " + marker?.title, Toast.LENGTH_LONG).show()
    }

    override fun onInfoWindowClick(marker: Marker?) {
       var clinic : Model.Clinic? =null
        val title = marker?.title
        latlngWhenPause = marker?.position!!

        hospitalList
                .filter { it.name == title }
                .forEach { clinic = it }


        val intent = Intent()
        intent.putExtra(KEYPREFER.CLINICMODEL,clinic)
        activity.startActivity(intent.setClass(activity,ClinicInfo::class.java))
        //Toast.makeText(context, "I'am click " + marker?.title, Toast.LENGTH_LONG).show()

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
        //d { "handle New location ["+location?.provider+"]" }
        //d{"get current location ["+getAddress(location?.latitude,location?.longitude)+"]"}

        val latLng = LatLng(location!!.latitude, location!!.longitude)
        latlngCurrent = latLng
        googleMap?.clear()
        val current =MarkerOptions()
                .position(latLng)
                .icon(BitmapDescriptorFactory.fromBitmap(CustomPin()))
        googleMap?.addMarker(current)
        googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel))
        if(hospitalList.size == 0) {
            loadHospital()
        }else{
            loadMarker()
        }
        if(latlngWhenPause != null){
            googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(latlngWhenPause,zoomLevel))
        }
        /*
        for(dt in hospitalList){
            addMarker(dt)
        }
        */
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
                val latLng = LatLng(location!!.latitude, location!!.longitude)
                /*
                val current =MarkerOptions()
                        .position(latLng)
                        .icon(BitmapDescriptorFactory.fromBitmap(CustomPin()))
                googleMap?.addMarker(current)
                googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel))


                for(dt in hospitalList){
                    addMarker(dt)
                }
                */
            }


        }
    }


    fun addMarker(lat:Double,lng:Double,position:Int){
        d{"addMarker from data position[$position]"}
       // val zoomLevel = 16.0f //This goes up to 21
        val latLng = LatLng(lat, lng)
        async(UI){
            val data: Deferred<MarkerOptions> = bg {
                MarkerOptions()
                        .position(latLng)
                        .snippet(hospitalList[position].service)
                        .title(hospitalList[position].name)
                        .icon(BitmapDescriptorFactory.fromBitmap(CustomPin()))
            }
            googleMap?.addMarker(data.await())
            if(position == 0) {
                googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel))
            }
        }
    }

    fun addMarker(dat:Model.Clinic){
        //d{"Add marker hospital lat["+dat.locy+"] lng["+dat.locx+"]"}
        //val zoomLevel = 16.0f //This goes up to 21
        val latLng = LatLng(dat.locx, dat.locy)
        async(UI){
            val data: Deferred<MarkerOptions> = bg {
                MarkerOptions()
                        .position(latLng)
                        .snippet(dat.service)
                        .title(dat.name)
                        .icon(BitmapDescriptorFactory.fromBitmap(CustomPin()))
            }
            googleMap?.addMarker(data.await())
            //googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoomLevel))
        }
    }

    fun loadMarker(){
        if (googleMap != null) {
            googleMap?.setInfoWindowAdapter(InfoMapAdapter(hospitalList, activity))
            // Access to the location has been granted to the app.
            //googleMap!!.isMyLocationEnabled = true
            if (mGoogleApiClient.isConnected && location != null) {
                val latLng = LatLng(location!!.latitude, location!!.longitude)
                //var builder = LatLngBounds.Builder()
                for(dt in hospitalList){
                    addMarker(dt)
                    //builder.include(LatLng(dt.locx,dt.locy))
                }
                /*
                val current =MarkerOptions()
                        .position(latLng)
                googleMap?.addMarker(current)
                */
                googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoomLevel))

            }
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

    fun getAddress(latitude:Double?, longitude:Double?):String {
        var result = StringBuilder()
        try {
            val geocoder = Geocoder(activity, Locale.getDefault())
            var  addresses = geocoder.getFromLocation(latitude!!, longitude!!, 1)
            if (addresses.size > 0) {
                var data = addresses[0]
                result.append(data.locality).append("\n")
                result.append(data.featureName).append("\n")
                result.append(data.adminArea).append("\n")
                result.append(data.subAdminArea).append("\n")
                result.append(data.subLocality).append("\n")
                result.append(data.thoroughfare).append("\n")
                result.append(data.countryName)

                city = data.locality
                var subArea :String
                subArea = if(data.subAdminArea == null){
                    data.subLocality+","+data.adminArea
                }else{
                    data.subLocality+","+data.adminArea
                }
                subCity = subArea

            }
        } catch (e:IOException) {
            d{e.toString()}
        }

        return result.toString()
    }

}