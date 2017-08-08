package asunder.toche.loveapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import asunder.toche.loveapp.R
import kotlinx.android.synthetic.main.header_logo_blue_back.*
import kotlinx.android.synthetic.main.unique_id_code.*


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

        txt_title.text = "UNIQUE\nID CODE"
        btn_save.typeface = MyApp.typeFace.heavy
        edt_fname.typeface = MyApp.typeFace.heavy
        edt_lname.typeface = MyApp.typeFace.heavy
        edt_hbd.typeface = MyApp.typeFace.heavy

        btn_back.setOnClickListener {
            onBackPressed()
        }


    }
}