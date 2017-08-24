package asunder.toche.loveapp

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import asunder.toche.loveapp.R
import kotlinx.android.synthetic.main.header_logo_blue_back.*
import kotlinx.android.synthetic.main.pill_reminder.*


/**
 * Created by admin on 8/2/2017 AD.
 */
class PillReminderActivity: AppCompatActivity(){


    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pill_reminder)
        txt_title.text ="PILL\nREMINDER"

        btn_take_pill.setOnClickListener {
            //startActivity(Intent().setClass(this@PillReminderActivity, PillReminderTimeActivity::class.java))
        }

        btn_msn.setOnClickListener {
            startActivity(Intent().setClass(this@PillReminderActivity, NotiMessageActivity::class.java))
        }



    }
}