package toche.asunder.loveapp.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.synnapps.carouselview.ImageListener
import kotlinx.android.synthetic.main.header_home.*
import kotlinx.android.synthetic.main.home.*
import toche.asunder.loveapp.MyApp
import toche.asunder.loveapp.R
import toche.asunder.loveapp.adapter.HomeAdapter

/**
 * Created by admin on 7/30/2017 AD.
 */
class HomeActivity :AppCompatActivity(){

    val banner = arrayOf(R.drawable.silde_img_1,R.drawable.silde_img_1,R.drawable.silde_img_1)

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent().setClass(this@HomeActivity, LoginActivity::class.java))
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home)

        title_app.typeface = MyApp.typeFace.heavy
        txt_hello.typeface = MyApp.typeFace.heavy
        cV.pageCount = banner.size
        cV.setImageListener(listener)

        rv_home.layoutManager = LinearLayoutManager(this)
        rv_home.setHasFixedSize(true)
        rv_home.adapter = HomeAdapter(this@HomeActivity)

        button_noti.setOnClickListener {
            startActivity(Intent().setClass(this@HomeActivity, NotificationActivity::class.java))
            finish()
        }
    }

    private var listener: ImageListener = ImageListener { position, imageView ->
        //imageView.scaleType = ImageView.ScaleType.FIT_XY
        Glide.with(this)
                .load(banner[position])
                .into(imageView)
    }


}