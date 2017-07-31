package toche.asunder.loveapp.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.header_logo_blue_back.*
import kotlinx.android.synthetic.main.notification.*
import toche.asunder.loveapp.MyApp
import toche.asunder.loveapp.R
import toche.asunder.loveapp.adapter.NotiAdapter

/**
 * Created by admin on 7/31/2017 AD.
 */
class NotificationActivity : AppCompatActivity() {

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent().setClass(this@NotificationActivity, HomeActivity::class.java))
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.notification)

        title_noti.typeface = MyApp.typeFace.heavy
        txt_title.typeface = MyApp.typeFace.heavy
        number_noti.typeface = MyApp.typeFace.medium
        btn_back.setOnClickListener {
            onBackPressed()
        }

        rv_notification.layoutManager = LinearLayoutManager(this)
        rv_notification.setHasFixedSize(true)
        rv_notification.adapter = NotiAdapter(this@NotificationActivity)

    }
}