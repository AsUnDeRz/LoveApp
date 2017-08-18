package asunder.toche.loveapp

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import asunder.toche.loveapp.R
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.header_logo_white_back.*
import kotlinx.android.synthetic.main.reminder.*

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
        Glide.with(this)
                .load(R.drawable.bg_white)
                .into(bg_root)
        Glide.with(this)
                .load(R.drawable.bg_guy)
                .into(bg_top)
        btn_yes.typeface = MyApp.typeFace.heavy
        btn_no.typeface = MyApp.typeFace.heavy
        btn_back.setOnClickListener {
            onBackPressed()
        }


    }
}