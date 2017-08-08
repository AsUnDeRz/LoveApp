package asunder.toche.loveapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import asunder.toche.loveapp.R
import kotlinx.android.synthetic.main.header_logo_white_back.*
import kotlinx.android.synthetic.main.learn_game_list.*


/**
 * Created by admin on 8/5/2017 AD.
 */
class LearnGameMainActivity: AppCompatActivity(){

    var LearnGameList = arrayListOf<Model.LearnGameContent>()




    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.learn_game_list)
        title_header.text = intent.extras.getString("title")

        when(intent.extras.getInt("key")){
            1 ->{
                LearnGameList = arrayListOf<Model.LearnGameContent>().apply {
                    for(i in 1..3) {
                        add(Model.LearnGameContent(R.drawable.clinic_img, "Memory Master #$i", "1"+i+"0", "20"))
                        add(Model.LearnGameContent(R.drawable.clinic_img, "Co-op Game", "100", "10"))

                    }
                }
            }
            2 ->{
                LearnGameList = arrayListOf<Model.LearnGameContent>().apply {
                    for(i in 1..3) {
                        add(Model.LearnGameContent(R.drawable.clinic_img, "HIV Safe Sex #$i", "1"+i+"0", "20"))
                        add(Model.LearnGameContent(R.drawable.clinic_img, "Safe Sex", "100", "10"))

                    }
                }
            }
        }


        rv_learngame.layoutManager = LinearLayoutManager(this@LearnGameMainActivity)
        rv_learngame.setHasFixedSize(true)
        //rv_learngame.adapter = LearnGameListAdapter(this@LearnGameMainActivity,intent.extras.getInt("key"))
        rv_learngame.adapter = MasterAdapter.LearnGameAdapter(LearnGameList,false)
        //set title
        btn_back.setOnClickListener {
            onBackPressed()
        }



    }
}
