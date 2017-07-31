package toche.asunder.loveapp.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.header_logo_white_back.*
import kotlinx.android.synthetic.main.reminder.*
import toche.asunder.loveapp.MyApp
import toche.asunder.loveapp.R

/**
 * Created by admin on 7/31/2017 AD.
 */
class ReminderActivity: AppCompatActivity() {

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent().setClass(this@ReminderActivity, NotificationActivity::class.java))
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.reminder)

       title_header.typeface = MyApp.typeFace.heavy
        txt_reminder.typeface = MyApp.typeFace.heavy
        btn_yes.typeface = MyApp.typeFace.heavy
        btn_no.typeface = MyApp.typeFace.heavy
        btn_back.setOnClickListener {
            onBackPressed()
        }


    }
}