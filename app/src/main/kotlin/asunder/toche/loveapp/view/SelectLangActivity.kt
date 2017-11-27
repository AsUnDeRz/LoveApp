package asunder.toche.loveapp

import android.content.Intent
import android.databinding.ObservableArrayList
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import asunder.toche.loveapp.databinding.LanguageItemBinding
import com.akexorcist.localizationactivity.LocalizationActivity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.select_lang.*
import utils.localization.LocalDelegate
import utils.localization.OnLocaleChange


/**
 * Created by ToCHe on 10/7/2017 AD.
 */
class SelectLangActivity : LocalizationActivity(), OnLocaleChange {

    var langList = ObservableArrayList<Model.Language>()
    lateinit var localizationDelegate: LocalDelegate
    var lang =""
    lateinit var utils: Utils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.select_lang)
        localizationDelegate = LocalDelegate(this@SelectLangActivity)
        localizationDelegate.addOnLocaleChengedListener(this)
        localizationDelegate.onCreate(savedInstanceState)
        utils = Utils(this@SelectLangActivity)

        langList.apply {
            add(Model.Language(11,utils.txtLocale("ไทย","ไทย"),"th"))
            add(Model.Language(12,utils.txtLocale("English","English"),"en"))

        }

        Glide.with(this@SelectLangActivity)
                .load(R.drawable.bg_blue)
                .into(bg_root)
        Glide.with(this)
                .load(R.drawable.image_cycle)
                .into(circle_icon)


        rv_lang.layoutManager = LinearLayoutManager(this)
        rv_lang.setHasFixedSize(true)
        rv_lang.adapter = LangAdapter(langList,false)

    }


    fun LangAdapter(item: List<Any>, stableIds: Boolean): LastAdapter {
        return LastAdapter(item,BR.langItem,stableIds).type{ item, position ->
            when(item){
                is Model.Language -> LangType
                else -> null
            }
        }
    }

    private val LangType = Type<LanguageItemBinding>(R.layout.language_item)
            .onClick {
                setLanguage(it.binding.langItem.key)
                LocalUtil.onAttach(applicationContext, it.binding.langItem.key)
                startActivity(Intent().setClass(this@SelectLangActivity,OldNewUserActivity::class.java))
                finish()

                //update gender with user id
                //call api
            }
            .onLongClick {}

}