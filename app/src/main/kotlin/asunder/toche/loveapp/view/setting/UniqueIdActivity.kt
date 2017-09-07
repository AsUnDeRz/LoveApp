package asunder.toche.loveapp

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.format.DateFormat
import android.view.View
import android.widget.DatePicker
import android.widget.TimePicker
import asunder.toche.loveapp.R
import com.bumptech.glide.Glide
import com.github.ajalt.timberkt.Timber
import com.github.ajalt.timberkt.Timber.d
import kotlinx.android.synthetic.main.header_logo_blue_back.*
import kotlinx.android.synthetic.main.unique_id_code.*
import java.util.*
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Intent
import android.text.InputFilter
import android.view.inputmethod.InputMethodManager
import android.widget.EditText


/**
 * Created by admin on 8/4/2017 AD.
 */
class UniqueIdActivity: AppCompatActivity() {

    override fun onBackPressed() {
        super.onBackPressed()
        fname = edt_fname.text.toString().toUpperCase()
        lname = edt_lname.text.toString().toUpperCase()
        val uniID = fname+lname+"$day$m$y"
        d{"Check uniID before finish $uniID"}
        AccountSettingActivity.setUnique(uniID,y,m,day,fname,lname)
        finish()
    }

    lateinit var edtHbd :EditText
    var y:Int=0
    var m:Int=0
    var day:Int=0
    var fname:String=""
    var lname:String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.unique_id_code)
        edtHbd = findViewById(R.id.edt_hbd)


        Glide.with(this)
                .load(R.drawable.bg_white)
                .into(bg_root)
        txt_title.text = "UNIQUE\nID CODE"
        btn_save.typeface = MyApp.typeFace.heavy
        edt_fname.typeface = MyApp.typeFace.heavy
        edt_lname.typeface = MyApp.typeFace.heavy
        edtHbd.typeface = MyApp.typeFace.heavy


        btn_back.setOnClickListener {
            onBackPressed()
        }

        edtHbd.setOnClickListener {
            showTimePickerDialog(edt_hbd)
        }

        edtHbd.setOnFocusChangeListener { view, b ->
            if(b){
                showTimePickerDialog(edt_hbd)
            }
        }

        btn_save.setOnClickListener {
            onBackPressed()
        }


    }


    fun showTimePickerDialog(v: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(v.windowToken, 0)
        val newFragment = TimePickerFragment()
        newFragment.show(fragmentManager, "timePicker")
    }

    @SuppressLint("ValidFragment")
    inner class TimePickerFragment : DialogFragment(), TimePickerDialog.OnTimeSetListener,DatePickerDialog.OnDateSetListener {


        override fun onDateSet(p0: DatePicker?, year: Int, mounth: Int, dom: Int) {
            d{"$year  $mounth  $dom"}
            y = year
            m = mounth
            day = dom
            edtHbd.setText("$dom/$mounth/$year")
        }


        var callCount = 0


        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

            val c = Calendar.getInstance()
            val mount = c.get(Calendar.MONTH)
            val dOfm = c.get(Calendar.DAY_OF_MONTH)
            val hour = c.get(Calendar.HOUR_OF_DAY)
            val minute = c.get(Calendar.MINUTE)
            val year = c.get(Calendar.YEAR)

            /*
            return TimePickerDialog(activity, this, hour, minute,
                    DateFormat.is24HourFormat(activity))
                    */
            return DatePickerDialog(activity,this,year,mount,dOfm)
        }


        override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {

            if (callCount == 0) {
                // Do something with the time chosen by the user
                val cal = Calendar.getInstance()
                cal.set(Calendar.HOUR_OF_DAY, hourOfDay)
                cal.set(Calendar.MINUTE, minute)
                d { "calendat set " + cal.time.toString() }
            }
            callCount++
        }
    }
}