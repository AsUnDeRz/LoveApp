package asunder.toche.loveapp

import android.content.Intent
import android.databinding.ObservableArrayList
import android.databinding.ObservableList
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.widget.TextView
import android.widget.Toast
import asunder.toche.loveapp.R
import asunder.toche.loveapp.databinding.GenderItemBinding
import com.bumptech.glide.Glide
import com.github.ajalt.timberkt.Timber.d
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.gender_list.*
import kotlinx.android.synthetic.main.header_logo_white_back.*
import com.afollestad.materialdialogs.MaterialDialog




/**
 * Created by admin on 7/30/2017 AD.
 */
class GenderActivity : AppCompatActivity(){


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
    val utils = Utils(this@GenderActivity)

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
                                editor.apply()
                                d{ "check userid in preference ="+preferences.getString(KEYPREFER.UserId,"")}
                            }
                            startActivity(Intent().setClass(this@GenderActivity,ActivityMain::class.java))
                            finish()
                            d { "check response [" + c.size + "]" }
                        }},{
                            d { it.message!! }
                        })
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.gender_list)
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

    private val GenderType = Type<GenderItemBinding>(R.layout.gender_item)
            .onCreate { println("Created ${it.binding.genderItem} at #${it.adapterPosition}") }
            .onBind { println("Bound ${it.binding.genderItem} at #${it.adapterPosition}") }
            .onRecycle { println("Recycled ${it.binding.genderItem} at #${it.adapterPosition}") }
            .onClick {
                //update gender with user id
                //call api
                getUserID(it.binding.genderItem.id.toString())
            }
            .onLongClick {}


    override fun onPause() {
        super.onPause()
        unsubscribe()
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