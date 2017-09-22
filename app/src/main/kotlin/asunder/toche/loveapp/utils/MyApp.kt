package asunder.toche.loveapp

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.databinding.ObservableArrayList
import android.databinding.ObservableList
import android.graphics.Bitmap
import android.support.multidex.MultiDex
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg
import timber.log.BuildConfig
import timber.log.Timber
import android.graphics.BitmapFactory
import com.github.ajalt.timberkt.Timber.d
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.io.FileOutputStream
import java.net.URL
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper
import android.preference.PreferenceManager
import android.content.SharedPreferences
import android.os.Build


/**
 * Created by admin on 8/7/2017 AD.
 */
class MyApp:Application(){

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


    fun loadRiskResult(){
        manageSub(
                service.getRiskResults("2112142")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ c -> run {
                            val data = ObservableArrayList<Model.RiskResult>().apply {
                                c.forEach {
                                    item -> add(item)
                                    d{" Show risk id and detail ["+item.risk_id+"]["+item.details_th+"]"}
                                }
                            }
                            d { "check response [" + c.size + "]" }
                        }},{
                            d { it.message!! }
                        })
        )
    }


    var service : LoveAppService = LoveAppService.create()

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var typeFace: Utils

    }

    override  fun onCreate() {
        super.onCreate()
        //utils typeface
        typeFace = Utils(applicationContext)
        Timber.plant(Timber.DebugTree())


        var lang:String

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            lang =resources.configuration.locales.get(0).language
            d{"check default SDK >= N "+resources.configuration.locales.get(0).language }
        } else {
            lang = resources.configuration.locale.language
            d{"check default SDK < N "+resources.configuration.locale.language}
        }
        val preferences = PreferenceManager.getDefaultSharedPreferences(this@MyApp)
        if(preferences.getString(KEYPREFER.language, lang) == "th"){
            LocalUtil.onAttach(applicationContext,"th")
        }else{
            LocalUtil.onAttach(applicationContext,"en")
        }
        //loadRiskResult()
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)


    }








}