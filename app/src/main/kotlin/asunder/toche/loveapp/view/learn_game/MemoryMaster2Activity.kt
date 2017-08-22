package asunder.toche.loveapp

import android.annotation.SuppressLint
import android.content.Context
import android.databinding.ObservableList
import android.os.Bundle
import android.os.CountDownTimer
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.ImageView
import android.widget.TextView
import asunder.toche.loveapp.*
import asunder.toche.loveapp.R.id.icon_random
import com.bumptech.glide.Glide
import com.github.ajalt.timberkt.Timber.d
import kotlinx.android.synthetic.main.header_logo_blue_back.*
import kotlinx.android.synthetic.main.memory_master_2.*
import java.util.*

/**
 * Created by admin on 8/13/2017 AD.
 */
class MemoryMaster2Activity:AppCompatActivity(){

    companion object {
        lateinit var utils :Utils
        lateinit var rv_game2:RecyclerView
        lateinit var iconGame :ImageView
        lateinit var txtCorrect :TextView
        lateinit var adapter :Game2Adapter
        var posPick :Int =0
        lateinit var data :Model.Game2
        lateinit var randomPick :ObservableList<Model.Game1>


        fun nextPic(context:Context){
            //change data and randomPick
            if(posPick <= 15 && isTimeRunner) {
                adapter.randomPosition()
                /*
                data = utils.getDataGame2()
                randomPick = data.randomPick
                adapter = Game2Adapter(data.itemGame,15)
                rv_game2.adapter = adapter
                */
                Glide.with(context)
                        .load(randomPick[posPick].icon)
                        .into(iconGame)

            }else{
                countDown.cancel()
                countDown.onFinish()
            }
            txtCorrect.text = ": $posPick/15"

        }
        lateinit var countDown :CountDownTimer
        var isTimeRunner :Boolean = false
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.memory_master_2)

        Glide.with(this)
                .load(R.drawable.bg_white)
                .into(bg_root)
        utils = Utils(this)
        txt_title.typeface = MyApp.typeFace.heavy
        txt_title.text = "MEMORY\nMASTER"
        iconGame = findViewById(R.id.icon_random)
        txtCorrect = findViewById(R.id.txt_correct)
        rv_game2 = findViewById(R.id.rv_mm_game_2)
        txtCorrect.text=": 0/15"

        data = utils.getDataGame2()
        randomPick = data.randomPick
        adapter =Game2Adapter(data.itemGame,15)

        rv_game2.layoutManager = GridLayoutManager(this,5)
        rv_game2.adapter = adapter
        rv_game2.setHasFixedSize(true)
        btn_back.setOnClickListener {
            onBackPressed()
        }

        txt_time.text = "Start!"
        btn_console.setOnClickListener {
        }

        initRandomPick(randomPick)

        countDown = object : CountDownTimer(31000,1000){
            override fun onFinish() {
                txt_time.text = "Finish!"
                isTimeRunner = false
                posPick = 0
            }

            override fun onTick(millisUntilFinished: Long) {
                txt_time.text = (millisUntilFinished/1000).toString() +" Sec"
                isTimeRunner = true
            }

        }.start()


    }

     fun initRandomPick(randomPick:ObservableList<Model.Game1>){
        if(posPick <= randomPick.size-1) {
            Glide.with(this@MemoryMaster2Activity)
                    .load(randomPick[posPick].icon)
                    .into(iconGame)
        }
    }
}