package asunder.toche.loveapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.databinding.ObservableArrayList
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.Html
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Base64
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.afollestad.materialdialogs.MaterialDialog
import com.bumptech.glide.Glide
import com.github.ajalt.timberkt.Timber.d
import com.tapadoo.alerter.Alerter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.account_setting.*
import kotlinx.android.synthetic.main.header_logo_blue_back.*
import java.text.SimpleDateFormat
import java.util.regex.Pattern


/**
 * Created by admin on 8/4/2017 AD.
 */
class AccountSettingActivity: AppCompatActivity() {

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

    var nations = ObservableArrayList<Model.National>()
    var nationID =""
    var nationTitle = ObservableArrayList<String>()
    var provinces = ObservableArrayList<Model.Province>()
    var provinID=""
    val provinTitle = ObservableArrayList<String>()
    var jobs = ObservableArrayList<Model.RepositoryJob>()
    var jobID=""
    var jobTitle = ObservableArrayList<String>()
    var statusList = ObservableArrayList<Model.HivStatus>()
    var statusTitle =  ObservableArrayList<String>()
    var statusID=""
    var dataUser = ObservableArrayList<Model.User>()
    lateinit var prefer :SharedPreferences
    lateinit var utils :Utils
    lateinit var appdb : AppDatabase

    fun loadProvince(){
            manageSub(
                    service.getProvinces()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({ c ->
                                run {
                                    var Title = ""
                                    val data = ObservableArrayList<Model.Province>().apply {
                                        c.forEach { item ->
                                            run {
                                                add(item)
                                                provinTitle.add(utils.txtLocale(item.province_th, item.province_eng))
                                                if (item.province_id == dataUser[0].province) {
                                                    Title = utils.txtLocale(item.province_th, item.province_eng)
                                                }
                                            }
                                        }
                                    }
                                    edt_province.setText(Title)
                                    provinces = data
                                    d { "check response [" + c.size + "]" }
                                }
                            }, {
                                d { it.message!! }
                            })
            )

    }


    companion object {
        fun setUnique(data:String,year:Int,month:Int,day:Int,f:String,l:String){
            edtUniId.setText(data)
            birth ="$year-$month-$day"
            fname = f
            lname = l
            d{ data  }
            d{"check $birth"}
        }

        @SuppressLint("StaticFieldLeak")
        lateinit var edtUniId :EditText
        var birth:String=""
        var fname:String=""
        var lname:String=""
    }
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.account_setting)
        appdb = AppDatabase(this)
        utils = Utils(this)
        prefer = PreferenceManager.getDefaultSharedPreferences(this@AccountSettingActivity)
        Glide.with(this)
                .load(R.drawable.bg_white)
                .into(bg_root)
        txt_title.text = "SETTING"
        edt_nation.typeface = MyApp.typeFace.heavy
        //edt_phone.typeface = MyApp.typeFace.heavy
        //edt_mcode.typeface = MyApp.typeFace.heavy
        //edt_fcode.typeface = MyApp.typeFace.heavy
        edt_email.typeface = MyApp.typeFace.heavy
        edt_password.typeface = MyApp.typeFace.heavy
        edt_unique.typeface = MyApp.typeFace.heavy
        edt_province.typeface = MyApp.typeFace.heavy
        edt_work.typeface = MyApp.typeFace.heavy
        edt_number_id.typeface = MyApp.typeFace.heavy
        edtUniId = findViewById(R.id.edt_unique)
        loadUser(prefer.getString(KEYPREFER.UserId,"0"))
        statusList.apply {
            add(Model.HivStatus("1","POSITIVE","บวก"))
            add(Model.HivStatus("2","NEGATIVE","ลบ"))
            add(Model.HivStatus("3","I DON'T KNOW","ไม่ทราบ"))
        }

        btn_back.setOnClickListener {
            onBackPressed()
        }


        edt_unique.setOnClickListener { view ->
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
            startActivity(Intent().setClass(this@AccountSettingActivity,UniqueIdActivity::class.java))
        }

        edt_unique.setOnFocusChangeListener { view, b ->
            if(b){
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
                startActivity(Intent().setClass(this@AccountSettingActivity,UniqueIdActivity::class.java))

            }
        }

        edt_province.setOnClickListener { view ->
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
           showProvince()
        }
        txt_hiv_status.setOnClickListener {
            showStatus()
        }
        edt_nation.setOnClickListener { view ->
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
            showNation()
        }
        edt_nation.isFocusableInTouchMode = false
        edt_nation.isFocusable = false
        edt_province.isFocusableInTouchMode = false
        edt_province.isFocusable =false
        edt_work.isFocusableInTouchMode = false
        edt_work.isFocusable =false
        edt_work.setOnClickListener { view ->
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
            showJob()
        }

        PushDownAnim.setOnTouchPushDownAnim(btn_save)
        btn_save.setOnClickListener {
            if(validate()) {
                updateUser()
                d{"validate success"}
            }else{
                d{"validate fail"}
                showPopup()

            }
        }

        setPasswordListener()

    }

    fun loadJob(){
        if(appdb.getJobs().size != 0){
            var Title = ""
            val job = ObservableArrayList<Model.RepositoryJob>().apply {
                appdb.getJobs().forEach { item ->
                    run {
                        add(item)
                        jobTitle.add(utils.txtLocale(item.occupation_th, item.occupation_eng))
                        if (item.occupation_id == dataUser[0].job) {
                            Title = utils.txtLocale(item.occupation_th, item.occupation_eng)
                        }
                    }
                }
            }
            edt_work.setText(Title)
            jobs = job

        }
    }
    fun showProvince(){
        MaterialDialog.Builder(this)
                .typeface(utils.medium,utils.medium)
                .title(getString(R.string.province))
                .items(provinTitle.sorted())
                .itemsCallback({ dialog, view, which, text ->
                    edt_province.setText(text)
                    d{"select province [$text]"}
                    provinces.filter { text == it.province_th || text == it.province_eng }
                            .forEach {
                                provinID = it.province_id
                                d{"check provinID [$provinID] ["+it.toString()+"]"}
                            }
                })
                .positiveText(android.R.string.cancel)
                .show()
    }

    fun showJob(){
        MaterialDialog.Builder(this)
                .typeface(utils.medium,utils.medium)
                .title(getString(R.string.work))
                .items(jobTitle)
                .itemsCallback({ dialog, view, which, text ->
                    edt_work.setText(text)
                    d{"select job [$text]"}
                    jobID = jobs[which].occupation_id
                    d{"check jobID [$jobID] ["+jobs[which].toString()+"]"}
                })
                .positiveText(android.R.string.cancel)
                .show()
    }

    fun loadStatus(){
        statusTitle = ObservableArrayList<String>()
        statusTitle.apply {
            statusList.forEach {
                item -> add(utils.txtLocale(item.status_th,item.status_eng))
            }
        }

    }

    fun showStatus(){
        loadStatus()
        MaterialDialog.Builder(this)
                .typeface(utils.medium,utils.medium)
                .title(getString(R.string.hivstatus))
                .items(statusTitle)
                .itemsCallback({ dialog, view, which, text ->
                    txt_hiv_status.text = text
                    d{"select status [$text]"}
                    statusID = statusList[which].status_id
                    d{ """check statusID [$statusID] [${statusList[which]}]""" }
                })
                .positiveText(android.R.string.cancel)
                .show()
    }

        fun loadUser(id:String){
            manageSub(
                    service.getUser(id)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({ c -> run {
                               val data = ObservableArrayList<Model.User>().apply {
                                    c.forEach {
                                        item -> add(item)
                                        d { "Add [$item] to arraylist" }
                                    }
                                }
                                dataUser = data
                                setDataUser(dataUser[0])
                                d { "check response [" + dataUser.size + "]" }
                                loadJob()
                                loadNation()
                                if(appdb.getProvince().size != 0){
                                    var Title = ""
                                    val provin = ObservableArrayList<Model.Province>().apply {
                                        appdb.getProvince().forEach { item ->
                                            run {
                                                add(item)
                                                provinTitle.add(utils.txtLocale(item.province_th, item.province_eng))
                                                if (item.province_id == dataUser[0].province) {
                                                    Title = utils.txtLocale(item.province_th, item.province_eng)
                                                }
                                            }
                                        }
                                    }
                                    edt_province.setText(Title)
                                    provinces = provin

                                }else{
                                    loadProvince()
                                }
                            }},{
                                d { it.message!! }
                            })

            )
        }

    fun loadNation(){
        if(appdb.getNations().isNotEmpty()){
            var title=""
            val rawData = appdb.getNations()
            rawData.sortBy { it.national_id }
            rawData.forEach {
                nationTitle.add(utils.txtLocale(it.nationality_th, it.nationality_eng))
                nations.add(Model.National(it.national_id.toLong(), utils.txtLocale(it.nationality_th, it.nationality_eng)))
                if(it.national_id == dataUser[0].national_id){
                    title = utils.txtLocale(it.nationality_th, it.nationality_eng)
                }
            }
            edt_nation.setText(title)

        }

    }

    fun showNation(){
        MaterialDialog.Builder(this)
                .typeface(utils.medium,utils.medium)
                .title(getString(R.string.nationtitle))
                .items(nationTitle)
                .itemsCallback({ dialog, view, which, text ->
                    edt_nation.setText(text)
                    d{"select nation [$text]"}
                    nationID = nations[which].id.toString()
                    d{ """check nationID [$nationID] [${nations[which]}]""" }
                })
                .positiveText(android.R.string.cancel)
                .show()

    }

    fun setDataUser(data:Model.User){
        d{"Birth in user =["+data.birth+"]"}
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        val formatUni = SimpleDateFormat("ddMMyyy")
        val formatSend = SimpleDateFormat("yyyy-MM-dd")
        val date = if(data.birth != null){formatter.parse(data.birth)}else{""}
        var mixUnique =""
        if(data.first_name!= null && data.first_surname !=null && data.birth != null) {
            mixUnique = data.first_name + data.first_surname + formatUni.format(date)
        }

        //edt_phone.setText(if(data.phone != null){data.phone!!}else{""})
        //edt_mcode.setText(if(data.user_id != null){data.user_id}else{""})
        //edt_fcode.setText(data.friend_id)
        edt_email.setText(data.email ?: "")
        edt_password.setText(if(data.password != null){
            Base64.decode(data.password.toByteArray(),Base64.DEFAULT).toString() }else{""})
        edt_unique.setText(mixUnique)
        //edt_work.setText(if(data.job != null){data.job!!}else{""})
        //edt_number_id.setText(if(data.iden_id != null){data.iden_id!!}else{""})
        fname = if(data.first_name != null){data.first_surname!!}else{""}
        lname = data.first_surname ?: ""
        birth = if(data.birth != null){formatSend.format(date)}else{""}
        provinID = data.province ?: ""
        jobID = data.job ?: ""
        statusID = data.status_id ?: ""
        nationID = data.national_id ?: ""
        if(data.iden_id != null){
            txt_number_id.visibility = View.GONE
            edt_number_id.visibility = View.GONE
        }else{
            txt_number_id.visibility = View.VISIBLE
            edt_number_id.visibility = View.VISIBLE
        }



        val editor = prefer.edit()
        var status=""
        when(data.status_id){
            "1" -> { status = getString(R.string.positive) }
            "2" -> { status = getString(R.string.negative)}
            "3" -> { status = getString(R.string.idontknow)}
            else -> ""
        }
        if(data.status_id != null) {
            editor.putInt(KEYPREFER.HIVSTAT, data.status_id!!.toInt())
            editor.apply()
        }
        txt_hiv_status.text = status

        if(!checkDataUser(data)){
            d{"return User No update Data"}
            editor.putBoolean(KEYPREFER.isUpdateProfile,false)
            editor.apply()
        }else{
            d{"return User Updated Data"}
            editor.putBoolean(KEYPREFER.isUpdateProfile,true)
            editor.apply()
        }


    }


    fun checkDataUser(data:Model.User) : Boolean{
        //if(data.name == null){ return false }
        if(data.first_name == null){ return false }
        if(data.first_surname == null){ return false}
        //if(data.friend_id == null){ return false}
        if(data.phone == null){ return false}
        if(data.email == null){ return false}
        if(data.password == null){ return false}
        if(data.province == null){ return false}
        if(data.job == null){ return false}
        //if(data.iden_id == null){ return false}
        if(data.birth == null){ return false}
        return true
    }


    fun validate() : Boolean{
        val emailPattern = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])"
        val alter = getString(R.string.alteraccupdate)
        /*
        if(TextUtils.isEmpty(edt_phone.editableText.toString())){
            //edt_phone.error = alter
            return false
        }
        if(TextUtils.isEmpty(edt_mcode.editableText.toString())){
            //edt_mcode.error = alter
            return false
        }
        */
        if(TextUtils.isEmpty(edt_email.editableText.toString())){
            //edt_email.error = alter
            return false
        }
        if(TextUtils.isEmpty(edt_password.editableText.toString())){
            //edt_password.error = alter
            return false
        }
        if(TextUtils.isEmpty(edt_unique.editableText.toString())){
            //edt_unique.error = alter
            return false
        }
        if(TextUtils.isEmpty(edt_province.editableText.toString())){
            //edt_province.error = alter
            return false
        }
        if(TextUtils.isEmpty(edt_work.editableText.toString())){
            //edt_work.error = alter
            return false
        }
        if(TextUtils.isEmpty(edt_nation.editableText.toString())){
            return false
        }
        /*
        if(TextUtils.isEmpty(edt_number_id.editableText.toString())){
            edt_number_id.error = alter
            return false
        }
        */

        return true
    }



    fun updateUser(){
        btn_save.isClickable = false
        val data = dataUser[0]
        //update edt_fcode.editableText.toString()

        var user = Model.User(data.user_id,data.gender_id,data.name, fname,
                lname,statusID,null,data.phone,
                edt_email.editableText.toString(),edt_password.editableText.toString(),provinID,jobID,
                if(edt_number_id.text.toString().isNotEmpty()){edt_number_id.text.toString()}else{null}, birth,data.point,"","","",nationID)


        if (prefer.getString(KEYPREFER.UserId, "") != "") {
            d { " user_id[" + prefer.getString(KEYPREFER.UserId, "") + "]" }
            manageSub(
                    mService.UpdateUser(data.user_id,data.gender_id,null, fname,
                            lname,statusID,null,data.phone,
                            Base64.encodeToString(edt_email.editableText.toString().toByteArray(), Base64.DEFAULT),
                            Base64.encodeToString(edt_password.editableText.toString().toByteArray(),Base64.DEFAULT)
                            ,provinID,jobID,
                            if(edt_number_id.text.toString().isNotEmpty()){edt_number_id.text.toString()}else{null}, birth,data.point, nationID)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({ c -> run {
                                btn_save.isClickable = true
                                when(c.header.code){
                                    "200" -> {
                                        d { "update successful" }
                                        prefer = PreferenceManager.getDefaultSharedPreferences(this@AccountSettingActivity)
                                        val editor = prefer.edit()
                                        editor.putInt(KEYPREFER.HIVSTAT,statusID.toInt())
                                        if(!checkDataUser(user)){
                                            d{"user can't update full information"}
                                            editor.putBoolean(KEYPREFER.isUpdateProfile,false)
                                            editor.apply()
                                        }else{
                                            d{"user update full information"}
                                            editor.putBoolean(KEYPREFER.isUpdateProfile,true)
                                            editor.apply()
                                        }
                                        finish()
                                    }
                                }
                            }},{
                                d { it.message!! }
                            })
            )
            /*
            val update = service.updateUser(data.user_id,data.gender_id,"", fname,
                    lname,statusID,"0",edt_phone.editableText.toString(),
                    edt_email.editableText.toString(),edt_password.editableText.toString(),provinID,jobID,
                    data.iden_id, birth,data.point, data.national_id)
            update.enqueue(object : Callback<Void> {
                override fun onFailure(call: Call<Void>?, t: Throwable?) {
                    d { t?.message.toString() }
                    btn_save.isClickable = true
                }

                override fun onResponse(call: Call<Void>?, response: Response<Void>?) {
                    if (response!!.isSuccessful) {
                        d { "update successful" }
                        prefer = PreferenceManager.getDefaultSharedPreferences(this@AccountSettingActivity)
                        val editor = prefer.edit()
                        editor.putInt(KEYPREFER.HIVSTAT,statusID.toInt())
                        if(!checkDataUser(user)){
                            d{"user can't update full information"}
                            editor.putBoolean(KEYPREFER.isUpdateProfile,false)
                            editor.apply()
                        }else{
                            d{"user update full information"}
                            editor.putBoolean(KEYPREFER.isUpdateProfile,true)
                            editor.apply()
                        }
                        finish()
                    }
                    btn_save.isClickable = true
                }
            })
            */
        }



    }


    fun setPasswordListener(){
        edt_password.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(!validatePassword(s)){
                    edt_password.error = Html.fromHtml("<font color='red'>กรุณากรอกรหัสผ่านให้ถูกต้อง</font>")

                }
            }
        })
    }

    fun validatePassword(s: CharSequence?) : Boolean{
        val passwordPattern = Pattern.compile( "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#\$]).{6,12})")
        return passwordPattern.matcher(s).matches()
    }

    fun showPopup(){
        Alerter.create(this@AccountSettingActivity)
                .setBackgroundColorInt(ContextCompat.getColor(this,R.color.red))
                .setIcon(R.drawable.ic_clear_white_48dp)
                .setText(getString(R.string.fillinformation))
                .setTextTypeface(utils.medium)
                .show()
    }

   override fun onPause() {
        super.onPause()
        unsubscribe()
    }

}