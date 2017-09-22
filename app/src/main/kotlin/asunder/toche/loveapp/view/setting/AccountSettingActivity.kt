package asunder.toche.loveapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.databinding.ObservableArrayList
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import asunder.toche.loveapp.R
import com.bumptech.glide.Glide
import com.github.ajalt.timberkt.Timber.d
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.account_setting.*
import kotlinx.android.synthetic.main.header_logo_blue_back.*
import android.R.array
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.widget.Toast
import com.afollestad.materialdialogs.MaterialDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat


/**
 * Created by admin on 8/4/2017 AD.
 */
class AccountSettingActivity: AppCompatActivity() {

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

    var provinces = ObservableArrayList<Model.Province>()
    var provinID=""
    val provinTitle = ObservableArrayList<String>()
    var dataUser = ObservableArrayList<Model.User>()
    lateinit var prefer :SharedPreferences
    lateinit var utils :Utils
    lateinit var appdb : AppDatabase
    lateinit var preferences : SharedPreferences

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
        edt_phone.typeface = MyApp.typeFace.heavy
        edt_mcode.typeface = MyApp.typeFace.heavy
        //edt_fcode.typeface = MyApp.typeFace.heavy
        edt_email.typeface = MyApp.typeFace.heavy
        edt_password.typeface = MyApp.typeFace.heavy
        edt_unique.typeface = MyApp.typeFace.heavy
        edt_province.typeface = MyApp.typeFace.heavy
        edt_work.typeface = MyApp.typeFace.heavy
        edt_number_id.typeface = MyApp.typeFace.heavy
        edtUniId = findViewById(R.id.edt_unique)
        loadUser(prefer.getString(KEYPREFER.UserId,"0"))




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
        edt_province.setOnFocusChangeListener { view, b ->
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
            if(b)
            showProvince()
        }

        btn_save.setOnClickListener {
            updateUser()
        }


    }
    fun showProvince(){
        MaterialDialog.Builder(this)
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

        edt_phone.setText(if(data.phone != null){data.phone}else{""})
        edt_mcode.setText(if(data.user_id != null){data.user_id}else{""})
        //edt_fcode.setText(data.friend_id)
        edt_email.setText(if(data.email != null){data.email}else{""})
        edt_password.setText(if(data.password != null){data.password}else{""})
        edt_unique.setText(mixUnique)
        edt_work.setText(if(data.job != null){data.job}else{""})
        edt_number_id.setText(if(data.iden_id != null){data.iden_id}else{""})
        fname = if(data.first_name != null){data.first_surname}else{""}
        lname = if(data.first_surname != null){data.first_surname}else{""}
        birth = if(data.birth != null){formatSend.format(date)}else{""}
        provinID = if(data.province != null){data.province}else{""}


        var status=""
        when(data.status_id){
            "1" -> { status = getString(R.string.positive)}
            "2" -> { status = getString(R.string.negative)}
            "3" -> { status = getString(R.string.idontknow)}
            else -> ""
        }
        txt_hiv_status.text = status

        preferences = PreferenceManager.getDefaultSharedPreferences(this@AccountSettingActivity)
        val editor = preferences.edit()
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
        if(data.name == null){ return false }
        if(data.first_name == null){ return false }
        if(data.first_surname == null){ return false}
        //if(data.friend_id == null){ return false}
        if(data.phone == null){ return false}
        if(data.email == null){ return false}
        if(data.password == null){ return false}
        if(data.province == null){ return false}
        if(data.job == null){ return false}
        if(data.iden_id == null){ return false}
        if(data.birth == null){ return false}
        return true
    }




    fun updateUser(){
        btn_save.isClickable = false
        val data = dataUser[0]
        //update edt_fcode.editableText.toString()
        var user = Model.User(data.user_id,data.gender_id,data.name, fname,
                lname,data.status_id,"0",edt_phone.editableText.toString(),
                edt_email.editableText.toString(),edt_password.editableText.toString(),provinID,edt_work.editableText.toString(),
                edt_number_id.editableText.toString(), birth,data.point,"","","")

        if (prefer.getString(KEYPREFER.UserId, "") != "") {
            val userID = prefer.getString(KEYPREFER.UserId,"")
            d { " user_id[" + prefer.getString(KEYPREFER.UserId, "") + "]" }
            val update = service.updateUser(data.user_id,data.gender_id,data.name, fname,
                    lname,data.status_id,"0",edt_phone.editableText.toString(),
                    edt_email.editableText.toString(),edt_password.editableText.toString(),provinID,edt_work.editableText.toString(),
                    edt_number_id.editableText.toString(), birth,data.point)
            update.enqueue(object : Callback<Void> {
                override fun onFailure(call: Call<Void>?, t: Throwable?) {
                    d { t?.message.toString() }
                    btn_save.isClickable = true
                }

                override fun onResponse(call: Call<Void>?, response: Response<Void>?) {
                    if (response!!.isSuccessful) {
                        d { "update successful" }
                        preferences = PreferenceManager.getDefaultSharedPreferences(this@AccountSettingActivity)
                        val editor = preferences.edit()
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
        }


    }


   override fun onPause() {
        super.onPause()
        unsubscribe()
    }

}