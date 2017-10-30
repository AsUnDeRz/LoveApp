package asunder.toche.loveapp

import android.Manifest
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.databinding.ObservableArrayList
import android.databinding.ObservableList
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
import android.view.View
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.Dexter
import com.karumi.dexter.listener.PermissionRequest


/**
 * Created by admin on 8/7/2017 AD.
 */
class LoadingActivity:AppCompatActivity(),ViewModel.MainViewModel.RiskQInterface{


    private val mTapScreenTextAnimDuration = 10
    private val mTapScreenTextAnimBreak = 500L
    var isFirst = false
    lateinit var utilDb :AppDatabase
    lateinit var  handler: Handler
    lateinit var runnable: Runnable
    lateinit var MainViewModel : ViewModel.MainViewModel
    lateinit var preferences :SharedPreferences

    var loadSuccess=0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.loading_page)
        utilDb = AppDatabase(this@LoadingActivity)
        preferences = PreferenceManager.getDefaultSharedPreferences(this@LoadingActivity)
        MainViewModel = ViewModelProviders.of(this).get(ViewModel.MainViewModel::class.java)
        MainViewModel.loadImage(this)
        //SceneAnimation(root_animation, DataSimple.imgAnimation.toIntArray(), mTapScreenTextAnimDuration, mTapScreenTextAnimBreak,view_loading)

        Glide.with(this@LoadingActivity)
                .load(R.drawable.loading)
                .into(root_animation)


        if (ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this@LoadingActivity, 123, Manifest.permission.ACCESS_FINE_LOCATION, true)
        }else{
           postDelayed()
        }

    }

    override fun endCallProgress(any: Any) {
            //add to local db
        val resultList = any as ObservableArrayList<*>

            when (resultList[0]) {
                is Model.RepositoryKnowledge -> {
                    utilDb.addKnowledgeContent(resultList as ObservableArrayList<Model.RepositoryKnowledge>)
                    loadSuccess++
                    d{"Load Knowledge Success"}
                }
                is Model.Province -> {
                    if(utilDb.getProvince().size ==0) {
                        utilDb.addProvince(resultList as ObservableArrayList<Model.Province>)
                    }
                    loadSuccess++
                    d{"Load Province Success"}
                }
                is Model.RiskQuestion -> {
                    if(utilDb.getRiskQuestion().size == 0) {
                        utilDb.addRiskQustion(resultList as ObservableArrayList<Model.RiskQuestion>)
                    }
                    loadSuccess++
                    d{"Load RiskQuestion Success"}
                }
                is Model.KnowledgeGroup -> {
                    utilDb.addKnowledgeGroup(resultList as ObservableArrayList<Model.KnowledgeGroup>)
                    loadSuccess++
                    d{"Load KnowledgeGroup Success"}
                }

                is Model.RepositoryJob -> {
                    if(utilDb.getJobs().size ==0) {
                        utilDb.addJobs(resultList as ObservableArrayList<Model.RepositoryJob>)
                    }
                    loadSuccess++
                    d{"Load Job Success"}
                }
                is Model.RepositoryNational -> {
                    if(utilDb.getNations().size ==0) {
                        utilDb.addNationals(resultList as ObservableArrayList<Model.RepositoryNational>)
                    }
                    loadSuccess++
                    d{"Load nation Success"}
                }
                is Model.ImageHome -> {
                    if(DataSimple.imageHome.size ==0) {
                        for (data in resultList as ObservableArrayList<Model.ImageHome>) {
                            DataSimple.imageHome.add(data)
                            //DataSimple.imageHome.add("http://backend.loveapponline.com/"+data.image_byte)
                            d { "Add image home  http://backend.loveapponline.com/" + data.image_byte }
                        }
                    }
                    loadSuccess++
                    d{"Load imageHome Success"}
                }
            }

        if(isFirst && loadSuccess == 5){
            handler.postDelayed(runnable,4000)
        }

        d{"Check LoadSuccess $loadSuccess"}



    }


    fun checkFirstTime():Boolean{
        if(preferences.getBoolean(KEYPREFER.isFirst,true)){
            isFirst = true
            d{"user open app isFirst"}
            val editor = preferences.edit()
            editor.putBoolean(KEYPREFER.isFirst, true)
            editor.apply()
            MainViewModel.loadRiskQuestion(this)
            MainViewModel.loadProvince(this)
            MainViewModel.loadJob(this)
            MainViewModel.loadNational(this)
            //MainViewModel.loadKnowledage(this,"0")
            //MainViewModel.loadKnowledgeGroup("1",this,Utils(this@LoadingActivity))
            // gen user id
        }else{
           d{"user open app normal"}
            d{"Check user ID ["+preferences.getString(KEYPREFER.UserId,"")+"]"}
            //load and check infomation user if
            if(utilDb.getKnowledgeContent().size == 0) {
                MainViewModel.loadKnowledage(this, preferences.getString(KEYPREFER.UserId, ""))
                MainViewModel.loadKnowledgeGroup(preferences.getString(KEYPREFER.GENDER, ""), this, Utils(this@LoadingActivity))
            }else{
                loadSuccess = 3
                handler.postDelayed(runnable,2000)
            }
        }

        return isFirst
    }

    fun checkPasscode():Boolean{
        var havePasscode = false
        if(preferences.getString(KEYPREFER.PASSCODE,"") != ""){
            havePasscode = true
        }

        havePasscode = preferences.getBoolean(KEYPREFER.ISCHECKPASSCODE,false)

        return havePasscode
    }



    fun postDelayed(){
        val intentThis = Intent()
        handler = Handler()
        runnable = Runnable({})
        if(checkFirstTime()){
            runnable = Runnable({
                if(preferences.getString(KEYPREFER.UserId,"") != ""){
                    startActivity(intentThis.setClass(this, ActivityMain::class.java))
                    finish()
                    overridePendingTransition( R.anim.fade_in, R.anim.fade_out )
                }else {
                    startActivity(intentThis.setClass(this, SelectLangActivity::class.java))
                    finish()
                }
                //overridePendingTransition(R.anim.slide_from_right,R.anim.slide_to_left)
            })
            if(isFirst && loadSuccess == 5){
                handler.postDelayed(runnable,4000)
            }
        }else{
            if(checkPasscode()){
                runnable = Runnable({
                    intentThis.putExtra(KEYPREFER.PASSCODE,"check")
                    startActivity(intentThis.setClass(this, PassCodeActivity::class.java))
                    finish()
                    overridePendingTransition( R.anim.fade_in, R.anim.fade_out )

                })
            }else{
                runnable = Runnable({
                    startActivity(intentThis.setClass(this, ActivityMain::class.java))
                    finish()
                    overridePendingTransition( R.anim.fade_in, R.anim.fade_out )

                })
            }
            if(loadSuccess == 3){
                handler.postDelayed(runnable,4000)
            }

        }
    }

    public override fun onResume() {
        super.onResume()
        view_loading?.visibility = View.VISIBLE
    }

    override fun onPause() {
        super.onPause()
        //spin_kit?.visibility = View.INVISIBLE

    }

    public override fun onStop() {
        super.onStop()
        MainViewModel.unsubscribe()
        handler.removeCallbacks(runnable)
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