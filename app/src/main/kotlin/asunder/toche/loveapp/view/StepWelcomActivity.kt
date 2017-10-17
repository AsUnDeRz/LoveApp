package asunder.toche.loveapp

import android.content.Intent
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.stepwelcom.*
import com.synnapps.carouselview.ImageListener
import com.synnapps.carouselview.ViewListener






/**
 * Created by ToCHe on 10/8/2017 AD.
 */
class StepWelcomActivity:AppCompatActivity(){


    val img = arrayOf(R.drawable.pic3,R.drawable.pic2,R.drawable.pic1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.stepwelcom)


        txt_infomation.text = getString(R.string.security)
        txt_nametest.text = getString(R.string.securitytext)
        carouselView.pageCount = img.size
        carouselView.setImageListener(imageListener)
        carouselView.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(position: Int) {
                when(position){
                    2 -> { btn_start.visibility = View.VISIBLE
                        txt_infomation.text = getString(R.string.nearby)
                        txt_nametest.text = getString(R.string.nearbytext)
                        PushDownAnim.setOnTouchPushDownAnim(btn_start)
                        btn_start.setOnClickListener {
                        startActivity(Intent().setClass(this@StepWelcomActivity,GenderActivity::class.java))
                        finish()
                    }}
                    1 ->{
                        btn_start.visibility = View.INVISIBLE
                        txt_infomation.text = getString(R.string.pillreminder)
                        txt_nametest.text = getString(R.string.pillremindertext)

                    }
                    0 ->{
                        btn_start.visibility = View.INVISIBLE
                        txt_infomation.text = getString(R.string.security)
                        txt_nametest.text = getString(R.string.securitytext)
                    }
                    else -> { btn_start.visibility = View.INVISIBLE}
                }
            }
        })
    }

    var imageListener: ImageListener = ImageListener {
        position, imageView ->
        Glide.with(this@StepWelcomActivity)
                .load(img[position])
                .into(imageView)
    }




}