package asunder.toche.loveapp

import android.annotation.SuppressLint
import android.content.Context
import android.databinding.ObservableList
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.AdapterDataObserver
import android.widget.ImageView
import android.widget.TextView
import asunder.toche.loveapp.*
import asunder.toche.loveapp.R.id.icon_random
import com.afollestad.materialdialogs.MaterialDialog
import com.bumptech.glide.Glide
import com.github.ajalt.timberkt.Timber.d
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.header_logo_blue_back.*
import kotlinx.android.synthetic.main.memory_master_2.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

/**
 * Created by admin on 8/13/2017 AD.
 */
class MemoryMaster2Activity:AppCompatActivity() {

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


    }

     fun initRandomPick(randomPick:ObservableList<Model.Game1>){
        if(posPick <= randomPick.size-1) {
            Glide.with(this@MemoryMaster2Activity)
                    .load(randomPick[posPick].icon)
                    .into(iconGame)
        }
    }



    fun updatePoint(point:String) {
        d{"Update point user"}
        val preferences = PreferenceManager.getDefaultSharedPreferences(this@MemoryMaster2Activity)
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
        val preferences = PreferenceManager.getDefaultSharedPreferences(this@MemoryMaster2Activity)
        if (preferences.getString(KEYPREFER.UserId, "") != "") {
            d{"point [$point] user_id["+preferences.getString(KEYPREFER.UserId,"")+"]"}
            val addPointHistory = service.addPointHistory("1",preferences.getString(KEYPREFER.UserId,""),
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
        MaterialDialog.Builder(this@MemoryMaster2Activity)
                .typeface(utils.medium,utils.heavy)
                //.title("Congratulation")
                .content(getString(R.string.totalpoint)+" $point "+getString(R.string.points))
                .onPositive { dialog, which -> run {
                    finish()
                }}
                .positiveText(R.string.confirm)
                .show()
    }

    override fun onResume() {
        super.onResume()
        Handler().postDelayed({
            countDown = object : CountDownTimer(30000,1000){
                override fun onFinish() {
                    showDialogFinish(posPick.toString())
                    updatePoint(posPick.toString())
                    txt_time.text = "Finish!"
                    isTimeRunner = false
                    posPick = 0
                }

                override fun onTick(millisUntilFinished: Long) {
                    txt_time.text = (millisUntilFinished/1000).toString() +" Sec"
                    isTimeRunner = true
                }

            }.start()
        },1500)

    }



    override fun onPause() {
        super.onPause()
        unsubscribe()

    }
}