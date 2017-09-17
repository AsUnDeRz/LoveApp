package asunder.toche.loveapp

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.ajalt.timberkt.Timber
import com.github.ajalt.timberkt.Timber.d
import kotlinx.android.synthetic.main.header_logo_white_back.*
import kotlinx.android.synthetic.main.risk_meter.*
import kotlinx.android.synthetic.main.risk_meter_question2.*


/**
 * Created by admin on 8/8/2017 AD.
 */
class RiskQuestionFragment2 : Fragment() {

    companion object {
        fun newInstance(): RiskQuestionFragment2 {
            return RiskQuestionFragment2()
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.risk_meter_question2, container, false)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setQuestion(ActivityMain.questions[1])
        btn_1.typeface = MyApp.typeFace.heavy
        btn_2.typeface = MyApp.typeFace.heavy
        btn_3.typeface =MyApp.typeFace.heavy
        btn_1.setOnClickListener {
            RiskMeterActivity.riskAnswer[1] = 1
            RiskMeterActivity.vp_riskmeter.setCurrentItem(2,false)
        }
        btn_2.setOnClickListener {
            RiskMeterActivity.riskAnswer[1] = 2
            RiskMeterActivity.vp_riskmeter.setCurrentItem(2,false)
        }
        btn_3.setOnClickListener {
            RiskMeterActivity.riskAnswer[1] = 3
            RiskMeterActivity.vp_riskmeter.setCurrentItem(2,false)
        }
        btn_back.setOnClickListener {
            activity.finish()
        }
    }

    fun setQuestion(question:Model.RiskQuestion){
        val utils = Utils(activity)
        d { "Check null" + question.title_eng }
        txt_question.text = "2. "+utils.txtLocale(question.title_th,question.title_eng)
        btn_1.text = utils.txtLocale(question.question1_th,question.question1_eng)
        btn_2.text = utils.txtLocale(question.question2_th,question.question2_eng)
        btn_3.text = utils.txtLocale(question.question3_th,question.question3_eng)
    }

}