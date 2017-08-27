package asunder.toche.loveapp

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import asunder.toche.loveapp.R
import kotlinx.android.synthetic.main.change_passcode.*
import kotlinx.android.synthetic.main.header_logo_blue_back.*


/**
 * Created by admin on 8/2/2017 AD.
 */
class ChangePasscodeActivity: AppCompatActivity(){


    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.change_passcode)

        btn_change_passcode.setOnClickListener {
            val data = Intent()
            data.putExtra(KEYPREFER.PASSCODE,"change")
            startActivity(data.setClass(this@ChangePasscodeActivity,PassCodeActivity::class.java))
        }
    }
}