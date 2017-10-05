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

    var dataUser = ObservableArrayList<Model.User>()
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
            val user = dataUser[0]
            //val call = service.updateHivStatus(data.status_id,userid)
            val cal = service.updateUser(user.user_id,user.gender_id,user.name,user.first_name,user.first_surname,data.status_id,user.friend_id,
                    user.phone,user.email,user.password,user.province,user.job,user.iden_id,user.birth,user.point,user.national_id)
            cal.enqueue(object :Callback<Void>{
                override fun onFailure(call: Call<Void>?, t: Throwable?) {
                    d{"Throwable ["+t?.message+"]"}
                }
                override fun onResponse(call: Call<Void>?, response: Response<Void>?) {
                    if(response!!.isSuccessful){
                        //change state user
                        val editor = preferences.edit()
                        editor.putBoolean(KEYPREFER.isFirst, false)
                        editor.putInt(KEYPREFER.HIVSTAT,data.status_id.toInt())
                        editor.apply()
                        d{"Response [Success]"}
                        d{ "update status HIV user and Change isFirst ="+preferences.getBoolean(KEYPREFER.isFirst,true) }
                        ActivityMain.vp_main.setCurrentItem(0,false)
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
        loadUser()
        loadStatus()
        Glide.with(this)
                .load(R.drawable.bg_blue)
                .into(bg_root)
        Glide.with(this)
                .load(R.drawable.image_cycle)
                .into(circle_icon)

    }


    fun loadUser(){
        val preferences = PreferenceManager.getDefaultSharedPreferences(this@HivStatusActivity)
        val userid = preferences.getString(KEYPREFER.UserId,"")
        manageSub(
                service.getUser(userid)
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
                            d { "check response [" + dataUser.size + "]" }
                        }},{
                            d { it.message!! }
                        })

        )
    }

    override fun onPause() {
        super.onPause()
        unsubscribe()
    }
}
