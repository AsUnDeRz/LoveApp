package asunder.toche.loveapp

import android.Manifest
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



class ActivityMain : AppCompatActivity() {

        companion object {
            lateinit var vp_main : CustomViewpager
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

        fun setUpBottomBar(){

            bnve.enableAnimation(false)
            bnve.enableShiftingMode(false)
            bnve.enableItemShiftingMode(false)
            bnve.setIconsMarginTop(30)
            bnve.setIconSize(30f,30f)

            bnve.onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->

                when (item.itemId){
                    R.id.setting -> vp_main.setCurrentItem(KEYPREFER.SETTING,false)
                    R.id.learnandgame -> vp_main.setCurrentItem(KEYPREFER.LEARNGAME,false)
                    R.id.iam -> vp_main.setCurrentItem(KEYPREFER.IAM,false)
                    R.id.lab -> vp_main.setCurrentItem(KEYPREFER.LAB,false)
                    R.id.home -> vp_main.setCurrentItem(KEYPREFER.HOME,false)
                }

                true
            }

        }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        d{"onSaveSate"}
    }


    override fun onBackPressed() {
        super.onBackPressed()
        val setIntent = Intent(Intent.ACTION_MAIN)
        setIntent.addCategory(Intent.CATEGORY_HOME)
        setIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(setIntent)
    }




}
