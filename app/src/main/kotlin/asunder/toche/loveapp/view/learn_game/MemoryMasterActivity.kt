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
import android.os.Handler
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.github.ajalt.timberkt.Timber.d


/**
 * Created by admin on 8/5/2017 AD.
 */
class MemoryMasterActivity:AppCompatActivity(){

    enum class Chapter{
        one,two,three,four,five,finish
    }
    var totalPoint:Int =0
    var chapterPoint = arrayListOf(0,0,0,0,0,0)
    var timeInChapter : Long =46000
    lateinit var utils : Utils
    lateinit var chapterGame : Chapter
    lateinit var adapter : Game1Adapter
    lateinit var countDown :CountDownTimer


    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.memory_master)

        Glide.with(this)
                .load(R.drawable.bg_white)
                .into(bg_root)
        utils = Utils(this)
        chapterGame = Chapter.one
        txt_title.typeface = MyApp.typeFace.heavy
        txt_title.text = "MEMORY\nMASTER"
        txt_time.typeface = MyApp.typeFace.heavy


        btn_back.setOnClickListener {
            onBackPressed()
        }

        initStageGame(chapterGame)
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
                chapterGame = Chapter.four
            }
            Chapter.four -> {
                chapterPoint[3] = point
                chapterGame = Chapter.five
            }
            Chapter.five -> {
                chapterPoint[4] = point
                chapterGame = Chapter.finish
            }
        }
    }

    fun initStageGame(chapter:Chapter){
        countDown = object :CountDownTimer(timeInChapter, 1000) { //Sets 10 second remaining
            override fun onTick(millisUntilFinished: Long) {
                txt_time.text = ": "+(millisUntilFinished/1000).toString() +" Sec"
                if(adapter.currnetPointInChapter == adapter.maxPointInChapter){
                    //countDown.cancel()
                    //countDown.onFinish()
                    ChangeState(chapterGame,adapter.currnetPointInChapter)
                    initStageGame(chapterGame)
                    adapter.currnetPointInChapter =0

                }
            }
            override fun onFinish() {
                for(point in chapterPoint){
                    totalPoint+=point
                }
                d{totalPoint.toString()}
                btn_time.isClickable = true
                txt_time.text ="$totalPoint Point"
                //ChangeState(chapterGame,adapter.currnetPointInChapter)
                //initStageGame(chapterGame)
            }
        }

        //4-8-10-12-16-20
        when (chapter){
            Chapter.one -> {
                adapter = Game1Adapter(utils.getDataGame1(2),2)
                linearLayout4.layoutManager = GridLayoutManager(this,2)
                linearLayout4.adapter = adapter
                countDown.start()
            }
            Chapter.two ->{
                adapter = Game1Adapter(utils.getDataGame1(4),4)
                linearLayout4.layoutManager = GridLayoutManager(this,2)
                linearLayout4.adapter = adapter
            }
            Chapter.three -> {
                adapter = Game1Adapter(utils.getDataGame1(6),6)
                linearLayout4.layoutManager = GridLayoutManager(this,3)
                linearLayout4.adapter = adapter
            }
            Chapter.four ->{
                adapter = Game1Adapter(utils.getDataGame1(8),8)
                linearLayout4.layoutManager = GridLayoutManager(this,4)
                linearLayout4.adapter = adapter
            }
            Chapter.five ->{
                adapter = Game1Adapter(utils.getDataGame1(10),10)
                linearLayout4.layoutManager = GridLayoutManager(this,4)
                linearLayout4.adapter = adapter
            }
            Chapter.finish -> {
                for(point in chapterPoint){
                    totalPoint+=point
                }
                d{totalPoint.toString()}
                adapter = Game1Adapter(utils.getDataGame1(10),10)
                linearLayout4.layoutManager = GridLayoutManager(this,4)
                linearLayout4.adapter = adapter
            }
        }
        linearLayout4.setHasFixedSize(true)
    }


    override fun onPause() {
        super.onPause()

    }
}