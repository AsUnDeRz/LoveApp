package toche.asunder.loveapp.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.header_logo_white_back.*
import kotlinx.android.synthetic.main.hiv_status.*
import toche.asunder.loveapp.MyApp
import toche.asunder.loveapp.R

/**
 * Created by admin on 8/2/2017 AD.
 */
class HivStatusActivity: AppCompatActivity(){


    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.clinic_info)
        title_header.typeface = MyApp.typeFace.heavy
        txt_positive.typeface = MyApp.typeFace.medium
        txt_negative.typeface = MyApp.typeFace.medium
        txt_dontknow.typeface = MyApp.typeFace.medium


        //set title



        btn_back.setOnClickListener {
            onBackPressed()
        }



    }
}
