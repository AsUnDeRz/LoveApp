package asunder.toche.loveapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.stepnewuser.*


/**
 * Created by ToCHe on 10/4/2017 AD.
 */
class StepNewUserActivity: AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.stepnewuser)

        view_gender.setOnInflateListener { stub, inflated ->

        }
        view_gender.inflate()
    }

}