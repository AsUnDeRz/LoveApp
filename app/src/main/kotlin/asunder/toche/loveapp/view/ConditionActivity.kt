package asunder.toche.loveapp

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
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

        btn_accept.setOnClickListener {
            startActivity(Intent().setClass(this@ConditionActivity,StepWelcomActivity::class.java))
            finish()
        }

        btn_no.setOnClickListener {
            finish()
        }
    }
}