package asunder.toche.loveapp

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.support.customtabs.CustomTabsIntent
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import asunder.toche.loveapp.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.android.synthetic.main.clinic_info.*
import kotlinx.android.synthetic.main.header_logo_white_back.*
import kotlinx.android.synthetic.main.old_new_user.view.*
import utils.CustomTabActivityHelper
import view.custom_view.TextViewMedium

/**
 * Created by admin on 8/2/2017 AD.
 */
class ClinicInfo:AppCompatActivity(){


    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    private var mCustomTabActivityHelper: CustomTabActivityHelper? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.clinic_info)

        var content = intent?.getParcelableExtra<Model.Clinic>(KEYPREFER.CLINICMODEL)
        val txtPhone = findViewById<TextViewMedium>(R.id.txt_phone)

        btn_booknow.typeface = MyApp.typeFace.heavy


        //set title


        Glide.with(this)
                .load(content?.img_detail)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(icon_clinic)

        title_clinic.text = content?.name
        txt_nametest.text = content?.service
        txt_worktime.text = content?.open_hour
        txtPhone.text = "Phone:"+content?.phone
        txt_map.text = content?.address
        txt_web.text = "Email:"+content?.email

        if (content?.promo_id == ""){
            info_promotion.visibility = View.GONE
        }else{
            txt_promo_start.text = "Start "+content?.promo_start
            txt_promo_end.text = "End "+content?.promo_end
            txt_promotion.text = content?.promo_title
        }

        txtPhone.setOnClickListener {
            actionCall(content?.phone!!)
        }

        txt_map.setOnClickListener {
            setupCustomTabHelper("http://maps.google.com/?saddr=${LabFragment.latlngCurrent?.latitude},${LabFragment.latlngCurrent?.longitude}&daddr=${content?.locx},${content?.locy}")
            openCustomTab("http://maps.google.com/?saddr=${LabFragment.latlngCurrent?.latitude},${LabFragment.latlngCurrent?.longitude}&daddr=${content?.locx},${content?.locy}")

            /*
            val gmmIntentUri = Uri.parse("google.navigation:q=${content?.locx},${content?.locy}")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.`package` = "com.google.android.apps.maps"
            startActivity(mapIntent)
            */
        }



        btn_booknow.setOnClickListener {
            startActivity(Intent().setClass(this@ClinicInfo, BookingActivity::class.java))
        }

        btn_back.setOnClickListener {
            onBackPressed()
        }


        if (ContextCompat.checkSelfPermission(this@ClinicInfo, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this@ClinicInfo, 123, Manifest.permission.CALL_PHONE, true)

        }



    }

    fun actionCall(number:String){

        if (ContextCompat.checkSelfPermission(this@ClinicInfo, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this@ClinicInfo, 123, Manifest.permission.CALL_PHONE, true)

        }else{
            startActivity(Intent(Intent.ACTION_CALL).setData(Uri.parse("tel:$number")))

        }
    }


    private fun setupCustomTabHelper(URL:String) {
        mCustomTabActivityHelper = CustomTabActivityHelper()
        mCustomTabActivityHelper!!.setConnectionCallback(mConnectionCallback)
        mCustomTabActivityHelper!!.mayLaunchUrl(Uri.parse(URL), null, null)
    }

    private fun openCustomTab(URL: String) {

        //ตัวแปรนี้จะให้ในการกำหนดค่าต่างๆ ที่ข้างล่างนี้
        val intentBuilder = CustomTabsIntent.Builder()

        //กำหนดสีของ Action bar
        intentBuilder.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary))

        //กำหนดให้มี Animation เมื่อ Custom tab เข้ามาและออกไป ถ้าไม่มีจะเหมือน Activity ที่เด้งเข้ามาเลย
        setAnimation(intentBuilder)

        //Launch Custome tab ให้ทำงาน
        CustomTabActivityHelper.openCustomTab(
                this, intentBuilder.build(), Uri.parse(URL), WebviewFallback())
    }

    private fun setAnimation(intentBuilder: CustomTabsIntent.Builder) {
        //intentBuilder.setStartAnimations(this, android.R.anim.slide_out_right, android.R.anim.slide_in_left)
        intentBuilder.setExitAnimations(this, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
    }

    // You can use this callback to make UI changes
    private val mConnectionCallback = object : CustomTabActivityHelper.ConnectionCallback {
        override fun onCustomTabsConnected() {
            //Toast.makeText(this@PointHistriesActivity, "Connected to service", Toast.LENGTH_SHORT).show()
        }

        override fun onCustomTabsDisconnected() {
            //Toast.makeText(this@PointHistriesActivity, "Disconnected from service", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onStart() {
        super.onStart()
        mCustomTabActivityHelper?.bindCustomTabsService(this)
    }

    override fun onStop() {
        super.onStop()
        mCustomTabActivityHelper?.unbindCustomTabsService(this)
    }

    inner class WebviewFallback : CustomTabActivityHelper.CustomTabFallback {
        override fun openUri(activity: Activity, uri: Uri) {
            val intent = Intent(activity, WebViewActivity::class.java)
            intent.putExtra("KEY_URL", uri.toString())
            activity.startActivity(intent)
        }
    }
}

