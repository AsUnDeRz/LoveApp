package toche.asunder.loveapp.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.header_logo_blue_back.*
import kotlinx.android.synthetic.main.pill_reminder_time.*
import toche.asunder.loveapp.MyApp
import toche.asunder.loveapp.R
import toche.asunder.loveapp.adapter.PillReminderTimeAdapter

/**
 * Created by admin on 8/3/2017 AD.
 */
class PillReminderTimeActivity : AppCompatActivity(){

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pill_reminder_time)

        txt_title.typeface = MyApp.typeFace.heavy

        rv_reminder_time.layoutManager = LinearLayoutManager(this)
        rv_reminder_time.setHasFixedSize(true)
        rv_reminder_time.adapter = PillReminderTimeAdapter(this@PillReminderTimeActivity)

    }



}