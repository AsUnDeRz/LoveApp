package toche.asunder.loveapp.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.header_logo_white.*
import kotlinx.android.synthetic.main.passcode.*
import toche.asunder.loveapp.MyApp
import toche.asunder.loveapp.R


/**
 * Created by admin on 7/30/2017 AD.
 */
class PasscodeActivity :AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.passcode)

        delete.setOnClickListener {
            startActivity(Intent().setClass(this@PasscodeActivity, LoginActivity::class.java))
            finish()
        }

        setColorText()

    }

    private fun setColorText(){
        //custom typeface
        //val tf = Typeface.createFromAsset(assets, "fonts/AvenirLTStd-Heavy.otf")
        title_app.typeface = MyApp.typeFace.heavy
        txt_passcode.typeface = MyApp.typeFace.heavy
        delete.typeface = MyApp.typeFace.heavy
    }


    /*
    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }
    */

}