package asunder.toche.loveapp

import android.content.Intent
import android.net.Uri
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
        txt_infomation.text = DataSimple.textCondition
        txt_web.visibility =View.VISIBLE

        btn_accept.setOnClickListener {
            startActivity(Intent().setClass(this@ConditionActivity,StepWelcomActivity::class.java))
            finish()
        }

        btn_no.setOnClickListener {
            startActivity(Intent().setClass(this@ConditionActivity,SelectLangActivity::class.java))

            finish()
        }

        txt_web.setOnClickListener {
            sendEmail(txt_web.text.toString())

        }

        check_condition.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked) {
                btn_accept.visibility = View.VISIBLE
            }else{
                btn_accept.visibility = View.GONE
            }
        }

    }


    fun sendEmail(email:String?){
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:")
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
        startActivity(Intent.createChooser(intent, "Email via..."))
    }
}