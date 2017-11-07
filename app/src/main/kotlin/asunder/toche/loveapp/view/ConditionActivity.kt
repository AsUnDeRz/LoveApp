package asunder.toche.loveapp

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.CompoundButton
import android.widget.RadioGroup
import kotlinx.android.synthetic.main.condition.*


/**
 * Created by ToCHe on 10/7/2017 AD.
 */
class ConditionActivity :AppCompatActivity(){


    lateinit var utils:Utils
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.condition)

        utils = Utils(this@ConditionActivity)

        btn_accept.typeface =utils.heavy
        btn_no.typeface = utils.heavy
        check_condition.typeface = utils.heavy

        btn_accept.setOnClickListener {
            startActivity(Intent().setClass(this@ConditionActivity,StepWelcomActivity::class.java))
            finish()
        }

        btn_no.setOnClickListener {
            startActivity(Intent().setClass(this@ConditionActivity,SelectLangActivity::class.java))

            finish()
        }

        check_condition.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked) {
                btn_accept.visibility = View.VISIBLE
            }else{
                btn_accept.visibility = View.GONE
            }
        }
    }
}