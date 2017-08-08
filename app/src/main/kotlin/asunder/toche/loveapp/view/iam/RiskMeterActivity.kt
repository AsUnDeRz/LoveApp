package asunder.toche.loveapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.risk_meter.*
import view.custom_view.CustomViewpager

/**
 * Created by admin on 8/8/2017 AD.
 */
class RiskMeterActivity:AppCompatActivity(){


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.risk_meter)

        val adapterViewPager = RiskMeterAdapter(supportFragmentManager, this)
        vp_riskmeter.adapter = adapterViewPager
        //vp_riskmeter.setAllowedSwipeDirection(CustomViewpager.SwipeDirection.none)


    }
}