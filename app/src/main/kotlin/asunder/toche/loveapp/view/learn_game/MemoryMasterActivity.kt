package asunder.toche.loveapp

import android.animation.AnimatorInflater
import android.animation.ObjectAnimator
import android.databinding.ObservableArrayList
import android.databinding.ObservableList
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import asunder.toche.loveapp.R
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.header_logo_blue_back.*
import kotlinx.android.synthetic.main.memory_master.*
import android.view.animation.AccelerateInterpolator
import android.opengl.ETC1.getHeight
import android.opengl.ETC1.getWidth
import android.support.v7.widget.GridLayoutManager
import android.view.View
import android.databinding.adapters.TextViewBindingAdapter.setText
import android.os.CountDownTimer
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.github.ajalt.timberkt.Timber.d
import kotlinx.android.synthetic.main.learn_game_list.*


/**
 * Created by admin on 8/5/2017 AD.
 */
class MemoryMasterActivity:AppCompatActivity(){

    enum class Chapter{
        one,two,three,finish
    }
    var totalPoint:Int =0
    var chapterPoint = arrayListOf(0,0,0)
    var timeInChapter : Long =0
    lateinit var utils : Utils
    lateinit var chapterGame : Chapter
    lateinit var adapter : Game1Adapter


    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.memory_master)

        utils = Utils(this)
        chapterGame = Chapter.one
        txt_title.typeface = MyApp.typeFace.heavy
        txt_title.text = "MEMORY\nMASTER"
        txt_time.typeface = MyApp.typeFace.heavy

        initStageGame(chapterGame)

        btn_back.setOnClickListener {
            onBackPressed()
        }





        txt_time.text = "Start!"
        btn_time.setOnClickListener {
            object : CountDownTimer(timeInChapter, 1000) { //Sets 10 second remaining
                override fun onTick(millisUntilFinished: Long) {
                    txt_time.text = ": "+(millisUntilFinished/1000).toString() +" Sec"
                    if(adapter.currnetPointInChapter == adapter.maxPointInChapter){
                        this.cancel()
                        this.onFinish()
                        adapter.currnetPointInChapter =0
                    }
                }
                override fun onFinish() {
                    btn_time.isClickable = true
                    txt_time.text = "Start!"
                    ChangeState(chapterGame,adapter.currnetPointInChapter)
                    initStageGame(chapterGame)
                }

            }.start()
            btn_time.isClickable = false
        }

    }



    fun ChangeState(chapter:Chapter,point:Int){
        when(chapter){
            Chapter.one -> {
                chapterPoint[0] = point
                chapterGame = Chapter.two
            }
            Chapter.two -> {
                chapterPoint[1] = point
                chapterGame = Chapter.three
            }
            Chapter.three -> {
                chapterPoint[2] = point
                chapterGame = Chapter.finish
            }
        }
    }

    fun initStageGame(chapter:Chapter){

        when (chapter){
            Chapter.one -> {
                timeInChapter = 5000
                adapter = Game1Adapter(utils.getDataGame1Chapter1(),1)
                linearLayout4.layoutManager = GridLayoutManager(this,2)
                linearLayout4.adapter = adapter
            }
            Chapter.two ->{
                timeInChapter = 10000
                adapter = Game1Adapter(utils.getDataGame1Chapter2(),2)
                linearLayout4.layoutManager = GridLayoutManager(this,2)
                linearLayout4.adapter = adapter
            }
            Chapter.three ->{
                timeInChapter = 30000
                adapter = Game1Adapter(utils.getDataGame1Chapter3(),8)
                linearLayout4.layoutManager = GridLayoutManager(this,4)
                linearLayout4.adapter = adapter
            }
            Chapter.finish -> {
                for(point in chapterPoint){
                    totalPoint+=point
                }
                d{totalPoint.toString()}
                Toast.makeText(applicationContext,"Finish Point = "+totalPoint,Toast.LENGTH_LONG).show()
                adapter = Game1Adapter(utils.getDataGame1Chapter3(),8)
                linearLayout4.layoutManager = GridLayoutManager(this,4)
                linearLayout4.adapter = adapter
            }
        }
        linearLayout4.setHasFixedSize(true)
    }




}