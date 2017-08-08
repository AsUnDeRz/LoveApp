package asunder.toche.loveapp

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.risk_meter.*
import kotlinx.android.synthetic.main.risk_meter_question5.*


/**
 * Created by admin on 8/8/2017 AD.
 */
class RiskQuestionFragment5 : Fragment() {

    companion object {
        fun newInstance(): RiskQuestionFragment5 {
            return RiskQuestionFragment5()
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.risk_meter_question5, container, false)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_1.typeface = MyApp.typeFace.heavy
        btn_2.typeface = MyApp.typeFace.heavy
        btn_3.typeface =MyApp.typeFace.heavy
        btn_4.typeface = MyApp.typeFace.heavy
        btn_5.typeface = MyApp.typeFace.heavy
        btn_6.typeface =MyApp.typeFace.heavy
        btn_7.typeface = MyApp.typeFace.heavy
        btn_1.setOnClickListener {
            RiskMeterActivity.vp_riskmeter.setCurrentItem(6,false)
        }
    }

}