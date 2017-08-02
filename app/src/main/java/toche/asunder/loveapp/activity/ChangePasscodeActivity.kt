package toche.asunder.loveapp.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.change_passcode.*
import kotlinx.android.synthetic.main.header_logo_blue_back.*
import toche.asunder.loveapp.MyApp
import toche.asunder.loveapp.R

/**
 * Created by admin on 8/2/2017 AD.
 */
class ChangePasscodeActivity: AppCompatActivity(){


    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.booking_layout)
        txt_title.typeface = MyApp.typeFace.heavy
        txt_changepass.typeface = MyApp.typeFace.heavy





    }
}