package toche.asunder.loveapp.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.booking_layout.*
import kotlinx.android.synthetic.main.header_logo_blue_back.*
import toche.asunder.loveapp.MyApp
import toche.asunder.loveapp.R

/**
 * Created by admin on 8/2/2017 AD.
 */
class BookingActivity:AppCompatActivity(){


    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.booking_layout)
        title_clinic.typeface = MyApp.typeFace.heavy
        txt_date.typeface = MyApp.typeFace.heavy
        txt_time.typeface = MyApp.typeFace.heavy
        txt_phone.typeface = MyApp.typeFace.heavy
        txt_email.typeface = MyApp.typeFace.heavy
        edt_date.typeface = MyApp.typeFace.heavy
        edt_time.typeface = MyApp.typeFace.heavy
        edt_phone.typeface = MyApp.typeFace.heavy
        edt_email.typeface = MyApp.typeFace.heavy
        txt_title.typeface = MyApp.typeFace.heavy




    }
}
