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
import android.widget.Toast
import com.afollestad.materialdialogs.MaterialDialog



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

    val provinces = ObservableArrayList<Model.Province>()
    val provinTitle = ObservableArrayList<String>()
    lateinit var utils :Utils

    fun loadProvince(){
        manageSub(
                service.getProvinces()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ c -> run {
                            provinces.apply {
                                c.forEach {
                                    item -> run {
                                    add(item)
                                    provinTitle.add(utils.txtLocale(item.province_th,item.province_eng))
                                }
                                    //d { "Add ["+item.province_th+"] to arraylist" }
                                }
                            }
                            d { "check provincTitle [" + provinTitle.size + "]" }
                            d { "check response [" + c.size + "]" }
                        }},{
                            d { it.message!! }
                        })
        )
    }


    companion object {
        fun setUnique(data:String){
            edtUniId.setText(data)
            d{ data }
        }

        @SuppressLint("StaticFieldLeak")
        lateinit var edtUniId :EditText
    }
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.account_setting)
        utils = Utils(this)
        Glide.with(this)
                .load(R.drawable.bg_white)
                .into(bg_root)
        txt_title.text = "SETTING"
        edt_name.typeface = MyApp.typeFace.heavy
        edt_phone.typeface = MyApp.typeFace.heavy
        edt_mcode.typeface = MyApp.typeFace.heavy
        edt_fcode.typeface = MyApp.typeFace.heavy
        edt_email.typeface = MyApp.typeFace.heavy
        edt_password.typeface = MyApp.typeFace.heavy
        edt_unique.typeface = MyApp.typeFace.heavy
        edt_province.typeface = MyApp.typeFace.heavy
        edtUniId = findViewById(R.id.edt_unique)
        loadProvince()


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


    }
    fun showProvince(){
        MaterialDialog.Builder(this)
                .title("Province")
                .items(provinTitle)
                .itemsCallback({ dialog, view, which, text ->  edt_province.setText(text) })
                .positiveText(android.R.string.cancel)
                .show()
    }


   override fun onPause() {
        super.onPause()
        unsubscribe()
    }

}