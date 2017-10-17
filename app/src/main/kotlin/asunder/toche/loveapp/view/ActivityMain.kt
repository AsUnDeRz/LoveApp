package asunder.toche.loveapp

import android.Manifest
import android.annotation.SuppressLint
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.view.ViewPager
import android.widget.Toast
import asunder.toche.loveapp.R
import com.bumptech.glide.Glide
import com.github.ajalt.timberkt.Timber.d
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx
import kotlinx.android.synthetic.main.activity_main.*
import view.custom_view.CustomViewpager
import android.content.Intent
import android.databinding.ObservableArrayList
import android.databinding.ObservableList
import android.preference.PreferenceManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.tapadoo.alerter.Alerter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.lab.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class ActivityMain : AppCompatActivity() {


    var finis = 0
    companion object {
        lateinit var vp_main : CustomViewpager
            @SuppressLint("StaticFieldLeak")
            lateinit var bnve : BottomNavigationViewEx

    }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)
            d{"onCreate"}
            vp_main = findViewById(R.id.vp_main_fragment)
            bnve = findViewById(R.id.bottom_nav)
            setUpBottomBar()
            Glide.with(this)
                    .load(R.drawable.bg_white)
                    .into(bg_root)



            val adapterViewPager = MainAdapter(supportFragmentManager, this)
            vp_main.adapter = adapterViewPager
            vp_main.setAllowedSwipeDirection(CustomViewpager.SwipeDirection.none)
            vp_main.offscreenPageLimit = 1

            vp_main.addOnPageChangeListener(object:ViewPager.OnPageChangeListener{
                override fun onPageScrollStateChanged(state: Int) {
                }

                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                }
                override fun onPageSelected(position: Int) {
                    //Toast.makeText(applicationContext,"Position =$position",Toast.LENGTH_LONG).show()

                    when(position){
                        2 -> {
                            Glide.with(this@ActivityMain)
                                    .load(R.drawable.bg_blue)
                                    .into(bg_root)
                        }
                        1 -> {}
                        else ->{
                            Glide.with(this@ActivityMain)
                                    .load(R.drawable.bg_white)
                                    .into(bg_root)
                        }
                    }

                }
            })
        }


    override fun onResume() {
        super.onResume()
        d{"onResume"}
    }

        fun setUpBottomBar(){

            bnve.enableAnimation(false)
            bnve.enableShiftingMode(false)
            bnve.enableItemShiftingMode(false)
            bnve.setIconsMarginTop(resources.getInteger(R.integer.marginBottomBarMain))
            bnve.setIconSize(resources.getInteger(R.integer.bottomBarIconSize).toFloat()
                    ,resources.getInteger(R.integer.bottomBarIconSize).toFloat())

            bnve.onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->

                when (item.itemId){
                    R.id.setting -> checkUserSetHivStatus(KEYPREFER.SETTING)
                    R.id.learnandgame -> checkUserSetHivStatus(KEYPREFER.LEARNGAME)
                    R.id.iam -> checkUserSetHivStatus(KEYPREFER.IAM)
                    R.id.lab -> checkUserSetHivStatus(KEYPREFER.LAB)
                    R.id.home -> vp_main.setCurrentItem(KEYPREFER.HOME,false)
                }

                true
            }

        }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putInt("viewpager", vp_main.currentItem)
        d{"onSaveSate viewpager =["+ vp_main.currentItem+"]"}
    }



    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        d{"onRestore viewpager =["+savedInstanceState?.getInt("viewpager",0)!!+"]"}
        vp_main.currentItem = savedInstanceState?.getInt("viewpager",0)!!
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onBackPressed() {
        if(vp_main.currentItem == 0){
            showAlerter()

        }else if(vp_main.currentItem >= 1){
            vp_main.setCurrentItem(0,false)
            bnve.currentItem = 0
            return

        }

    }

    fun alerter(){

    }
    fun showAlerter(){
        d{"$finis  show Alerter"}
        when (finis) {
            0 -> {

            }
            1 -> {
                Alerter.create(this)
                        .setText(getString(R.string.alterhome))
                        .setTitleTypeface(Utils(this).heavy)
                        .setTextTypeface(Utils(this).heavy)
                        .setBackgroundColorRes(R.color.colorAccent) // or setBackgroundColorInt(Color.CYAN)
                        .show()

            }
            else -> {
                finis =0
                val setIntent = Intent(Intent.ACTION_MAIN)
                setIntent.addCategory(Intent.CATEGORY_HOME)
                setIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(setIntent)
            }
        }
        finis++
    }

    fun checkUserSetHivStatus(keyprefer:Int){
        val preferences = PreferenceManager.getDefaultSharedPreferences(this@ActivityMain)
        if(preferences.getBoolean(KEYPREFER.isFirst,true)){
            val intent =Intent()
            intent.putExtra("from",keyprefer)
            startActivity(Intent().setClass(this@ActivityMain,HivStatusActivity::class.java))
        }else {
            vp_main.setCurrentItem(keyprefer, false)
        }
    }







}
