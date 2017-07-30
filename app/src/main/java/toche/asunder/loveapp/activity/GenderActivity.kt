package toche.asunder.loveapp.activity

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import kotlinx.android.synthetic.main.gender_list.*
import toche.asunder.loveapp.R
import toche.asunder.loveapp.adapter.GenderAdapter
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

/**
 * Created by admin on 7/30/2017 AD.
 */
class GenderActivity : AppCompatActivity(){


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.gender_list)

        rv_gender.layoutManager = LinearLayoutManager(this) 
        rv_gender.setHasFixedSize(true)
        rv_gender.adapter = GenderAdapter(this)

    }


    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }
}