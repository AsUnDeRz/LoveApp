package toche.asunder.loveapp.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.header_logo_blue_back.*
import kotlinx.android.synthetic.main.unique_id_code.*
import toche.asunder.loveapp.MyApp
import toche.asunder.loveapp.R

/**
 * Created by admin on 8/4/2017 AD.
 */
class UniqueIdActivity: AppCompatActivity() {

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.unique_id_code)

        txt_title.typeface = MyApp.typeFace.heavy
        txt_title.text = "UNIQUE\nID CODE"
        txt_fname.typeface =MyApp.typeFace.heavy
        txt_lname.typeface = MyApp.typeFace.heavy
        txt_hbd.typeface = MyApp.typeFace.heavy
        btn_save.typeface = MyApp.typeFace.heavy
        edt_fname.typeface = MyApp.typeFace.heavy
        edt_lname.typeface = MyApp.typeFace.heavy
        edt_hbd.typeface = MyApp.typeFace.heavy

        btn_back.setOnClickListener {
            onBackPressed()
        }


    }
}