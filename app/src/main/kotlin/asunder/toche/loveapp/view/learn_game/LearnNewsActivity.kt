package asunder.toche.loveapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import asunder.toche.loveapp.R
import kotlinx.android.synthetic.main.header_logo_white_back.*
import kotlinx.android.synthetic.main.learn_new.*
import kotlinx.android.synthetic.main.learn_new_finish.*


/**
 * Created by admin on 8/5/2017 AD.
 */
class LearnNewsActivity : AppCompatActivity() {

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.learn_new)

        title_header.text = "LEARNS\nAND GAMES"

        btn_back.setOnClickListener {
            onBackPressed()
        }


    }
}