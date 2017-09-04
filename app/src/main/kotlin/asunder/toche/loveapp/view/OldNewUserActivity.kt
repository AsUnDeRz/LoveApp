package asunder.toche.loveapp

import android.annotation.SuppressLint
import android.app.Dialog
import android.arch.lifecycle.LifecycleActivity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import asunder.toche.loveapp.R
import kotlinx.android.synthetic.main.old_new_user.*
import android.widget.TimePicker
import android.text.format.DateFormat.is24HourFormat
import android.app.TimePickerDialog
import android.app.DialogFragment
import android.text.format.DateFormat
import android.view.View
import com.github.ajalt.timberkt.Timber.d
import java.util.*






/**
 * Created by admin on 8/8/2017 AD.
 */
class OldNewUserActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.old_new_user)


        btn_signin.setOnClickListener {
            startActivity(Intent().setClass(this@OldNewUserActivity,LoginActivity::class.java))
            finish()
        }

        btn_newaccout.setOnClickListener {
            startActivity(Intent().setClass(this@OldNewUserActivity,GenderActivity::class.java))
            finish()
        }
    }


}