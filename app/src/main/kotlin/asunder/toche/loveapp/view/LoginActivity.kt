package asunder.toche.loveapp

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import asunder.toche.loveapp.R
import kotlinx.android.synthetic.main.header_logo_blue_back.*
import kotlinx.android.synthetic.main.login.*


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
            //check login
            startActivity(Intent().setClass(this@LoginActivity, ActivityMain::class.java))
            finish()
        }
        btn_back.setOnClickListener {
            onBackPressed()
        }
    }

     override fun onBackPressed() {
        super.onBackPressed()
         startActivity(Intent().setClass(this@LoginActivity, OldNewUserActivity::class.java))
         finish()

    }

}