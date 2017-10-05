package asunder.toche.loveapp

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.ObservableArrayList
import android.databinding.ObservableList
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.widget.TextView
import android.widget.Toast
import asunder.toche.loveapp.R
import asunder.toche.loveapp.databinding.GenderItemBinding
import asunder.toche.loveapp.databinding.NationItemBinding
import com.bumptech.glide.Glide
import com.github.ajalt.timberkt.Timber.d
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.gender_list.*
import kotlinx.android.synthetic.main.header_logo_white_back.*
import com.afollestad.materialdialogs.MaterialDialog
import view.custom_view.TextViewHeavy
import view.custom_view.TextViewMedium


/**
 * Created by admin on 7/30/2017 AD.
 */
class GenderActivity : AppCompatActivity(),ViewModel.MainViewModel.RiskQInterface{



    override fun endCallProgress(any: Any) {
        val resultList = any as ObservableArrayList<*>

        when (resultList[0]) {
            is Model.RepositoryKnowledge -> {
                appDb.addKnowledgeContent(resultList as ObservableArrayList<Model.RepositoryKnowledge>)
                loadSuccess++
                d{"Load Knowledge Success"}
            }
            is Model.KnowledgeGroup -> {
                appDb.addKnowledgeGroup(resultList as ObservableArrayList<Model.KnowledgeGroup>)
                loadSuccess++
                d{"Load KnowledgeGroup Success"}
            }
        }

        if(loadSuccess == 2) {
            //startActivity(Intent().setClass(this@GenderActivity, ActivityMain::class.java))
            //finish()
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

    val genderList = ObservableArrayList<Model.Gender>()
    lateinit var utils :Utils
    lateinit var appDb : AppDatabase
    lateinit var MainViewModel : ViewModel.MainViewModel
    var loadSuccess =0


    fun loadGender(){
        manageSub(
                service.getGenders()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ c -> run {
                            genderList.apply {
                                c.forEach {
                                    item -> add(Model.Gender(item.gender_id,utils.txtLocale(item.gender_type_th,item.gender_type_eng)))
                                    d { "Add ["+item.gender_type_eng+"] to arraylist" }
                                }
                            }
                            rv_gender.adapter = GenderAdapter(genderList,false)
                            d { "check response [" + c.size + "]" }
                        }},{
                            d { it.message!! }
                        })
        )
    }

    fun getUserID(genderID:String){
        d{"Check GenderId [$genderID]"}
        manageSub(
                service.genUserID(genderID)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ c -> run {
                            if(c.size != 0 &&c[0].user_id != ""){
                                val preferences = PreferenceManager.getDefaultSharedPreferences(this@GenderActivity)
                                val editor = preferences.edit()
                                editor.putString(KEYPREFER.UserId, c[0].user_id)
                                editor.putString(KEYPREFER.GENDER, genderID)
                                editor.apply()
                                d{ "check userid in preference ="+preferences.getString(KEYPREFER.UserId,"")}
                                MainViewModel.loadKnowledage(this,c[0].user_id)
                                MainViewModel.loadKnowledgeGroup(genderID,this,Utils(this@GenderActivity))
                            }

                            d { "check response [" + c.size + "]" }
                        }},{
                            d { it.message!! }
                        })
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.gender_list)
        appDb = AppDatabase(this@GenderActivity)
        utils = Utils(this@GenderActivity)
        MainViewModel = ViewModelProviders.of(this).get(ViewModel.MainViewModel::class.java)
        loadGender()
        Glide.with(this)
                .load(R.drawable.bg_blue)
                .into(bg_root)
        Glide.with(this)
                .load(R.drawable.image_cycle)
                .into(circle_icon)


        rv_gender.layoutManager = LinearLayoutManager(this) 
        rv_gender.setHasFixedSize(true)

    }

    fun GenderAdapter(item: List<Any>, stableIds: Boolean): LastAdapter {
        return LastAdapter(item,BR.genderItem,stableIds).type{ item, position ->
            when(item){
                is Model.Gender -> GenderType
                else -> null
            }
        }
    }
    fun NationAdapter(item: List<Any>, stableIds: Boolean): LastAdapter {
        return LastAdapter(item,BR.nationItem,stableIds).type{ item, position ->
            when(item){
                is Model.National -> NationType
                else -> null
            }
        }
    }


    private val GenderType = Type<GenderItemBinding>(R.layout.gender_item)
            .onCreate { println("Created ${it.binding.genderItem} at #${it.adapterPosition}") }
            .onBind { println("Bound ${it.binding.genderItem} at #${it.adapterPosition}") }
            .onRecycle { println("Recycled ${it.binding.genderItem} at #${it.adapterPosition}") }
            .onClick {
                //update gender with user id
                //call api
                getUserID(it.binding.genderItem.id.toString())
                swicthState()
            }
            .onLongClick {}

    private val NationType = Type<NationItemBinding>(R.layout.nation_item)
            .onClick {
                d{it.binding.nationItem.toString()}
                val preferences = PreferenceManager.getDefaultSharedPreferences(this@GenderActivity)
                val editor = preferences.edit()
                editor.putString(KEYPREFER.NATIONAL, it.binding.nationItem.id.toString())
                editor.apply()
                val data = Intent()
                if(it.binding.nationItem.title.equals("thai",true) ||
                        it.binding.nationItem.title.equals("ไทย",true)) {
                    data.putExtra("isThai", true)
                    d{"isThai"}
                }
                startActivity(data.setClass(this@GenderActivity,UniqueIdActivity::class.java))
                finish()
                //update nation with user id
                //getUserID(it.binding.nationItem.id.toString())
            }


    override fun onPause() {
        super.onPause()
        unsubscribe()
    }

    fun swicthState(){
        vf.displayedChild = 1
        val title = findViewById<TextViewHeavy>(R.id.title_whoru)
        title.setTextColor(ContextCompat.getColor(this,R.color.colorPrimary))
        title.text = getString(R.string.nationtitle)
        Glide.with(this)
                .load(R.drawable.bg_white)
                .into(bg_root)

        val nationList = ObservableArrayList<Model.National>().apply {
            appDb.getNations().forEach {
                add(Model.National(it.national_id.toLong(), utils.txtLocale(it.nationality_th, it.nationality_eng)))

            }
        }
        rv_gender.adapter = NationAdapter(nationList,false)



    }
    fun showProgress(){
        val dialog =MaterialDialog.Builder(this)
                .title("")
                .content("Please wait...")
                .progress(true, 0)
                .progressIndeterminateStyle(false)
                .show()

    }
}