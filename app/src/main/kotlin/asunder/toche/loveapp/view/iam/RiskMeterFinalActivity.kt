package asunder.toche.loveapp

import android.databinding.ObservableArrayList
import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.view.animation.RotateAnimation
import com.bumptech.glide.Glide
import com.github.ajalt.timberkt.Timber
import com.github.ajalt.timberkt.Timber.d
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.risk_meter_final.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


/**
 * Created by admin on 8/9/2017 AD.
 */
class RiskMeterFinalActivity:AppCompatActivity(){

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


    fun loadRiskResult(code:String){
        manageSub(
                service.getRiskResults(code)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ c -> run {
                            loadAnimation(c[0])
                            addRiskTestAndUpdatePoint(code)
                            d { "check response [" + c.size + "]" }
                        }},{
                            Timber.d { it.message!! }
                        })
        )
    }

    fun addRiskTestAndUpdatePoint(riskCode:String){
        val preferences = PreferenceManager.getDefaultSharedPreferences(this@RiskMeterFinalActivity)
        if(preferences.getString(KEYPREFER.UserId,"") != "") {
            val addTest = service.addRiskTest(preferences.getString(KEYPREFER.UserId,""),riskCode, Date())
            val addPoint = service.addUserPoint(preferences.getString(KEYPREFER.UserId,""),"20")
            addTest.enqueue(object : Callback<Void>{
                override fun onFailure(call: Call<Void>?, t: Throwable?) {
                    d{ t?.message.toString() }
                }
                override fun onResponse(call: Call<Void>?, response: Response<Void>?) {
                    if(response!!.isSuccessful){
                        d{"addRiskTest successful"}
                        addPoint.enqueue(object :Callback<Void>{
                            override fun onFailure(call: Call<Void>?, t: Throwable?) {
                                d{ t?.message.toString() }
                            }
                            override fun onResponse(call: Call<Void>?, response: Response<Void>?) {
                                if(response!!.isSuccessful){
                                    d{"addPoint Successful"}
                                    inputPointHistory(riskCode)
                                }
                            }
                        })

                    }
                }
            })

        }
    }
    fun inputPointHistory(code:String){
        d{"input point history"}
        val preferences = PreferenceManager.getDefaultSharedPreferences(this@RiskMeterFinalActivity)
        if (preferences.getString(KEYPREFER.UserId, "") != "") {
            d{"code [$code] user_id["+preferences.getString(KEYPREFER.UserId,"")+"]"}
            val addPointHistory = service.addPointHistory(code,preferences.getString(KEYPREFER.UserId,""),
                    "3","20",Date())
            addPointHistory.enqueue(object : Callback<Void> {
                override fun onFailure(call: Call<Void>?, t: Throwable?) {
                    d { t?.message.toString() }
                }

                override fun onResponse(call: Call<Void>?, response: Response<Void>?) {
                    if (response!!.isSuccessful) {
                        d { "addPointHistory successful" }
                    }
                }
            })
        }
    }


    var service : LoveAppService = LoveAppService.create()
    var riskResult = ObservableArrayList<Model.RiskResult>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.risk_meter_final)
        loadRiskResult(intent.extras.getString("answer"))
        loadImage()

        btn_findtest.setOnClickListener {
            ActivityMain.vp_main.setCurrentItem(1,false)
            finish()
        }

    }

    fun loadAnimation(riskResult:Model.RiskResult){
        val utils = Utils(this@RiskMeterFinalActivity)
        var toDegree = 0f
        when(riskResult.status){
            "MEDIUM" -> toDegree = -20f
            "HIGH" -> toDegree = 30f
            "LOW" -> toDegree = -50f
        }


        val rotateAnim = RotateAnimation(0f,toDegree,
                RotateAnimation.RELATIVE_TO_SELF,0.5f,
                RotateAnimation.RELATIVE_TO_SELF,1f)

        rotateAnim.duration = 3000
        rotateAnim.fillAfter = true
        val splash = Handler()
        splash.postDelayed({
            meter_pin.startAnimation(rotateAnim)

        },500)

        //set txt
        txt_risk_status.text = utils.txtLocale(riskResult.topic_th,riskResult.topic_eng)
        txt_risk_detail.text = utils.txtLocale(utils.clearString(riskResult.details_th),utils.clearString(riskResult.details_eng))

    }

    fun loadImage(){
        Glide.with(this)
                .load(R.drawable.bg_blue)
                .into(bg_root)
        Glide.with(this)
                .load(R.drawable.meter_circle)
                .into(meter_bar)
        Glide.with(this)
                .load(R.drawable.meter_pin)
                .into(meter_pin)
    }


    override fun onPause() {
        super.onPause()
        unsubscribe()
    }
}