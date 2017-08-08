package asunder.toche.loveapp

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.risk_meter.*
import kotlinx.android.synthetic.main.risk_meter_question3b.*


/**
 * Created by admin on 8/8/2017 AD.
 */
class RiskQuestionFragment3b : Fragment() {

    companion object {
        fun newInstance(): RiskQuestionFragment3b {
            return RiskQuestionFragment3b()
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.risk_meter_question3b, container, false)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_1.setOnClickListener {
            vp_riskmeter.setCurrentItem(3,false)
        }
    }

}