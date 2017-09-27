package asunder.toche.loveapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import asunder.toche.loveapp.R
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.clinic_info.*
import kotlinx.android.synthetic.main.header_logo_white_back.*
import kotlinx.android.synthetic.main.old_new_user.view.*
import view.custom_view.TextViewMedium

/**
 * Created by admin on 8/2/2017 AD.
 */
class ClinicInfo:AppCompatActivity(){


    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.clinic_info)

        var content = intent?.getParcelableExtra<Model.Clinic>(KEYPREFER.CLINICMODEL)
        val txtPhone = findViewById<TextViewMedium>(R.id.txt_phone)

        btn_booknow.typeface = MyApp.typeFace.heavy


        //set title

        Glide.with(this)
                .load(content?.img_detail)
                .into(img_clinic)
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
}

