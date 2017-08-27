package asunder.toche.loveapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import asunder.toche.loveapp.R
import com.bumptech.glide.Glide
import com.github.ajalt.timberkt.Timber
import com.github.ajalt.timberkt.Timber.d
import kotlinx.android.synthetic.main.loading_page.*
import android.graphics.drawable.AnimationDrawable
import android.preference.PreferenceManager


/**
 * Created by admin on 8/7/2017 AD.
 */
class LoadingActivity:AppCompatActivity(){

    lateinit var utilDb :AppDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.loading_page)
        utilDb = AppDatabase(this@LoadingActivity)
        if (ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this@LoadingActivity, 123, Manifest.permission.ACCESS_FINE_LOCATION, true)
        }else{
            postDelayed()
        }



        Glide.with(this)
                .load(R.drawable.bg_blue_only)
                .into(bg_root)

        Glide.with(this)
                .load(R.drawable.logo)
                .into(bg_logo)


        //root_animation.post(Starter())




    }

    internal inner class Starter : Runnable {
        override fun run() {
            //val animation = root_animation.background as AnimationDrawable
            //animation.start()
        }
    }

    fun checkFirstTime():Boolean{
        var isFirst = false
        val preferences = PreferenceManager.getDefaultSharedPreferences(this@LoadingActivity)
        if(preferences.getBoolean(KEYPREFER.isFirst,true)){
            isFirst = true
            d{"user open app isFirst"}
            val editor = preferences.edit()
            editor.putBoolean(KEYPREFER.isFirst, true)
            editor.apply()
            // gen user id
        }else{
           d{"user open app normal"}
            d{"Check user ID ["+preferences.getString(KEYPREFER.UserId,"")+"]"}

        }

        return isFirst
    }

    fun checkPasscode():Boolean{
        var havePasscode = false
        val preferences = PreferenceManager.getDefaultSharedPreferences(this@LoadingActivity)
        if(preferences.getString(KEYPREFER.PASSCODE,"") != ""){
            havePasscode = true
        }


        return havePasscode
    }

    fun postDelayed(){
        val intentThis = Intent()
        val splash = Handler()
        if(checkFirstTime()){
            splash.postDelayed({
                startActivity(intentThis.setClass(this, OldNewUserActivity::class.java))
                finish()
            },3000)
        }else{
            if(checkPasscode()){
                intentThis.putExtra(KEYPREFER.PASSCODE,"check")
                startActivity(intentThis.setClass(this, PassCodeActivity::class.java))
                finish()
            }else{
                startActivity(intentThis.setClass(this, ActivityMain::class.java))
                finish()
            }

        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode != 123) {
            return
        }else {
            postDelayed()
            if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Enable the my location layer if the permission has been granted.
                d { "Permission true" }
            } else {
                // Display the missing permission error dialog when the fragments resume.
                d { "Permission false" }

            }
        }
    }
}