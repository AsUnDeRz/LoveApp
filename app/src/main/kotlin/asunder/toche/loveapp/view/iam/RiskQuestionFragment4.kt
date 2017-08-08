package asunder.toche.loveapp

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.risk_meter.*
import kotlinx.android.synthetic.main.risk_meter_question4.*


/**
 * Created by admin on 8/8/2017 AD.
 */
class RiskQuestionFragment4 : Fragment() {

    companion object {
        fun newInstance(): RiskQuestionFragment4 {
            return RiskQuestionFragment4()
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.risk_meter_question4, container, false)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_1.typeface = MyApp.typeFace.heavy
        btn_2.typeface = MyApp.typeFace.heavy
        btn_1.setOnClickListener {
            RiskMeterActivity.vp_riskmeter.setCurrentItem(5,false)
        }
    }

}