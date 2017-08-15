package asunder.toche.loveapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.risk_meter.*
import kotlinx.android.synthetic.main.risk_meter_question6.*


/**
 * Created by admin on 8/8/2017 AD.
 */
class RiskQuestionFragment6 : Fragment() {

    companion object {
        fun newInstance(): RiskQuestionFragment6 {
            return RiskQuestionFragment6()
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.risk_meter_question6, container, false)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_1.typeface = MyApp.typeFace.heavy
        btn_2.typeface = MyApp.typeFace.heavy
        btn_3.typeface =MyApp.typeFace.heavy
        btn_1.setOnClickListener {
            RiskMeterActivity.riskAnswer[6] = 1
            RiskMeterActivity.sendRiskAnswer(context)

        }
        btn_2.setOnClickListener {
            RiskMeterActivity.riskAnswer[6] = 2
            RiskMeterActivity.sendRiskAnswer(context)

        }
        btn_3.setOnClickListener {
            RiskMeterActivity.riskAnswer[6] = 3
            RiskMeterActivity.sendRiskAnswer(context)

        }
    }

}