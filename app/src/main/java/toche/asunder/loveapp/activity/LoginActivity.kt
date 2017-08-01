package toche.asunder.loveapp.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.header_logo_blue_back.*
import kotlinx.android.synthetic.main.login.*
import toche.asunder.loveapp.MyApp
import toche.asunder.loveapp.R

/**
 * Created by admin on 7/31/2017 AD.
 */
class LoginActivity :AppCompatActivity(){


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        edit_email.typeface = MyApp.typeFace.heavy
        edit_password.typeface = MyApp.typeFace.heavy
        txt_title.typeface = MyApp.typeFace.heavy
        txt_email.typeface = MyApp.typeFace.heavy
        txt_login.typeface = MyApp.typeFace.heavy
        txt_password.typeface = MyApp.typeFace.heavy
        forgetpassword.typeface = MyApp.typeFace.heavy

        login_btn.setOnClickListener {
            startActivity(Intent().setClass(this@LoginActivity, ActivityMain::class.java))
            finish()
        }


    }

}