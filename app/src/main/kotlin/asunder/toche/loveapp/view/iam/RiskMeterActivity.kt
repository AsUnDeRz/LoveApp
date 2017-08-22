package asunder.toche.loveapp

import android.app.Activity
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.databinding.ObservableArrayList
import android.databinding.ObservableList
import android.os.Bundle
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.bumptech.glide.Glide
import com.github.ajalt.timberkt.Timber.d
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.risk_meter.*
import view.custom_view.CustomViewpager

/**
 * Created by admin on 8/8/2017 AD.
 */
class RiskMeterActivity:AppCompatActivity(){

    companion object {
        lateinit var vp_riskmeter : CustomViewpager
        var riskAnswer = arrayOfNulls<Int>(7)
        fun sendRiskAnswer(context:Context){
            var answer =""
          for(i in riskAnswer){
             answer +=i
          }


            Toast.makeText(context,answer,Toast.LENGTH_LONG).show()
            val intent = Intent()
            intent.putExtra("answer",answer)
            context.startActivity(intent.setClass(context,RiskMeterFinalActivity::class.java))
            val activity = context as Activity
            activity.finish()
        }

        //data
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.risk_meter)
        Glide.with(this)
                .load(R.drawable.bg_white)
                .into(bg_root)


        val adapterViewPager = RiskMeterAdapter(supportFragmentManager, this)
        vp_riskmeter = findViewById(R.id.vpriskmeter)
        vp_riskmeter.adapter = adapterViewPager
        vp_riskmeter.setAllowedSwipeDirection(CustomViewpager.SwipeDirection.none)


    }

}