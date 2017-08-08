package asunder.toche.loveapp

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import asunder.toche.loveapp.R
import kotlinx.android.synthetic.main.clinic_info.*
import kotlinx.android.synthetic.main.header_logo_white_back.*

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

        btn_booknow.typeface = MyApp.typeFace.heavy


        //set title

        btn_booknow.setOnClickListener {
            startActivity(Intent().setClass(this@ClinicInfo, BookingActivity::class.java))
        }

        btn_back.setOnClickListener {
            onBackPressed()
        }



    }
}

