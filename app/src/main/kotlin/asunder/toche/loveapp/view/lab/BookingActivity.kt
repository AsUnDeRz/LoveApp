package asunder.toche.loveapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import asunder.toche.loveapp.R
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.booking_layout.*
import kotlinx.android.synthetic.main.header_logo_blue_back.*


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
        edt_date.typeface = MyApp.typeFace.heavy
        edt_time.typeface = MyApp.typeFace.heavy
        edt_phone.typeface = MyApp.typeFace.heavy
        edt_email.typeface = MyApp.typeFace.heavy

        Glide.with(this)
                .load(R.drawable.bg_white)
                .into(bg_root)



    }
}
