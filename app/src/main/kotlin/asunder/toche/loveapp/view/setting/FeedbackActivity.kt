package asunder.toche.loveapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import asunder.toche.loveapp.R
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.feedback.*
import kotlinx.android.synthetic.main.header_logo_white_back.*

/**
 * Created by admin on 8/13/2017 AD.
 */
class FeedbackActivity: AppCompatActivity(){


    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.feedback)

        Glide.with(this)
                .load(R.drawable.bg_feedback)
                .into(bg_feedback)
        Glide.with(this)
                .load(R.drawable.bg_white)
                .into(bg_root)

        btn_back.setOnClickListener {
            onBackPressed()
        }


    }
}