package asunder.toche.loveapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import asunder.toche.loveapp.R
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

        btn_back.setOnClickListener {
            onBackPressed()
        }


    }
}