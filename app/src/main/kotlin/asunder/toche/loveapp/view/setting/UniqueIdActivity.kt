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
import android.content.SharedPreferences
import android.databinding.ObservableArrayList
import android.preference.PreferenceManager
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.text.method.DigitsKeyListener
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.afollestad.materialdialogs.MaterialDialog
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


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

    var provinces = ObservableArrayList<Model.Province>()
    var provinID=""
    val provinTitle = ObservableArrayList<String>()
    lateinit var edtHbd :EditText
    lateinit var prefer : SharedPreferences
    lateinit var utils : Utils
    lateinit var appDb : AppDatabase
    var y:Int=0
    var m:Int=0
    var day:Int=0
    var fname:String=""
    var lname:String=""
    var isThai = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.unique_id_code)
        edtHbd = findViewById(R.id.edt_hbd)
        appDb = AppDatabase(this@UniqueIdActivity)
        prefer = PreferenceManager.getDefaultSharedPreferences(this@UniqueIdActivity)
        utils = Utils(this@UniqueIdActivity)

        Glide.with(this)
                .load(R.drawable.bg_white)
                .into(bg_root)
        txt_title.text = "UNIQUE\nID CODE"
        btn_save.typeface = MyApp.typeFace.heavy
        edt_fname.typeface = MyApp.typeFace.heavy
        edt_lname.typeface = MyApp.typeFace.heavy
        edtHbd.typeface = MyApp.typeFace.heavy



        if(prefer.getBoolean(KEYPREFER.isFirst,false)){
            vf_uic.displayedChild = 1
            isThai = intent.getBooleanExtra("isThai",false)
            d{"Check isThai $isThai"}
            txt_province.visibility = View.VISIBLE
            edt_province.visibility = View.VISIBLE
            loadProvince()
            edt_province.typeface = MyApp.typeFace.heavy
            edt_province.setOnClickListener { view ->
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
                showProvince()
            }
            edt_province.isFocusableInTouchMode = false
            edt_province.isFocusable =false
            //weightInput.setKeyListener(DigitsKeyListener.getInstance("0123456789."));


        }else{
            btn_back.setOnClickListener {
                onBackPressed()
            }
        }

        var regex=""
        when(isThai){
            true -> {regex="กขฃคฅฆงจฉชซฌญฎฏฐฑฒณดตถทธนบปผฝพฟภมยรลวศษสหฬอฮ"}
            false -> {regex="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"}
        }
        edt_fname.keyListener = DigitsKeyListener.getInstance(regex)
        edt_lname.keyListener = DigitsKeyListener.getInstance(regex)

        edtHbd.setOnClickListener {
            showTimePickerDialog(edt_hbd)
        }

        edtHbd.setOnFocusChangeListener { view, b ->
            if(b){
                showTimePickerDialog(edt_hbd)
            }
        }

        edt_fname.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(text: Editable?) {

                d{text.toString()}
                edt_fname.clearFocus()
                edt_lname.requestFocus()
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })

        edt_lname.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(text: Editable?) {
                d{text.toString()}
                edt_lname.clearFocus()
                edtHbd.requestFocus()
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })

        btn_save.setOnClickListener {
            if(prefer.getBoolean(KEYPREFER.isFirst,false)) {
                updateUser()
            }else{
                onBackPressed()

            }
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
            m = mounth+1
            day = dom
            if(isThai){
                y+=543
            }
            edtHbd.setText("$dom/$m/$y")
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

    fun showProvince(){
        MaterialDialog.Builder(this)
                .typeface(utils.medium,utils.medium)
                .title("Province")
                .items(provinTitle)
                .itemsCallback({ dialog, view, which, text ->
                    edt_province.setText(text)
                    d{"select province [$text]"}
                    provinID = provinces[which].province_id
                    d{"check provinID [$provinID] ["+provinces[which].toString()+"]"}
                })
                .positiveText(android.R.string.cancel)
                .show()
    }

    fun loadProvince(){
        if(appDb.getProvince().size != 0){
            val provin = ObservableArrayList<Model.Province>().apply {
                appDb.getProvince().forEach { item ->
                    run {
                        add(item)
                        provinTitle.add(utils.txtLocale(item.province_th, item.province_eng))
                    }
                }
            }
            provinces = provin
        }
    }


    fun updateUser(){
        btn_save.isClickable = false
        //check data before update
        fname = edt_fname.text.toString().toUpperCase()
        lname = edt_lname.text.toString().toUpperCase()
        val birth = "$y-$m-$day"
        //update edt_fcode.editableText.toString()
        if (prefer.getString(KEYPREFER.UserId, "") != "") {
            val userID = prefer.getString(KEYPREFER.UserId,"")
            val genderID = prefer.getString(KEYPREFER.GENDER,"")
            val nationID = prefer.getString(KEYPREFER.NATIONAL,"")
            d { " user_id[$userID] genderID[$genderID] fname[$fname] lname[$lname} birth[$birth] "}
            val update = service.updateUser(userID,genderID,null,fname,lname,null,null,null,
                    null,null,provinID,null,null,birth,"0",nationID)

            update.enqueue(object : Callback<Void> {
                override fun onFailure(call: Call<Void>?, t: Throwable?) {
                    d { t?.message.toString() }
                    btn_save.isClickable = true
                }
                override fun onResponse(call: Call<Void>?, response: Response<Void>?) {
                    if (response!!.isSuccessful) {
                        d { "update successful" }
                        //val editor = prefer.edit()
                        //editor.apply()
                        startActivity(Intent().setClass(this@UniqueIdActivity, ActivityMain::class.java))
                        finish()
                    }
                    btn_save.isClickable = true
                }
            })

        }
    }

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


}