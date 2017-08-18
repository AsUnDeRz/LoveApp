package asunder.toche.loveapp

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.widget.LinearLayout
import asunder.toche.loveapp.IndicatorAdapter
import asunder.toche.loveapp.MyApp
import asunder.toche.loveapp.NotificationActivity
import asunder.toche.loveapp.R
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.header_logo_white_back.*
import kotlinx.android.synthetic.main.question.*

/**
 * Created by admin on 8/14/2017 AD.
 */
class QuestionActivity: AppCompatActivity() {

    var currentQuestion:Int=1
    var currentPosition:Int=0
    var maxQuestionSize:Int=5
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.question)

        Glide.with(this)
                .load(R.drawable.bg_question)
                .into(bg_root)
        btn_yes.typeface = MyApp.typeFace.heavy
        btn_no.typeface = MyApp.typeFace.heavy
        btn_back.setOnClickListener {
            onBackPressed()
        }

        txt_current_page.text ="$currentQuestion"

        btn_yes.setOnClickListener{
            if(currentPosition < maxQuestionSize-1) {
                currentQuestion++
                currentPosition++
                txt_current_page.text ="$currentQuestion"
                rv_question_position.adapter = IndicatorAdapter(maxQuestionSize, currentPosition)
            }

        }


        rv_question_position.setHasFixedSize(true)
        rv_question_position.layoutManager = LinearLayoutManager(this,LinearLayout.HORIZONTAL,false)
       // rv_question_position.layoutManager = GridLayoutManager(this,maxQuestionSize)
        rv_question_position.adapter = IndicatorAdapter(maxQuestionSize,currentPosition)

    }
}