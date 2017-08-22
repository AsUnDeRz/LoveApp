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
            startActivity(Intent().setClass(this@OldNewUserActivity,ActivityMain::class.java))
            finish()
        }

        btn_newaccout.setOnClickListener {
            showTimePickerDialog(btn_newaccout)
            //Notification.EventReceiver.setupAlarm(applicationContext)

            //startActivity(Intent().setClass(this@OldNewUserActivity,ActivityMain::class.java))
            //finish()
        }
    }

    fun showTimePickerDialog(v: View) {
        val newFragment = TimePickerFragment()
        newFragment.show(fragmentManager, "timePicker")
    }

    class TimePickerFragment : DialogFragment(), TimePickerDialog.OnTimeSetListener {

        var callCount = 0


        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

            val c = Calendar.getInstance()
            val hour = c.get(Calendar.HOUR_OF_DAY)
            val minute = c.get(Calendar.MINUTE)

            return TimePickerDialog(activity, this, hour, minute,
                    DateFormat.is24HourFormat(activity))
        }


        override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {

            if (callCount == 0) {
                // Do something with the time chosen by the user
                val cal = Calendar.getInstance()
                cal.set(Calendar.HOUR_OF_DAY, hourOfDay)
                cal.set(Calendar.MINUTE, minute)
                Notification.EventReceiver.setupAlarm(activity,cal)
                d{"calendat set "+cal.time.toString()}
            }
            callCount++
        }
    }
}