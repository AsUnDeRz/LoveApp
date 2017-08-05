package toche.asunder.loveapp.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.header_logo_white_back.*
import kotlinx.android.synthetic.main.learn_game_list.*
import toche.asunder.loveapp.MyApp
import toche.asunder.loveapp.R
import toche.asunder.loveapp.adapter.LearnGameListAdapter

/**
 * Created by admin on 8/5/2017 AD.
 */
class LearnGameMainActivity: AppCompatActivity(){


    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.learn_game_list)
        title_header.typeface = MyApp.typeFace.heavy
        title_header.text = intent.extras.getString("title")


        rv_learngame.layoutManager = LinearLayoutManager(this@LearnGameMainActivity)
        rv_learngame.setHasFixedSize(true)
        rv_learngame.adapter = LearnGameListAdapter(this@LearnGameMainActivity,intent.extras.getInt("key"))

        //set title
        btn_back.setOnClickListener {
            onBackPressed()
        }



    }
}
