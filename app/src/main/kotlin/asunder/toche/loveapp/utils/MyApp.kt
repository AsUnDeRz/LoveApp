package asunder.toche.loveapp

import android.annotation.SuppressLint
import android.app.Application

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
    }
}