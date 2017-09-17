package asunder.toche.loveapp

import android.content.Intent
import android.databinding.ObservableArrayList
import android.graphics.Color
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import asunder.toche.loveapp.R
import com.bumptech.glide.Glide
import com.github.ajalt.timberkt.Timber.d
import com.tapadoo.alerter.Alerter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.header_logo_blue_back.*
import kotlinx.android.synthetic.main.login.*


/**
 * Created by admin on 7/31/2017 AD.
 */
class LoginActivity :AppCompatActivity(){


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

    fun checkLogin(email:String,password:String){
        manageSub(
                service.login(email,password)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ c -> run {
                            if(c.size != 0 &&c[0].user_id != ""){
                                val preferences = PreferenceManager.getDefaultSharedPreferences(this@LoginActivity)
                                val editor = preferences.edit()
                                editor.putString(KEYPREFER.UserId, c[0].user_id)
                                editor.putBoolean(KEYPREFER.isFirst, false)
                                editor.putInt(KEYPREFER.HIVSTAT,c[0].status_id.toInt())
                                editor.putString(KEYPREFER.GENDER,c[0].gender_id)
                                editor.apply()
                                d{ "check userid in preference ="+preferences.getString(KEYPREFER.UserId,"")}

                                startActivity(Intent().setClass(this@LoginActivity,ActivityMain::class.java))
                                finish()
                            }else{
                                d{"login fail"}
                                showAlerter()
                            }

                        }},{
                            d { it.message!! }
                        })

        )

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        Glide.with(this)
                .load(R.drawable.image_cycle)
                .into(imageView5)
        edit_email.typeface = MyApp.typeFace.heavy
        edit_password.typeface = MyApp.typeFace.heavy
        txt_title.typeface = MyApp.typeFace.heavy
        txt_email.typeface = MyApp.typeFace.heavy
        txt_login.typeface = MyApp.typeFace.heavy
        txt_password.typeface = MyApp.typeFace.heavy
        forgetpassword.typeface = MyApp.typeFace.heavy


        PushDownAnim.setOnTouchPushDownAnim(login_btn)
        login_btn.setOnClickListener {
            //check login
            if(edit_email.text.toString() != "" && edit_password.text.toString() != ""){
                checkLogin(edit_email.text.toString(),edit_password.text.toString())
            }
        }
        btn_back.setOnClickListener {
            onBackPressed()
        }
    }

     override fun onBackPressed() {
        super.onBackPressed()
         startActivity(Intent().setClass(this@LoginActivity, OldNewUserActivity::class.java))
         finish()

    }


    override fun onPause() {
        super.onPause()
        unsubscribe()
    }

    fun showAlerter(){
        d{"show Alerter"}
                Alerter.create(this)
                        .setText("Email or Password incorrect")
                        .setTitleTypeface(Utils(this).heavy)
                        .setTextTypeface(Utils(this).heavy)
                        //.setBackgroundColorRes(Color.RED) // or setBackgroundColorInt(Color.CYAN)
                        .setBackgroundColorInt(Color.RED)
                        .show()

    }
}