package asunder.toche.loveapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import asunder.toche.loveapp.R
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.loading_page.*

/**
 * Created by admin on 8/7/2017 AD.
 */
class LoadingActivity:AppCompatActivity(){

    lateinit var utilDb :AppDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.loading_page)
        utilDb = AppDatabase(this@LoadingActivity)


        Glide.with(this)
                .load(R.drawable.bg_blue_only)
                .into(bg_root)

        Glide.with(this)
                .load(R.drawable.logo)
                .into(bg_logo)



        val intentThis = Intent()
        val splash = Handler()
        splash.postDelayed({
            startActivity(intentThis.setClass(this, OldNewUserActivity::class.java))
            finish()
        },3000)
    }
}