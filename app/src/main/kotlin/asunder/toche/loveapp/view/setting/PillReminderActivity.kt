package asunder.toche.loveapp

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.app.DialogFragment
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.format.DateFormat
import android.view.View
import android.widget.TimePicker
import android.widget.Toast
import asunder.toche.loveapp.R
import com.github.ajalt.timberkt.Timber.d
import com.tapadoo.alerter.Alerter
import kotlinx.android.synthetic.main.header_logo_blue_back.*
import kotlinx.android.synthetic.main.pill_reminder.*
import java.util.*


/**
 * Created by admin on 8/2/2017 AD.
 */
class PillReminderActivity: AppCompatActivity(){


    override fun onBackPressed() {
        super.onBackPressed()
        d{"before finish [$message][$dateLong][$hasAlert]"}
        val cal = Calendar.getInstance()
        cal.timeInMillis = dateLong
        //insert notification
        if(isStateAdd) {
            if(dateLong != 0L && message != ""){
                appDb.addNotification(Model.Notification("123", "Pill Reminder", message, Date(dateLong)))
                val lastRow = appDb.getNotificationList().last()
                d { "Check last Row id [" + lastRow.id + "]" }

                if (hasAlert) {
                    //insert service notification
                    Notification.EventReceiver.setupAlarm(this, cal, Integer.parseInt(lastRow.id))
                }
                PillReminderTimeActivity.add(Model.PillReminder(lastRow.id.toLong(), message, Date(dateLong)))
            }
        }else{
            notification.message = message
            notification.time = Date(dateLong)
            appDb.updateNotification(notification,KEYPREFER.WAITING)
            if (hasAlert) {
                //insert service notification
                Notification.EventReceiver.setupAlarm(this, cal, Integer.parseInt(notification.id))
            }
            PillReminderTimeActivity.update(Model.PillReminder(notification.id.toLong(), message, Date(dateLong)),position)
        }
        finish()

    }

    lateinit var utils :Utils
    var position = 0
    var dateLong : Long =0L
    var hasAlert = false
    var message =""
    lateinit var appDb :AppDatabase
    var isStateAdd =false
    lateinit var notification:  Model.Notification


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pill_reminder)
        txt_title.text ="PILL\nREMINDER"
        utils = Utils(this@PillReminderActivity)
        appDb = AppDatabase(this)

        //init
        isStateAdd = intent.getBooleanExtra(KEYPREFER.isFirst,false)
        if(!isStateAdd){
            notification = appDb.getNotiWithState(intent.getStringExtra(KEYPREFER.NOTIID))!!
            message = notification.message
            dateLong = notification.time.time
            position = intent.getIntExtra(KEYPREFER.POSITION,0)
        }
        sb_ios.setOnCheckedChangeListener { compoundButton, b ->
            if(b){
                hasAlert = b
                d{"Swicth button is $b"}
            }else{
                hasAlert = b
                d{"Swicth button is $b"}
            }
            alerter("")
        }

        btn_msn.setOnClickListener {
            startActivityForResult(Intent().setClass(this@PillReminderActivity, NotiMessageActivity::class.java),
                    1)
        }

        btn_take_pill.setOnClickListener {
            showTimePickerDialog(btn_take_pill)
        }

        btn_back.setOnClickListener {
            onBackPressed()
        }
    }

    fun alerter(message:String?){
        val noti = if(hasAlert){"Want to alert"}else{ "Do not alert" }
        Alerter.create(this)
                .setTitle("PILL REMINDER")
                .setText("Take pill at "+utils.getDate(Date(dateLong))+" \n"+
                "With Message $message \n"+
                "$noti")
                .setTitleTypeface(Utils(this).heavy)
                .setTextTypeface(Utils(this).heavy)
                .setBackgroundColorRes(R.color.colorAccent) // or setBackgroundColorInt(Color.CYAN)
                .show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 1){
            if(data != null) {
                alerter(data.getStringExtra(KEYPREFER.RESULT))
                message = data.getStringExtra(KEYPREFER.RESULT)
            }
            //Toast.makeText(this@PillReminderActivity,data?.getStringExtra(KEYPREFER.RESULT),Toast.LENGTH_LONG).show()
        }

    }

    fun showTimePickerDialog(v: View) {
        val newFragment = TimePickerFragment()
        newFragment.show(fragmentManager, "timePicker")
    }



    @SuppressLint("ValidFragment")
    inner class TimePickerFragment : DialogFragment(), TimePickerDialog.OnTimeSetListener {

        var callCount = 0


        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
        }

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
                dateLong = cal.timeInMillis
                alerter("")
                //Notification.EventReceiver.setupAlarm(activity,cal)
                d{"calendat set "+cal.time.toString()}

            }
            callCount++
        }
    }
}