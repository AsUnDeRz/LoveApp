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
import io.reactivex.schedulers.Schedulers
import java.io.FileOutputStream
import java.net.URL


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
        Timber.plant(Timber.DebugTree())
    }



    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)

    }
}