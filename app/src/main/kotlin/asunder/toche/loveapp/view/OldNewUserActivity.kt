package asunder.toche.loveapp

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.old_new_user.*


/**
 * Created by admin on 8/8/2017 AD.
 */
class OldNewUserActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.old_new_user)

        PushDownAnim.setOnTouchPushDownAnim(btn_signin)
        PushDownAnim.setOnTouchPushDownAnim(btn_newaccout)

        btn_signin.setOnClickListener {
            startActivity(Intent().setClass(this@OldNewUserActivity,LoginActivity::class.java))
            finish()
        }

        btn_newaccout.setOnClickListener {
            startActivity(Intent().setClass(this@OldNewUserActivity,ConditionActivity::class.java))
            finish()
        }
    }


}