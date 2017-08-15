package asunder.toche.loveapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.view.ViewPager
import android.widget.Toast
import asunder.toche.loveapp.R
import com.github.ajalt.timberkt.Timber.d
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx
import view.custom_view.CustomViewpager

class ActivityMain : AppCompatActivity() {

        companion object {
            lateinit var vp_main : CustomViewpager
            lateinit var bnve : BottomNavigationViewEx
        }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)

            vp_main = findViewById(R.id.vp_main)
            bnve = findViewById(R.id.bnve)

            setUpBottomBar()


            val adapterViewPager = MainAdapter(supportFragmentManager, this)
            vp_main.adapter = adapterViewPager
            vp_main.setAllowedSwipeDirection(CustomViewpager.SwipeDirection.none)
            /*
            vp_main.addOnPageChangeListener(object:ViewPager.OnPageChangeListener{
                override fun onPageScrollStateChanged(state: Int) {
                }

                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                }

                override fun onPageSelected(position: Int) {
                    Toast.makeText(applicationContext,"Position =$position",Toast.LENGTH_LONG).show()
                }
            })
            */



        }

        fun setUpBottomBar(){

            bnve.enableAnimation(false)
            bnve.enableShiftingMode(false)
            bnve.enableItemShiftingMode(false)
            bnve.setIconsMarginTop(30)
            bnve.setIconSize(30f,30f)

            bnve.onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->

                when (item.itemId){
                    R.id.setting -> vp_main.setCurrentItem(4,false)
                    R.id.learnandgame -> vp_main.setCurrentItem(3,false)
                    R.id.iam -> vp_main.setCurrentItem(2,false)
                    R.id.lab -> vp_main.setCurrentItem(1,false)
                    R.id.home -> vp_main.setCurrentItem(0,false)
                }

                true
            }

        }

}
