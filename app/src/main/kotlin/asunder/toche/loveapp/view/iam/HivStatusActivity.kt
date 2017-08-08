package asunder.toche.loveapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import asunder.toche.loveapp.R
import kotlinx.android.synthetic.main.header_logo_white_back.*
import kotlinx.android.synthetic.main.hiv_status.*


/**
 * Created by admin on 8/2/2017 AD.
 */
class HivStatusActivity: AppCompatActivity(){


    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.hiv_status)





        btn_back.setOnClickListener {
            onBackPressed()
        }



    }
}
