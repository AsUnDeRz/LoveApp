package asunder.toche.loveapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import asunder.toche.loveapp.R

/**
 * Created by admin on 8/7/2017 AD.
 */
class LoadingActivity:AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.loading_page)



        val intentThis = Intent()
        val splash = Handler()
        splash.postDelayed({
            startActivity(intentThis.setClass(this, OldNewUserActivity::class.java))
            finish()
        },3000)
    }
}