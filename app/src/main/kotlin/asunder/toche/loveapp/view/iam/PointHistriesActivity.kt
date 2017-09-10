package asunder.toche.loveapp

import android.content.SharedPreferences
import android.databinding.ObservableArrayList
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import asunder.toche.loveapp.R
import com.github.ajalt.timberkt.Timber
import com.github.ajalt.timberkt.Timber.d
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.header_logo_blue_back.*
import kotlinx.android.synthetic.main.point_histries.*
import java.util.*
import kotlin.collections.ArrayList


/**
 * Created by admin on 8/3/2017 AD.
 */
class PointHistriesActivity: AppCompatActivity() {


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

    val dataList = ObservableArrayList<Model.Point>()
    val dataDate = ArrayList<Long>()
    lateinit var utils :Utils
    lateinit var prefer : SharedPreferences

    val pointList = ObservableArrayList<Model.Point>().apply {
        for(i in 1..5) {
            add(Model.Point(123, "Reading", "300"))
        }
    }
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.point_histries)
        utils = Utils(this@PointHistriesActivity)
        prefer = PreferenceManager.getDefaultSharedPreferences(this@PointHistriesActivity)

        loadPointHistoryKnowledge()
        loadPointHistoryGame()
        loadPointHistoryRiskMeter()




        rv_point_histries.layoutManager = LinearLayoutManager(this)
        rv_point_histries.setHasFixedSize(true)
        txt_title.typeface = MyApp.typeFace.heavy
        txt_redeem.typeface = MyApp.typeFace.heavy
        txt_title.text = "POINT\nHISTRIES"
        btn_back.setOnClickListener {
            onBackPressed()
        }


    }

    fun loadPointHistoryKnowledge(){
        manageSub(
                service.getPointHistoryKnowledge(prefer.getString(KEYPREFER.UserId,""))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ c -> run {
                            dataList.apply {
                                c.forEach {
                                    item -> add(Model.Point(item.date.time,utils.txtLocale(item.title_th,
                                        item.title_eng),item.point))
                                    dataDate.add(item.date.time)
                                    Timber.d { "add [" + item.title_eng + "] to array" }
                                }
                            }
                            rv_point_histries.adapter = MasterAdapter.PointsAdapter(sortDate(dataList,dataDate),false)

                            Timber.d { "check response [" + c.size + "]" }
                        }},{
                            Timber.d { it.message!! }
                        })
        )
    }
    fun loadPointHistoryGame(){
        manageSub(
                service.getPointHistoryGame("1")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ c -> run {
                            dataList.apply {
                                c.forEach {
                                    item -> add(Model.Point(item.date.time,utils.txtLocale(item.game_name_th,
                                        item.game_name_eng),item.point))
                                    dataDate.add(item.date.time)
                                    Timber.d { "add [" + item.game_name_eng + "] to array" }
                                }
                            }
                            rv_point_histries.adapter = MasterAdapter.PointsAdapter(sortDate(dataList,dataDate),false)
                            Timber.d { "check response [" + c.size + "]" }
                        }},{
                            Timber.d { it.message!! }
                        })
        )
    }
    fun loadPointHistoryRiskMeter(){
        manageSub(
                service.getPointHistoryRiskMeter("1")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ c -> run {
                            dataList.apply {
                                c.forEach {
                                    item -> add(Model.Point(item.date.time,item.risk_status,item.point))
                                    dataDate.add(item.date.time)
                                    Timber.d { "add [" + item.risk_status + "] to array" }
                                }
                            }
                            rv_point_histries.adapter = MasterAdapter.PointsAdapter(sortDate(dataList,dataDate),false)
                            Timber.d { "check response [" + c.size + "]" }
                        }},{
                            Timber.d { it.message!! }
                        })
        )
    }


    fun sortDate(master:ObservableArrayList<Model.Point>,dataLong:ArrayList<Long>):ObservableArrayList<Model.Point>{
        Collections.sort(dataLong)
        var result = ObservableArrayList<Model.Point>()

        for (long in dataLong){
            for(data in master){
                if(data.time == long){
                    d{"add [$long] ["+data.title+"]["+data.time+"] to result"}
                    result.add(data)
                }
            }
        }


        return result
    }


    override fun onPause() {
        super.onPause()
        unsubscribe()
    }
}