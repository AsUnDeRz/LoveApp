package asunder.toche.loveapp

import android.content.Intent
import android.content.SharedPreferences
import android.databinding.ObservableArrayList
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
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
import android.widget.Toast
import android.support.customtabs.CustomTabsIntent
import android.graphics.BitmapFactory
import android.support.v4.content.ContextCompat
import android.support.v4.content.ContextCompat.startActivity
import android.app.Activity
import utils.CustomTabActivityHelper










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

    private var mCustomTabActivityHelper: CustomTabActivityHelper? = null

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

        txt_redeem.text = intent?.extras?.getString(KEYPREFER.POINT) + " : "+getString(R.string.redeempoint)


        btn_redeem.setOnClickListener {
            setupCustomTabHelper("http://www.loveapponline.com/redeem")
            openCustomTab("http://www.loveapponline.com/redeem")
            //val data = Intent(Intent.ACTION_VIEW, Uri.parse("http://www.loveapponline.com/redeem"))
            //startActivity(data)
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
                service.getPointHistoryGame(prefer.getString(KEYPREFER.UserId,""))
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
                service.getPointHistoryRiskMeter(prefer.getString(KEYPREFER.UserId,""))
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

    private fun setupCustomTabHelper(URL:String) {
        mCustomTabActivityHelper = CustomTabActivityHelper()
        mCustomTabActivityHelper!!.setConnectionCallback(mConnectionCallback)
        mCustomTabActivityHelper!!.mayLaunchUrl(Uri.parse(URL), null, null)
    }

    private fun openCustomTab(URL: String) {

        //ตัวแปรนี้จะให้ในการกำหนดค่าต่างๆ ที่ข้างล่างนี้
        val intentBuilder = CustomTabsIntent.Builder()

        intentBuilder.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary))

        //กำหนดให้มี Animation เมื่อ Custom tab เข้ามาและออกไป ถ้าไม่มีจะเหมือน Activity ที่เด้งเข้ามาเลย
        setAnimation(intentBuilder)

        //Launch Custome tab ให้ทำงาน
        CustomTabActivityHelper.openCustomTab(
                this, intentBuilder.build(), Uri.parse(URL), WebviewFallback())
    }

    private fun setAnimation(intentBuilder: CustomTabsIntent.Builder) {
        //intentBuilder.setStartAnimations(this, android.R.anim.slide_out_right, android.R.anim.slide_in_left)
        intentBuilder.setExitAnimations(this, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
    }

    // You can use this callback to make UI changes
    private val mConnectionCallback = object : CustomTabActivityHelper.ConnectionCallback {
        override fun onCustomTabsConnected() {
            //Toast.makeText(this@PointHistriesActivity, "Connected to service", Toast.LENGTH_SHORT).show()
        }

        override fun onCustomTabsDisconnected() {
            //Toast.makeText(this@PointHistriesActivity, "Disconnected from service", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onStart() {
        super.onStart()
        mCustomTabActivityHelper?.bindCustomTabsService(this)
    }

    override fun onStop() {
        super.onStop()
        mCustomTabActivityHelper?.unbindCustomTabsService(this)
    }

    inner class WebviewFallback : CustomTabActivityHelper.CustomTabFallback {
        override fun openUri(activity: Activity, uri: Uri) {
            val intent = Intent(activity, WebViewActivity::class.java)
            intent.putExtra("KEY_URL", uri.toString())
            activity.startActivity(intent)
        }
    }
    override fun onPause() {
        super.onPause()
        unsubscribe()
    }


}