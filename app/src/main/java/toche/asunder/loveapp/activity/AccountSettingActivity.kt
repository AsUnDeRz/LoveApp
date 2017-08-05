package toche.asunder.loveapp.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.account_setting.*
import kotlinx.android.synthetic.main.header_logo_blue_back.*
import toche.asunder.loveapp.MyApp
import toche.asunder.loveapp.R

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

        txt_title.typeface = MyApp.typeFace.heavy
        txt_title.text = "SETTING"
        txt_name.typeface = MyApp.typeFace.heavy
        txt_phone.typeface = MyApp.typeFace.heavy
        txt_mcode.typeface = MyApp.typeFace.heavy
        txt_fcode.typeface = MyApp.typeFace.heavy
        txt_email.typeface = MyApp.typeFace.heavy
        txt_password.typeface = MyApp.typeFace.heavy
        txt_unique_id.typeface = MyApp.typeFace.heavy
        txt_province.typeface = MyApp.typeFace.heavy
        txt_hiv_status.typeface = MyApp.typeFace.heavy
        txt_title_hiv_status.typeface = MyApp.typeFace.heavy

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