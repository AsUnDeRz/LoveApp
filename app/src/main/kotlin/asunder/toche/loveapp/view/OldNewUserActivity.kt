package asunder.toche.loveapp

import android.arch.lifecycle.LifecycleActivity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import asunder.toche.loveapp.R
import kotlinx.android.synthetic.main.old_new_user.*

/**
 * Created by admin on 8/8/2017 AD.
 */
class OldNewUserActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.old_new_user)

        btn_signin.setOnClickListener {
            startActivity(Intent().setClass(this@OldNewUserActivity,ActivityMain::class.java))
            finish()
        }

        btn_newaccout.setOnClickListener {
            startActivity(Intent().setClass(this@OldNewUserActivity,ActivityMain::class.java))
            finish()
        }
    }
}