package asunder.toche.loveapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import asunder.toche.loveapp.R
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.account_setting.*
import kotlinx.android.synthetic.main.header_logo_blue_back.*

/**
 * Created by admin on 8/4/2017 AD.
 */
class AccountSettingActivity: AppCompatActivity() {

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.account_setting)
        Glide.with(this)
                .load(R.drawable.bg_white)
                .into(bg_root)
        txt_title.text = "SETTING"
        edt_name.typeface = MyApp.typeFace.heavy
        edt_phone.typeface = MyApp.typeFace.heavy
        edt_mcode.typeface = MyApp.typeFace.heavy
        edt_fcode.typeface = MyApp.typeFace.heavy
        edt_email.typeface = MyApp.typeFace.heavy
        edt_password.typeface = MyApp.typeFace.heavy
        edt_unique.typeface = MyApp.typeFace.heavy
        edt_province.typeface = MyApp.typeFace.heavy


        btn_back.setOnClickListener {
            onBackPressed()
        }


    }
}