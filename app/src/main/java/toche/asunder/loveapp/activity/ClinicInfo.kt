package toche.asunder.loveapp.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.clinic_info.*
import kotlinx.android.synthetic.main.header_logo_white_back.*
import toche.asunder.loveapp.MyApp
import toche.asunder.loveapp.R

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
        title_header.typeface = MyApp.typeFace.heavy
        title_clinic.typeface = MyApp.typeFace.heavy
        txt_infomation.typeface = MyApp.typeFace.heavy
        txt_contact.typeface = MyApp.typeFace.heavy
        txt_nametest.typeface = MyApp.typeFace.medium
        txt_worktime.typeface = MyApp.typeFace.medium
        txt_web.typeface = MyApp.typeFace.medium
        txt_phone.typeface = MyApp.typeFace.medium
        txt_map.typeface = MyApp.typeFace.medium

        //set title

        btn_booknow.setOnClickListener {
            startActivity(Intent().setClass(this@ClinicInfo,BookingActivity::class.java))
        }

        btn_back.setOnClickListener {
            onBackPressed()
        }



    }
}

