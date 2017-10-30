package asunder.toche.loveapp

import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.afollestad.materialdialogs.DialogAction
import kotlinx.android.synthetic.main.new_setting.*
import com.afollestad.materialdialogs.MaterialDialog
import com.akexorcist.localizationactivity.LocalizationActivity
import com.github.ajalt.timberkt.Timber.d
import kotlinx.android.synthetic.main.about_loveapp.*
import kotlinx.android.synthetic.main.dialog_support.*
import kotlinx.android.synthetic.main.general.*
import kotlinx.android.synthetic.main.header_logo_blue_back.*
import kotlinx.android.synthetic.main.old_new_user.view.*
import kotlinx.android.synthetic.main.reminders.*
import kotlinx.android.synthetic.main.support.*
import utils.localization.LocalDelegate
import utils.localization.OnLocaleChange


/**
 * Created by ToCHe on 30/10/2017 AD.
 */
class NewSetting: LocalizationActivity(), OnLocaleChange {

    override fun onBackPressed() {
        super.onBackPressed()
        if(lang != "") {
            setLanguage(lang)
            LocalUtil.onAttach(applicationContext, lang)
            startActivity(Intent().setClass(this@NewSetting,ActivityMain::class.java))
        }
        finish()
    }

    lateinit var localizationDelegate: LocalDelegate
    var lang =""
    lateinit var utils:Utils
    lateinit var prefer:SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_setting)
        localizationDelegate = LocalDelegate(this@NewSetting)
        localizationDelegate.addOnLocaleChengedListener(this)
        localizationDelegate.onCreate(savedInstanceState)
        utils = Utils(this@NewSetting)
        prefer = PreferenceManager.getDefaultSharedPreferences(this@NewSetting)

        val state = intent.getStringExtra("data")
        when(state){
            KEYPREFER.GENERAL ->{
                initGeneral()
            }
            KEYPREFER.ABOUT ->{
                initAboutLove()
            }
            KEYPREFER.SUPPORT ->{
                initSupport()
            }
            KEYPREFER.REMINDERS ->{
                initReminders()
            }
        }

        btn_back.setOnClickListener {
            onBackPressed()
        }
    }



    fun initGeneral(){
        general.inflate()
        PushDownAnim.setOnTouchPushDownAnim(btn_changepassword)
        btn_changepassword.setOnClickListener {
            showDialogChangePass()
        }

        //init language
        var locale :String = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            resources?.configuration?.locales?.get(0)?.language!!
        } else {
            resources?.configuration?.locale?.language!!
        }
        if(prefer.getString(KEYPREFER.language, locale) == "th"){
            switch_lang.checkedTogglePosition = 0
            lang ="th"
        }else{
            switch_lang.checkedTogglePosition = 1
            lang="en"
        }
        switch_lang.setOnToggleSwitchChangeListener { position, isChecked ->
            when(position){
                0 -> {
                    lang="th"
                    setThai()
                }
                1 -> {
                    lang="en"
                    setEng()
                }
            }
        }
        //init Passcode
        if(prefer.getBoolean(KEYPREFER.ISCHECKPASSCODE, false)){
            switch_passcode.checkedTogglePosition =0
        }else{
            switch_passcode.checkedTogglePosition =1
        }

        switch_passcode.setOnToggleSwitchChangeListener { position, isChecked ->
            var isCheckPass =false
            when(position){
                0 -> { isCheckPass = true}
                1 -> { isCheckPass = false}
            }
            val editor = prefer.edit()
            editor.putBoolean(KEYPREFER.ISCHECKPASSCODE,isCheckPass)
            editor.apply()
            d{"isCheckPasscode $isCheckPass"}
        }

    }

    fun initReminders(){
        reminders.inflate()
        if(prefer.getInt(KEYPREFER.HIVSTAT,0) != 0){
            val stat = prefer.getInt(KEYPREFER.HIVSTAT,0)
            when(stat){
                1 ->{ btn_cd4.visibility = View.VISIBLE}
                2 ->{ btn_cd4.visibility = View.GONE}
                3 ->{ btn_cd4.visibility = View.GONE}
            }

        }
        PushDownAnim.setOnTouchPushDownAnim(btn_testing)
        btn_testing.setOnClickListener {
            startActivity(Intent().setClass(this@NewSetting,HivTestActivity::class.java))
        }
        PushDownAnim.setOnTouchPushDownAnim(btn_pillreminder)
        btn_pillreminder.setOnClickListener {
            startActivity(Intent().setClass(this@NewSetting,PillReminderTimeActivity::class.java))

        }
        PushDownAnim.setOnTouchPushDownAnim(btn_cd4)
        btn_cd4.setOnClickListener {
            startActivity(Intent().setClass(this@NewSetting,Cd4VLActivity::class.java))
        }

    }

    fun initSupport(){
        support.inflate()
        PushDownAnim.setOnTouchPushDownAnim(btn_feedback)
        btn_feedback.setOnClickListener {
            showDialogSupport()
        }
        PushDownAnim.setOnTouchPushDownAnim(btn_rateus)
        btn_rateus.setOnClickListener {
            startActivity(Intent().setClass(this@NewSetting, FeedbackActivity::class.java))
        }

    }

    fun initAboutLove(){
        aboutapp.inflate()
        version_app.text = BuildConfig.VERSION_NAME
        PushDownAnim.setOnTouchPushDownAnim(btn_termcondition)
        btn_termcondition.setOnClickListener {
            val data = Intent()
            data.putExtra("data","term")
            startActivity(data.setClass(this@NewSetting,ConditionText::class.java))
        }
        PushDownAnim.setOnTouchPushDownAnim(btn_policy)
        btn_policy.setOnClickListener {
            val data = Intent()
            data.putExtra("data","policy")
            startActivity(data.setClass(this@NewSetting,ConditionText::class.java))        }
    }


    fun showDialogChangePass(){
        var pc=""
       val dialog = MaterialDialog.Builder(this)
                .customView(R.layout.dialog_changepass, true)
                .positiveText(R.string.confirm)
                .negativeText(android.R.string.cancel)
                .onPositive(
                        { dialog1, which ->
                            //change passcode
                            val editor = prefer.edit()
                            editor.putString(KEYPREFER.PASSCODE, pc)
                            editor.apply()
                        })
                .build()

        var c1=false
        var c2=false
        var c3=false
        val positiveAction = dialog.getActionButton(DialogAction.POSITIVE)
        val currentPass = dialog.customView?.findViewById<EditText>(R.id.edt_current)
        val newPass = dialog.customView?.findViewById<EditText>(R.id.edt_newpass)
        val newPassAgain = dialog.customView?.findViewById<EditText>(R.id.edt_newpass_again)
        currentPass?.typeface = utils.medium
        newPass?.typeface = utils.medium
        newPassAgain?.typeface = utils.medium

        currentPass?.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                c1 = s.toString().trim().isNotEmpty() && s.toString() == prefer.getString(KEYPREFER.PASSCODE,"")
                d{"C1 $c1"}
                positiveAction.isEnabled = c1 && c2 && c3
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
        newPass?.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                c2 = s.toString().trim().isNotEmpty() &&  s.toString() != currentPass?.text.toString()
                d{"C2 $c2"}
                positiveAction.isEnabled = c1 && c2 && c3
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
        newPassAgain?.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                c3 =  s.toString().trim().isNotEmpty() && s.toString() == newPass?.text.toString()
                d{"C3 $c3"}
                positiveAction.isEnabled = c1 && c2 && c3
                pc = s.toString()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        dialog.show()
        positiveAction.isEnabled = false // disabled by default

    }

    fun showDialogSupport(){
        val dialog = MaterialDialog.Builder(this)
                .customView(R.layout.dialog_support, true)
                .negativeText(android.R.string.cancel)
                .build()

        val btnProblem = dialog.customView?.findViewById<Button>(R.id.btn_problem)
        val btnSugges = dialog.customView?.findViewById<Button>(R.id.btn_sugges)
        val btnOther = dialog.customView?.findViewById<Button>(R.id.btn_others)
        btnProblem?.typeface = utils.medium
        btnSugges?.typeface = utils.medium
        btnOther?.typeface = utils.medium

        btnProblem?.setOnClickListener {

        }
        btnSugges?.setOnClickListener {

        }
        btnOther?.setOnClickListener {

        }


        dialog.show()

    }

    fun setThai(){
        txt_lang.text ="ภาษา"
        header.text ="ความเป็นส่วนตัว"
        txt_noti.text ="การแจ้งเตือน"
        txt_touch.text = "ลายนิ้วมือ"
        txt_pass.text = "ล็อครหัสผ่าน"
        btn_changepassword.text ="เปลี่ยนรหัสผ่าน"

    }

    fun setEng(){
        txt_lang.text ="Language"
        header.text ="Privacy"
        txt_noti.text ="Notifications"
        txt_touch.text = "Touch ID"
        txt_pass.text = "Password lock"
        btn_changepassword.text ="Change Passwoed"


    }



}