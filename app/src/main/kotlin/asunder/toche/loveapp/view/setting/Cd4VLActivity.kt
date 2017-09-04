package asunder.toche.loveapp

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.DialogFragment
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TimePicker
import com.bumptech.glide.Glide
import com.github.ajalt.timberkt.Timber
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.cd4_vl.*
import kotlinx.android.synthetic.main.header_logo_blue_back.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


/**
 * Created by ToCHe on 9/3/2017 AD.
 */
class Cd4VLActivity :AppCompatActivity(){


    var service : LoveAppService = LoveAppService.create()

    private var _compoSub = CompositeDisposable()
    private val compoSub: CompositeDisposable
        get() {
            if (_compoSub.isDisposed) {
                _compoSub = CompositeDisposable()
            }
            return _compoSub
        }

    protected final fun manageSub(s: Disposable) = compoSub.add(s)

    fun unsubscribe() { compoSub.dispose() }

    lateinit var edtDate : EditText
    lateinit var dateVal :Date
    var y:Int=0
    var m:Int=0
    var day:Int=0
    var fname:String=""
    var lname:String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.cd4_vl)


        edtDate = findViewById(R.id.edt_date)
        Glide.with(this)
                .load(R.drawable.bg_white)
                .into(bg_root)
        txt_title.text = "CD4&VL"
        edt_cd4.typeface = MyApp.typeFace.heavy
        edt_vl.typeface = MyApp.typeFace.heavy
        btn_save.typeface = MyApp.typeFace.heavy
        edtDate.typeface = MyApp.typeFace.heavy




        edtDate.setOnClickListener {
            showTimePickerDialog(edt_date)
        }

        edtDate.setOnFocusChangeListener { view, b ->
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
            if(b){
                showTimePickerDialog(edt_date)
            }
        }

        btn_back.setOnClickListener {
            onBackPressed()
        }

        btn_save.setOnClickListener {
            btn_save.isClickable = false
            inputHCD4VL(edt_vl.text.toString(),edt_cd4.text.toString())
        }


    }

    fun inputHCD4VL(cd4:String,vl:String){
        Timber.d { "input CD4 VL" }
        val preferences = PreferenceManager.getDefaultSharedPreferences(this@Cd4VLActivity)
        if (preferences.getString(KEYPREFER.UserId, "") != "") {
            val userID = preferences.getString(KEYPREFER.UserId,"")
            Timber.d { " user_id[" + preferences.getString(KEYPREFER.UserId, "") + "]" }
            val addCD4 = service.addLabResult(userID,vl,cd4,dateVal)
            addCD4.enqueue(object : Callback<Void> {
                override fun onFailure(call: Call<Void>?, t: Throwable?) {
                    Timber.d { t?.message.toString() }
                    btn_save.isClickable = true
                }

                override fun onResponse(call: Call<Void>?, response: Response<Void>?) {
                    if (response!!.isSuccessful) {
                        Timber.d { "addCD4 successful" }
                        onBackPressed()
                    }
                    btn_save.isClickable = true
                }
            })
        }
    }


    fun showTimePickerDialog(v: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(v.windowToken, 0)
        val newFragment = TimePickerFragment()
        newFragment.show(fragmentManager, "timePicker")
    }


    @SuppressLint("ValidFragment")
    inner class TimePickerFragment : DialogFragment(), TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {


        override fun onDateSet(p0: DatePicker?, year: Int, mounth: Int, dom: Int) {
            Timber.d { "$year  $mounth  $dom" }
            y = year
            m = mounth
            day = dom
            val calendar = Calendar.getInstance()
            calendar.set(year, mounth, dom)
            edtDate.setText("$dom/$mounth/$year")
            dateVal = calendar.time
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
                Timber.d { "calendat set " + cal.time.toString() }
            }
            callCount++
        }
    }


    override fun onPause() {
        super.onPause()
        unsubscribe()
    }
}