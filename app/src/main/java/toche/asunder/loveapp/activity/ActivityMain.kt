package toche.asunder.loveapp.activity

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.graphics.Color
import kotlinx.android.synthetic.main.lab_result.*
import com.github.mikephil.charting.components.Legend.LegendForm
import com.github.mikephil.charting.components.LimitLine.LimitLabelPosition
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import android.graphics.DashPathEffect
import android.media.session.PlaybackState
import android.support.design.widget.BottomNavigationView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx
import toche.asunder.loveapp.CustomViewpager
import toche.asunder.loveapp.R
import toche.asunder.loveapp.adapter.MainAdapter
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper


class ActivityMain : AppCompatActivity() {



    companion object {
        lateinit var vp_main : CustomViewpager
        lateinit var bnve : BottomNavigationViewEx
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        vp_main = findViewById(R.id.vp_main)
        bnve = findViewById(R.id.bnve)

        setUpBottomBar()


        val adapterViewPager = MainAdapter(supportFragmentManager, this)
        vp_main.adapter = adapterViewPager
        vp_main.setAllowedSwipeDirection(CustomViewpager.SwipeDirection.none)



    }

    fun setUpBottomBar(){

        bnve.enableAnimation(false)
        bnve.enableShiftingMode(false)
        bnve.enableItemShiftingMode(false)
        bnve.setIconsMarginTop(30)
        bnve.setIconSize(30f,30f)

        bnve.onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->

            when (item.itemId){
                R.id.setting -> vp_main.setCurrentItem(4,false)
                R.id.learnandgame -> vp_main.setCurrentItem(3,false)
                R.id.iam -> vp_main.setCurrentItem(2,false)
                R.id.lab -> vp_main.setCurrentItem(1,false)
                R.id.home -> vp_main.setCurrentItem(0,false)
            }
            true
        }

    }


    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }


}
