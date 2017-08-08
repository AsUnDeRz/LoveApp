package asunder.toche.loveapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.animation.RotateAnimation
import kotlinx.android.synthetic.main.risk_meter_final.*


/**
 * Created by admin on 8/9/2017 AD.
 */
class RiskMeterFinalActivity:AppCompatActivity(){


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.risk_meter_final)


        val rotateAnim = RotateAnimation(-90f,(-90f+90) ,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 1f)

        rotateAnim.duration = 3000
        rotateAnim.fillAfter = true
        meter_pin.startAnimation(rotateAnim)

    }
}