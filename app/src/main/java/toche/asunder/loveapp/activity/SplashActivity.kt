package toche.asunder.loveapp.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.gender_list.*
import toche.asunder.loveapp.R
import toche.asunder.loveapp.adapter.GenderAdapter
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

/**
 * Created by admin on 7/30/2017 AD.
 */
class SplashActivity: AppCompatActivity(){


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_page)

        val intentThis = Intent()
        val splash = Handler()
        splash.postDelayed({
            startActivity(intentThis.setClass(this,GenderActivity::class.java))
            finish()
        },3000)
    }
}