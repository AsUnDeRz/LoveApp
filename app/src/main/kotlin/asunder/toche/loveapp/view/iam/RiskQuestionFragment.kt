package asunder.toche.loveapp

import android.arch.lifecycle.ViewModel
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import asunder.toche.loveapp.MemoryMaster2Activity.Companion.data
import com.github.ajalt.timberkt.Timber.d
import kotlinx.android.synthetic.main.risk_meter.*
import kotlinx.android.synthetic.main.risk_meter_question.*
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg


/**
 * Created by admin on 8/8/2017 AD.
 */
class RiskQuestionFragment : Fragment() {


    companion object {
        fun newInstance(): RiskQuestionFragment {
            return RiskQuestionFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ViewModel

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.risk_meter_question, container, false)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_1.typeface = MyApp.typeFace.heavy
        btn_2.typeface = MyApp.typeFace.heavy
        setQuestion(ActivityMain.questions[0])

        btn_1.setOnClickListener {
            RiskMeterActivity.riskAnswer[0] = 1
            RiskMeterActivity.vp_riskmeter.setCurrentItem(1,false)
        }

        btn_2.setOnClickListener {
            RiskMeterActivity.riskAnswer[0] = 2
            RiskMeterActivity.vp_riskmeter.setCurrentItem(1,false)

        }
    }

    fun setQuestion(question:Model.RiskQuestion){
        val utils = Utils(activity)
        d{"Check null"+question.title_eng}
        txt_question.text = "1. "+question.title_eng
        btn_1.text = utils.txtLocale(question.question1_th,question.question1_eng)
        btn_2.text = utils.txtLocale(question.question2_th,question.question2_eng)
    }

}