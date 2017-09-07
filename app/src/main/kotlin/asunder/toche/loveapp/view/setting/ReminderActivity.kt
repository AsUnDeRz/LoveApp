package asunder.toche.loveapp

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import asunder.toche.loveapp.R
import com.bumptech.glide.Glide
import com.github.ajalt.timberkt.Timber.d
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.header_logo_white_back.*
import kotlinx.android.synthetic.main.reminder.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

/**
 * Created by admin on 7/31/2017 AD.
 */
class ReminderActivity: AppCompatActivity() {


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

    lateinit var utils :Utils

    lateinit var appDb :AppDatabase
    lateinit var prefer :SharedPreferences
    override fun onBackPressed() {
        //startActivity(Intent().setClass(this@ReminderActivity, NotificationActivity::class.java))
        //finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.reminder)
        appDb = AppDatabase(this@ReminderActivity)
        val notiId = intent?.getIntExtra(KEYPREFER.NOTIID,0)
        d{"Notification id [$notiId]"}
        val notiDb = appDb.getNotiWithState(notiId.toString())!!
        Glide.with(this)
                .load(R.drawable.bg_white)
                .into(bg_root)
        Glide.with(this)
                .load(R.drawable.bg_guy)
                .into(bg_top)
        btn_yes.typeface = MyApp.typeFace.heavy
        btn_no.typeface = MyApp.typeFace.heavy
        btn_back.setOnClickListener {
            onBackPressed()
        }

        btn_yes.setOnClickListener {
            appDb.updateNotification(notiDb,KEYPREFER.TRACKED)
            addTrack("true")
        }
        btn_no.setOnClickListener {
            appDb.updateNotification(notiDb,KEYPREFER.MISSING)
            addTrack("false")


        }

    }

    fun addTrack(status:String){
        d { "input Tracking" }
        val preferences = PreferenceManager.getDefaultSharedPreferences(this@ReminderActivity)
        if (preferences.getString(KEYPREFER.UserId, "") != "") {
            val userID = preferences.getString(KEYPREFER.UserId,"")
            d { " user_id[" + preferences.getString(KEYPREFER.UserId, "") + "]" }
            val addCD4 = service.addTracking(Date().toString(),userID,status)
            addCD4.enqueue(object : Callback<Void> {
                override fun onFailure(call: Call<Void>?, t: Throwable?) {
                    d { t?.message.toString() }
                    finish()
                }

                override fun onResponse(call: Call<Void>?, response: Response<Void>?) {
                    if (response!!.isSuccessful) {
                        d { "addTracking successful" }
                        finish()
                    }
                }
            })
        }
    }


    override fun onPause() {
        super.onPause()
        unsubscribe()
    }
}