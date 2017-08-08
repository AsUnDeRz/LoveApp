package asunder.toche.loveapp

import android.databinding.ObservableArrayList
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import asunder.toche.loveapp.R
import kotlinx.android.synthetic.main.header_logo_blue_back.*
import kotlinx.android.synthetic.main.point_histries.*


/**
 * Created by admin on 8/3/2017 AD.
 */
class PointHistriesActivity: AppCompatActivity() {

    val pointList = ObservableArrayList<Model.Point>().apply {
        for(i in 1..5) {
            add(Model.Point(123, "Reading", "300"))
        }
    }
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.point_histries)



        rv_point_histries.layoutManager = LinearLayoutManager(this)
        rv_point_histries.setHasFixedSize(true)
        rv_point_histries.adapter = MasterAdapter.PointsAdapter(pointList,false)
        txt_title.typeface = MyApp.typeFace.heavy
        txt_redeem.typeface = MyApp.typeFace.heavy
        txt_title.text = "POINT\nHISTRIES"
        btn_back.setOnClickListener {
            onBackPressed()
        }


    }
}