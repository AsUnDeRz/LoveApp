package asunder.toche.loveapp

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import asunder.toche.loveapp.R
import kotlinx.android.synthetic.main.header_logo_white_back.*
import kotlinx.android.synthetic.main.learn_new.*
import kotlinx.android.synthetic.main.learn_new_finish.*
import android.widget.TextView




/**
 * Created by admin on 8/5/2017 AD.
 */
class LearnNewsActivity : AppCompatActivity() {

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.learn_new)

        /*
        (0 until toolbar.childCount)
                .map { toolbar.getChildAt(it) }
                .filterIsInstance<TextView>()
                .forEach {
                    it.typeface = MyApp.typeFace.heavy
                    it.setTextColor(ContextCompat.getColor(this@LearnNewsActivity, R.color.white))
                    it.text = "Learn"
                }
                */
        toolbar_layout.title = "Hiv and Safe Sex #1 "
        toolbar_layout.setExpandedTitleMargin(50,5,5,50)
        toolbar_layout.setExpandedTitleTextAppearance(R.style.CollapsedAppBar)
        toolbar_layout.setCollapsedTitleTextColor(ContextCompat.getColor(this@LearnNewsActivity, R.color.white))
        toolbar_layout.setExpandedTitleColor(ContextCompat.getColor(this@LearnNewsActivity, R.color.white))
        toolbar_layout.setCollapsedTitleTypeface(MyApp.typeFace.heavy)
        toolbar_layout.setExpandedTitleTypeface(MyApp.typeFace.heavy)

        title_header.text = "LEARNS\nAND GAMES"

        btn_back.setOnClickListener {
            onBackPressed()
        }


    }
}