package asunder.toche.loveapp

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import asunder.toche.loveapp.R
import asunder.toche.loveapp.databinding.LearnGameItemBinding
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
        rv_learngame.adapter = LearnGameAdapter(LearnGameList,false)
        //set title
        btn_back.setOnClickListener {
            onBackPressed()
        }



    }

    fun LearnGameAdapter(item: List<Any>, stableIds: Boolean): LastAdapter {
        return LastAdapter(item,BR.learnGameItem,stableIds).type{ item, position ->
            when(item){
                is Model.LearnGameContent -> LearnGameType
                else -> null
            }
        }
    }

    private val LearnGameType = Type<LearnGameItemBinding>(R.layout.learn_game_item)
            .onCreate { println("Created ${it.binding.learnGameItem} at #${it.adapterPosition}") }
            .onBind { println("Bound ${it.binding.learnGameItem} at #${it.adapterPosition}") }
            .onRecycle { println("Recycled ${it.binding.learnGameItem} at #${it.adapterPosition}") }
            .onClick {
                if(it.binding.titleGame.text == "Memory Master #1"){
                    startActivity(Intent().setClass(this,MemoryMasterActivity::class.java))
                }
                if(it.binding.titleGame.text == "Memory Master #2"){
                    startActivity(Intent().setClass(this,MemoryMaster2Activity::class.java))

                }

            }
            .onLongClick {}
}
