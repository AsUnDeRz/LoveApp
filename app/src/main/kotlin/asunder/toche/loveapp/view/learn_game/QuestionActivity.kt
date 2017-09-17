package asunder.toche.loveapp

import android.content.Intent
import android.databinding.ObservableArrayList
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.widget.LinearLayout
import asunder.toche.loveapp.IndicatorAdapter
import asunder.toche.loveapp.MyApp
import asunder.toche.loveapp.NotificationActivity
import asunder.toche.loveapp.R
import com.afollestad.materialdialogs.MaterialDialog
import com.bumptech.glide.Glide
import com.github.ajalt.timberkt.Timber.d
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.header_logo_white_back.*
import kotlinx.android.synthetic.main.question.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

/**
 * Created by admin on 8/14/2017 AD.
 */
class QuestionActivity: AppCompatActivity() {

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
    var currentQuestion:Int=1
    var currentPosition:Int=0
    var maxQuestionSize:Int=0
    var point:Int =0
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    lateinit var content : String
    var questionList = ArrayList<Model.QuestionYesNo>()
    lateinit var utils: Utils


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.question)
        utils = Utils(this@QuestionActivity)
        //questionList = intent.getParcelableArrayListExtra<Model.QuestionYesNo>(KEYPREFER.CONTENT)
        content = intent.getStringExtra(KEYPREFER.CONTENT)

        Glide.with(this)
                .load(R.drawable.bg_question)
                .into(bg_root)
        btn_yes.typeface = MyApp.typeFace.heavy
        btn_no.typeface = MyApp.typeFace.heavy
        btn_back.setOnClickListener {
            onBackPressed()
        }


        btn_yes.setOnClickListener{
            anwser(true)
        }

        btn_no.setOnClickListener{
            anwser(false)

        }


        rv_question_position.setHasFixedSize(true)
        rv_question_position.layoutManager = LinearLayoutManager(this,LinearLayout.HORIZONTAL,false)
       // rv_question_position.layoutManager = GridLayoutManager(this,maxQuestionSize)
        loadYesNoQuestion()

    }

    fun anwser(anwser:Boolean){
        //
        val question = questionList[currentPosition]
        d{"check anwser"}
        if(anwser == question.answer){
            point += question.point.toInt()
        }
        d{"update total point $point"}
        if(currentPosition < maxQuestionSize-1) {

            //init new question
            currentQuestion++
            currentPosition++
            val data = questionList[currentPosition]
            txt_current_page.text = currentQuestion.toString()
            txt_question.text =utils.txtLocale(data.question_th,data.question_eng)
            rv_question_position.adapter = IndicatorAdapter(maxQuestionSize, currentPosition)
        }else{
            //finish yes no question update point
            d{"finish yes no question"}
            //update point
            if(point > 0){
                val data = questionList[currentPosition]
                addUpdatePoint(point.toString(),data.knowledge_id)
            }else{
                //onBackPressed()
            }
            showDialogFinish(point.toString())



        }


    }

    fun showDialogFinish(point: String){
        MaterialDialog.Builder(this@QuestionActivity)
                .typeface(utils.medium,utils.heavy)
                //.title("Congratulation")
                .content("Your total point is $point Point")
                .onPositive { dialog, which -> run {
                    dialog.dismiss()
                }}
                .dismissListener {
                    onBackPressed()
                }
                .positiveText("Confirm")
                .show()
    }


    fun loadYesNoQuestion(){
        d{"check id ["+content+"]"}
        manageSub(
                service.getYesNoQuestion(content)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ c -> run {
                            val data =ObservableArrayList<Model.QuestionYesNo>().apply {
                                c.forEach {
                                    item -> add(item)
                                    d{"add ["+item.question_eng+"] to array"}
                                }
                            }
                            questionList = data

                            //init first question
                            maxQuestionSize = questionList.size
                            d{"check Maxquestion $maxQuestionSize"}
                            val content = questionList[currentPosition]
                            txt_current_page.text = currentQuestion.toString()
                            txt_question.text =utils.txtLocale(content.question_th,content.question_eng)
                            rv_question_position.adapter = IndicatorAdapter(maxQuestionSize,currentPosition)

                            d { "check response [" + c.size + "]" }
                        }},{
                            d { it.message!! }
                        })
        )
    }

    fun addUpdatePoint(point:String,knowledgeID: String){
        val preferences = PreferenceManager.getDefaultSharedPreferences(this@QuestionActivity)
        if(preferences.getString(KEYPREFER.UserId,"") != "") {
            val addPoint = service.addUserPoint(preferences.getString(KEYPREFER.UserId,""),point)
                        addPoint.enqueue(object : Callback<Void> {
                            override fun onFailure(call: Call<Void>?, t: Throwable?) {
                                d{ t?.message.toString() }
                            }
                            override fun onResponse(call: Call<Void>?, response: Response<Void>?) {
                                if(response!!.isSuccessful){
                                    d{"addPoint Successful"}
                                    inputPointHistory(point,knowledgeID)
                                }
                            }
                        })
            }

        }

    fun inputPointHistory(point:String,knowledgeID:String){
        d{"input point history"}
        val preferences = PreferenceManager.getDefaultSharedPreferences(this@QuestionActivity)
        if (preferences.getString(KEYPREFER.UserId, "") != "") {
            d{"point [$point] user_id["+preferences.getString(KEYPREFER.UserId,"")+"]"}
            val addPointHistory = service.addPointHistory(knowledgeID,preferences.getString(KEYPREFER.UserId,""),
                    "2",point, Date())
            addPointHistory.enqueue(object : Callback<Void> {
                override fun onFailure(call: Call<Void>?, t: Throwable?) {
                    d { t?.message.toString() }
                }

                override fun onResponse(call: Call<Void>?, response: Response<Void>?) {
                    if (response!!.isSuccessful) {
                        d { "addPointHistory successful" }
                        //onBackPressed()
                    }
                }
            })
        }
    }
}