package asunder.toche.loveapp

import android.databinding.ObservableArrayList
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import asunder.toche.loveapp.R
import com.bumptech.glide.Glide
import com.github.ajalt.timberkt.Timber
import com.github.ajalt.timberkt.Timber.d
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.header_logo_white_back.*
import kotlinx.android.synthetic.main.hiv_status.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * Created by admin on 8/2/2017 AD.
 */
class HivStatusActivity: AppCompatActivity(){



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

    val statusList = ObservableArrayList<Model.HivStatus>()
    val utils = Utils(this@HivStatusActivity)

    fun loadStatus(){
        manageSub(
                service.getHivStatus()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ c -> run {
                            statusList.apply {
                                c.forEach {
                                    item -> add(item)
                                    Timber.d { "Add [" + item.status_eng + "] to arraylist" }
                                }
                            }
                            setTxt(statusList)
                            Timber.d { "check response [" + c.size + "]" }
                        }},{
                            Timber.d { it.message!! }
                        })
        )
    }



    fun setTxt(data:ObservableArrayList<Model.HivStatus>){
        txt_positive.text = utils.txtLocale(data[0].status_th,data[0].status_eng)
        txt_negative.text = utils.txtLocale(data[1].status_th,data[1].status_eng)
        txt_dontknow.text =utils.txtLocale(data[2].status_th,data[2].status_eng)

        // update user hiv status
        txt_positive.setOnClickListener {
            updateStatusUser(data[0])
        }
        txt_negative.setOnClickListener {
            updateStatusUser(data[1])
        }
        txt_dontknow.setOnClickListener {
            updateStatusUser(data[2])
        }
    }

    fun updateStatusUser(data:Model.HivStatus){

        val preferences = PreferenceManager.getDefaultSharedPreferences(this@HivStatusActivity)
        if(preferences.getString(KEYPREFER.UserId,"") != ""){
            //update status by user id
            val call = service.updateHivStatus(data.status_id,preferences.getString(KEYPREFER.UserId,""))
            call.enqueue(object :Callback<Void>{
                override fun onFailure(call: Call<Void>?, t: Throwable?) {
                    d{"Throwable ["+t?.message+"]"}
                }
                override fun onResponse(call: Call<Void>?, response: Response<Void>?) {
                    if(response!!.isSuccessful){
                        //change state user
                        val editor = preferences.edit()
                        editor.putBoolean(KEYPREFER.isFirst, false)
                        editor.apply()
                        d{"Response [Success]"}
                        d{ "update status HIV user and Change isFirst ="+preferences.getBoolean(KEYPREFER.isFirst,true) }
                        ActivityMain.vp_main.setCurrentItem(2,false)
                        finish()
                    }
                }

            })

        }

    }

    override fun onBackPressed() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.hiv_status)
        loadStatus()
        Glide.with(this)
                .load(R.drawable.bg_blue)
                .into(bg_root)
        Glide.with(this)
                .load(R.drawable.image_cycle)
                .into(circle_icon)

    }

    override fun onPause() {
        super.onPause()
        unsubscribe()
    }
}
