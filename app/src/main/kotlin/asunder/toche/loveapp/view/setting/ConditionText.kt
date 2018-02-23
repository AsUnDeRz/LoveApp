package asunder.toche.loveapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.condition.*
import android.widget.RelativeLayout
import kotlinx.android.synthetic.main.header_logo_blue_back.*
import kotlinx.android.synthetic.main.header_logo_white.*


/**
 * Created by ToCHe on 30/10/2017 AD.
 */
class ConditionText:AppCompatActivity(){


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.condition)

        val data = intent.getStringExtra("data")
        when(data){
            "term" -> {
                title_condition.text = getString(R.string.conditiontitle)
                txt_infomation.text = DataSimple.textCondition
                txt_web.visibility = View.VISIBLE
                txt_web.setOnClickListener {
                    sendEmail(txt_web.text.toString())
                }
            }
            "policy" ->{
                title_condition.text = getString(R.string.newpolicy)
                txt_infomation.text = DataSimple.textPrivacy
                txt_web.visibility = View.GONE

            }
            "about" ->{
                title_condition.text = getString(R.string.aboutlove)
                txt_infomation.text = Utils(this@ConditionText).txtLocale(DataSimple.textAboutTh,DataSimple.textAboutEn)
                txt_web.visibility = View.GONE

            }
        }

        var parameter = relativeLayout4.layoutParams as RelativeLayout.LayoutParams
        parameter.setMargins(parameter.leftMargin, parameter.topMargin, parameter.rightMargin, 50) // left, top, right, bottom
        relativeLayout4.layoutParams = parameter

        check_condition.visibility = View.GONE
        btn_accept.visibility = View.GONE
        btn_no.visibility = View.GONE

        btn_back.setOnClickListener {
            finish()
        }
    }

    fun sendEmail(email:String?){
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:")
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
        startActivity(Intent.createChooser(intent, "Email via..."))
    }
}