package asunder.toche.loveapp

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.DialogFragment
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.databinding.ObservableArrayList
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TimePicker
import com.afollestad.materialdialogs.MaterialDialog
import com.bumptech.glide.Glide
import com.github.ajalt.timberkt.Timber.d
import com.tapadoo.alerter.Alerter
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.header_logo_blue_back.*
import kotlinx.android.synthetic.main.unique_id_code.*
import java.util.*
import java.util.regex.Pattern


/**
 * Created by admin on 8/4/2017 AD.
 */
class UniqueIdActivity: AppCompatActivity() ,com.layernet.thaidatetimepicker.date.DatePickerDialog.OnDateSetListener{


    override fun onDateSet(view: com.layernet.thaidatetimepicker.date.DatePickerDialog?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        d{"$year  $monthOfYear  $dayOfMonth"}
        y = year+543
        m = monthOfYear+1
        day = dayOfMonth
        edtHbd.setText("$day/$m/$y")
    }

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
    var provinID :String?=null
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
        txt_title.text = "LoveApp"
        btn_save.typeface = MyApp.typeFace.heavy
        edt_fname.typeface = MyApp.typeFace.heavy
        edt_lname.typeface = MyApp.typeFace.heavy
        edtHbd.typeface = MyApp.typeFace.heavy



        if(prefer.getBoolean(KEYPREFER.isFirst,false)){
            vf_uic.displayedChild = 1
            isThai = intent.getBooleanExtra("isThai",false)
            d{"Check isThai $isThai"}
            if(isThai) {
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
                edt_province.isFocusable = false
                //weightInput.setKeyListener(DigitsKeyListener.getInstance("0123456789."));
            }

        }else{
            btn_back.setOnClickListener {
                onBackPressed()
            }
        }

        val hasTh = LocalUtil.getLanguage(this@UniqueIdActivity)
        when(hasTh){
            "th" -> {isThai = true}
            "en" -> {isThai = false}
        }


        var regex=""
        when(isThai){
            true -> {regex="[กขฃคฅฆงจฉชซฌญฎฏฐฑฒณดตถทธนบปผฝพฟภมยรลวศษสหฬอฮ]"}
            false -> {regex="[abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ]"}
        }
        //edt_fname.keyListener = DigitsKeyListener.getInstance(regex)
        //edt_lname.keyListener = DigitsKeyListener.getInstance(regex)

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
                val length = text.toString().length

                if(length > 0 && !Pattern.matches(regex, text)) {
                    text?.delete(length - 1, length)
                }
                if(text.toString().length == 1){
                    d { text.toString() }
                    edt_fname.clearFocus()
                    edt_lname.requestFocus()
                }
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })

        edt_lname.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(text: Editable?) {
                val length = text.toString().length

                if(length > 0 && !Pattern.matches(regex, text)) {
                    text?.delete(length - 1, length)
                }
                if(text.toString().length == 1){
                    d { text.toString() }
                    edt_lname.clearFocus()
                    edtHbd.requestFocus()
                }
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })

        PushDownAnim.setOnTouchPushDownAnim(btn_save)
        btn_save.setOnClickListener {
            fname = edt_fname.text.toString()
            lname = edt_lname.text.toString()
            if(y == 0 || m == 0 || day == 0 || fname == "" || lname == ""){
                showPopup()
            }else{
                if(prefer.getBoolean(KEYPREFER.isFirst,false)) {
                    if(provinID =="" && isThai){
                        showPopup()
                    }else {
                        updateUser()
                    }
                }else{
                    onBackPressed()

                }
            }
        }


    }


    fun showTimePickerDialog(v: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(v.windowToken, 0)
        showSpinner()
        if(isThai) {
            //val datePopup = com.layernet.thaidatetimepicker.date.DatePickerDialog.newInstance(this, year, mount, dOfm)
            //datePopup.show(fragmentManager, "datepicker")
        }else{
            //val newFragment = TimePickerFragment()
            //newFragment.show(fragmentManager, "timePicker")

        }
    }

    fun showSpinner(){
        val hasTh = LocalUtil.getLanguage(this@UniqueIdActivity)
        var isthai=0
        when(hasTh){
            "th" -> {isthai=543}
            "en" -> {isthai=0}
        }
        val c = Calendar.getInstance()
        val mount = c.get(Calendar.MONTH)
        val dOfm = c.get(Calendar.DAY_OF_MONTH)
        var year = c.get(Calendar.YEAR)
        SpinnerDatePickerDialogBuilder()
                .context(this)
                .callback { view, year, monthOfYear, dayOfMonth ->
                    d{"$year  $monthOfYear  $dayOfMonth"}
                    y = year
                    m = monthOfYear+1
                    day = dayOfMonth
                    edtHbd.setText("$day/$m/$y")
                }
                .spinnerTheme(R.style.DatePickerSpinner)
                .year(year+isthai)
                .monthOfYear(mount)
                .dayOfMonth(dOfm)
                .build()
                .show()
    }

    @SuppressLint("ValidFragment")
    inner class TimePickerFragment : DialogFragment(), TimePickerDialog.OnTimeSetListener,DatePickerDialog.OnDateSetListener{

        override fun onDateSet(p0: DatePicker?, year: Int, mounth: Int, dom: Int) {
            d{"$year  $mounth  $dom"}
            y = year
            m = mounth+1
            day = dom
            edtHbd.setText("$dom/$m/$y")
        }


        var callCount = 0


        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

            val c = Calendar.getInstance()
            val mount = c.get(Calendar.MONTH)
            val dOfm = c.get(Calendar.DAY_OF_MONTH)
            val hour = c.get(Calendar.HOUR_OF_DAY)
            val minute = c.get(Calendar.MINUTE)
            var year = c.get(Calendar.YEAR)

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
                .title(getString(R.string.province))
                .items(provinTitle.sorted())
                .itemsCallback({ dialog, view, which, text ->
                    edt_province.setText(text)
                    provinces.filter { text == it.province_th || text == it.province_eng }
                            .forEach {
                                provinID = it.province_id
                                d{"check provinID [$provinID] ["+it.toString()+"]"}
                            }
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

            manageSub(
                    mService.UpdateUser(userID.toDouble().toInt().toString(),genderID,null,fname,lname,null,null,null,
                    null,null,provinID,null,null,birth,"0",nationID)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({ c -> run {
                                btn_save.isClickable = true
                                when(c.header.code){
                                    "200" -> {
                                        d { "update successful" }
                                        //val editor = prefer.edit()
                                        //editor.apply()
                                        val data = Intent()
                                        data.putExtra(KEYPREFER.PASSCODE,"change")
                                        startActivity(data.setClass(this@UniqueIdActivity,PassCodeActivity::class.java))
                                        //startActivity(Intent().setClass(this@UniqueIdActivity, ActivityMain::class.java))
                                        finish()
                                    }
                                }
                            }},{
                                d { it.message!! }
                            })
            )


            /*
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
                        val data = Intent()
                        data.putExtra(KEYPREFER.PASSCODE,"change")
                        startActivity(data.setClass(this@UniqueIdActivity,PassCodeActivity::class.java))
                        //startActivity(Intent().setClass(this@UniqueIdActivity, ActivityMain::class.java))
                        finish()
                    }
                    btn_save.isClickable = true
                }
            })
            */



        }
    }

    var service : LoveAppService = LoveAppService.create()
    var mService : newService = newService.create()

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

    fun showPopup(){
        Alerter.create(this@UniqueIdActivity)
                .setBackgroundColorInt(ContextCompat.getColor(this,R.color.red))
                .setIcon(R.drawable.ic_clear_white_48dp)
                .setText(getString(R.string.fillinformation))
                .setTextTypeface(utils.medium)
                .show()
    }


}