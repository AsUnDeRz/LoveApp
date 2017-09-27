package asunder.toche.loveapp

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import com.akexorcist.localizationactivity.LocalizationActivity
import com.github.ajalt.timberkt.Timber.d
import kotlinx.android.synthetic.main.language.*
import utils.localization.LocalDelegate
import utils.localization.OnLocaleChange


/**
 * Created by ToCHe on 9/22/2017 AD.
 */
class LanguageActivity : LocalizationActivity(), OnLocaleChange {


    lateinit var localizationDelegate: LocalDelegate
    var lang =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.language)
        localizationDelegate = LocalDelegate(this@LanguageActivity)
        localizationDelegate.addOnLocaleChengedListener(this)
        localizationDelegate.onCreate(savedInstanceState)
        val preferences = PreferenceManager.getDefaultSharedPreferences(this@LanguageActivity)
        var locale :String = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            resources?.configuration?.locales?.get(0)?.language!!
        } else {
            resources?.configuration?.locale?.language!!
        }
        if(preferences.getString(KEYPREFER.language, locale) == "th"){
            sb_ios.isChecked = true
            txt_lang.text = "ภาษาไทย"
            lang = "th"
        }else{
            lang = "en"
            sb_ios.isChecked = false
            txt_lang.text = "English"
        }

        sb_ios.setOnCheckedChangeListener { compoundButton, b ->
            if(b){
                txt_lang.text = "ภาษาไทย"
                d{"Swicth button is $b"}
                lang = "th"
            }else{
                txt_lang.text = "English"
                lang = "en"
                d{"Swicth button is $b"}
            }
        }
    }


    override fun onBackPressed() {
        super.onBackPressed()
        if(lang != "") {
            LocalUtil.onAttach(applicationContext, lang)
        }
        startActivity(Intent().setClass(this@LanguageActivity,ActivityMain::class.java))
        finish()

    }


}