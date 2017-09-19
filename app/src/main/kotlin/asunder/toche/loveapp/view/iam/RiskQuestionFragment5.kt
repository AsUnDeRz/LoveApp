package asunder.toche.loveapp

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.ajalt.timberkt.Timber.d
import kotlinx.android.synthetic.main.header_logo_white_back.*
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
        setQuestion(RiskMeterActivity.questions[5])
        btn_1.typeface = MyApp.typeFace.heavy
        btn_2.typeface = MyApp.typeFace.heavy
        btn_3.typeface =MyApp.typeFace.heavy
        btn_4.typeface = MyApp.typeFace.heavy
        btn_5.typeface = MyApp.typeFace.heavy
        btn_6.typeface =MyApp.typeFace.heavy
        btn_7.typeface = MyApp.typeFace.heavy
        btn_1.setOnClickListener {
            RiskMeterActivity.riskAnswer[5] = 1
            RiskMeterActivity.riskAnswer[6] = 0
            RiskMeterActivity.sendRiskAnswer(context)

        }
        btn_2.setOnClickListener {
            RiskMeterActivity.riskAnswer[5] = 2
            RiskMeterActivity.riskAnswer[6] = 0
            RiskMeterActivity.sendRiskAnswer(context)

        }
        btn_3.setOnClickListener {
            RiskMeterActivity.riskAnswer[5] = 3
            RiskMeterActivity.vp_riskmeter.setCurrentItem(6,false)
        }
        btn_4.setOnClickListener {
            RiskMeterActivity.riskAnswer[5] = 4
            RiskMeterActivity.vp_riskmeter.setCurrentItem(6,false)
        }
        btn_5.setOnClickListener {
            RiskMeterActivity.riskAnswer[5] = 5
            RiskMeterActivity.vp_riskmeter.setCurrentItem(6,false)
        }
        btn_6.setOnClickListener {
            RiskMeterActivity.riskAnswer[5] = 6
            RiskMeterActivity.vp_riskmeter.setCurrentItem(6,false)
        }
        btn_7.setOnClickListener {
            RiskMeterActivity.riskAnswer[5] = 7
            RiskMeterActivity.riskAnswer[6] = 0
            RiskMeterActivity.sendRiskAnswer(context)
        }

        btn_back.setOnClickListener {
            activity.finish()
        }
    }

    fun setQuestion(question:Model.RiskQuestion){
        val utils = Utils(activity)
        d { "Check null" + question.title_eng }
        txt_question.text = "5. "+utils.txtLocale(question.title_th,question.title_eng)
        btn_1.text = utils.txtLocale(question.question1_th,question.question1_eng)
        btn_2.text = utils.txtLocale(question.question2_th,question.question2_eng)
        btn_3.text = utils.txtLocale(question.question3_th,question.question3_eng)
        btn_4.text = utils.txtLocale(question.question4_th,question.question4_eng)
        btn_5.text = utils.txtLocale(question.question5_th,question.question5_eng)
        btn_6.text = utils.txtLocale(question.question6_th,question.question6_eng)
        btn_7.text = utils.txtLocale(question.question7_th,question.question7_eng)
    }

}