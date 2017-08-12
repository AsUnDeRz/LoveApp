package asunder.toche.loveapp

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.support.multidex.MultiDex
import timber.log.BuildConfig
import timber.log.Timber

/**
 * Created by admin on 8/7/2017 AD.
 */
class MyApp:Application(){

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var typeFace: Utils
    }

    override  fun onCreate() {
        super.onCreate()

        //utils typeface
        typeFace = Utils(applicationContext)
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }else{
            Timber.plant(Timber.DebugTree())
        }
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)

    }
}