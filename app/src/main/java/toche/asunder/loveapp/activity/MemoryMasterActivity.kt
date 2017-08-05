package toche.asunder.loveapp.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.header_logo_blue_back.*
import kotlinx.android.synthetic.main.memory_master.*
import toche.asunder.loveapp.MyApp
import toche.asunder.loveapp.R

/**
 * Created by admin on 8/5/2017 AD.
 */
class MemoryMasterActivity:AppCompatActivity(){


    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.memory_master)

        txt_title.typeface = MyApp.typeFace.heavy
        txt_title.text = "MEMORY\nMASTER"
        txt_time.typeface = MyApp.typeFace.heavy

        btn_back.setOnClickListener {
            onBackPressed()
        }


    }
}