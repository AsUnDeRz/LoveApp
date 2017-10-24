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
import android.preference.PreferenceManager
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.afollestad.materialdialogs.MaterialDialog
import com.github.ajalt.timberkt.Timber.d
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


/**
 * Created by admin on 8/5/2017 AD.
 */
class MemoryMasterActivity:AppCompatActivity(){

    var service : LoveAppService = LoveAppService.create()

    private var _compoSub = CompositeDisposable()
    private val compoSub: CompositeDisposable
        get() {
            if (_compoSub.isDisposed) {
                _compoSub = CompositeDisposable()
            }
            return _compoSub
        }

    protected final fun manageSub(s: Disposable) = compoSub.add(s)

    fun unsubscribe() { compoSub.dispose() }

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
                adapter.clickAble = false
                btn_time.isClickable = true
                txt_time.text ="0 Sec"
                showDialogFinish(totalPoint.toString())
                //txt_time.text ="$totalPoint Point"
                //update point and input point history
                updatePoint(totalPoint.toString())

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




    fun updatePoint(point:String) {
        d{"Update point user"}
        val preferences = PreferenceManager.getDefaultSharedPreferences(this@MemoryMasterActivity)
        if (preferences.getString(KEYPREFER.UserId, "") != "") {
            d{"point [$point] user_id["+preferences.getString(KEYPREFER.UserId,"")+"]"}
            val addPoint = service.addUserPoint(preferences.getString(KEYPREFER.UserId, ""), point)
            addPoint.enqueue(object : Callback<Void> {
                override fun onFailure(call: Call<Void>?, t: Throwable?) {
                    d { t?.message.toString() }
                }

                override fun onResponse(call: Call<Void>?, response: Response<Void>?) {
                    if (response!!.isSuccessful) {
                        d { "addPoint successful" }
                        inputPointHistory(point)
                    }
                }
            })
        }
    }

    fun inputPointHistory(point:String){
        d{"input point history"}
        val preferences = PreferenceManager.getDefaultSharedPreferences(this@MemoryMasterActivity)
        if (preferences.getString(KEYPREFER.UserId, "") != "") {
            d{"point [$point] user_id["+preferences.getString(KEYPREFER.UserId,"")+"]"}
            val addPointHistory = service.addPointHistory("0",preferences.getString(KEYPREFER.UserId,""),
                    "1",point,Date())
            addPointHistory.enqueue(object : Callback<Void> {
                override fun onFailure(call: Call<Void>?, t: Throwable?) {
                    d { t?.message.toString() }
                }

                override fun onResponse(call: Call<Void>?, response: Response<Void>?) {
                    if (response!!.isSuccessful) {
                        d { "addPointHistory successful" }
                    }
                }
            })
        }
    }



    fun showDialogFinish(point: String){
        MaterialDialog.Builder(this@MemoryMasterActivity)
                .typeface(utils.medium,utils.heavy)
                .content(getString(R.string.totalpoint)+" $point "+getString(R.string.points))
                .onPositive { dialog, which -> run {
                    finish()
                }}
                .positiveText(R.string.confirm)
                .show()
    }




    override fun onPause() {
        super.onPause()
        unsubscribe()

    }
}