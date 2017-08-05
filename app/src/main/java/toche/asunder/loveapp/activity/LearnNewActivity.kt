package toche.asunder.loveapp.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.header_logo_white_back.*
import kotlinx.android.synthetic.main.learn_new.*
import kotlinx.android.synthetic.main.learn_new_finish.*
import toche.asunder.loveapp.MyApp
import toche.asunder.loveapp.R

/**
 * Created by admin on 8/5/2017 AD.
 */
class LearnNewActivity: AppCompatActivity() {

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.learn_new)

        title_header.typeface = MyApp.typeFace.heavy
        title_header.text = "LEARNS\nAND GAMES"
        colunm_1.typeface = MyApp.typeFace.medium
        colunm_2.typeface = MyApp.typeFace.medium
        colunm_final.typeface = MyApp.typeFace.medium

        high_light_1.typeface = MyApp.typeFace.heavy
        high_light_2.typeface = MyApp.typeFace.heavy
        txt_readmore.typeface = MyApp.typeFace.heavy
        txt_getmore.typeface = MyApp.typeFace.heavy
        txt_f1.typeface = MyApp.typeFace.heavy
        txt_f2.typeface = MyApp.typeFace.heavy
        txt_f3.typeface = MyApp.typeFace.heavy
        title_new.typeface = MyApp.typeFace.heavy




        btn_back.setOnClickListener {
            onBackPressed()
        }


    }
}