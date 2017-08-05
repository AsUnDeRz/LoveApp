package toche.asunder.loveapp.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.header_logo_blue_back.*
import kotlinx.android.synthetic.main.point_histries.*
import toche.asunder.loveapp.MyApp
import toche.asunder.loveapp.R
import toche.asunder.loveapp.adapter.PointHistriesAdapter

/**
 * Created by admin on 8/3/2017 AD.
 */
class PointHistriesActivity: AppCompatActivity() {

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.point_histries)



        rv_point_histries.layoutManager = LinearLayoutManager(this)
        rv_point_histries.setHasFixedSize(true)
        rv_point_histries.adapter = PointHistriesAdapter(this@PointHistriesActivity)
        txt_title.typeface = MyApp.typeFace.heavy
        txt_redeem.typeface = MyApp.typeFace.heavy
        txt_title.text = "POINT\nHISTRIES"
        btn_back.setOnClickListener {
            onBackPressed()
        }


    }
}