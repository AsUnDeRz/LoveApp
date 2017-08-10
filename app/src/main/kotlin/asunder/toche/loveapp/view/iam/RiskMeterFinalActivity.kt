package asunder.toche.loveapp

import android.os.Bundle
import android.os.Handler
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

        //(30, 360, image.getX() + image.getWidth()/2, image.getY() + image.getHeight()/2)

        val rotateAnim = RotateAnimation(0f,-35f,
                RotateAnimation.RELATIVE_TO_SELF,0.5f,
                RotateAnimation.RELATIVE_TO_SELF,1f)

        rotateAnim.duration = 3500
        rotateAnim.fillAfter = true
        val splash = Handler()
        splash.postDelayed({
            meter_pin.startAnimation(rotateAnim)

        },2000)

    }


}