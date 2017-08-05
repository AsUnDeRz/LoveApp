package toche.asunder.loveapp.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.header_logo_blue.*
import kotlinx.android.synthetic.main.learnandgame.*
import toche.asunder.loveapp.MyApp
import toche.asunder.loveapp.R
import toche.asunder.loveapp.activity.LearnGameMainActivity
import toche.asunder.loveapp.adapter.LearnGameAdapter

/**
 * Created by admin on 8/1/2017 AD.
 */
class LearnAndGameFragment : Fragment() {

    companion object {
        fun newInstance(): LearnAndGameFragment {
            return LearnAndGameFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.learnandgame, container, false)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        title_app.text = "LEARNS\nAND GAMES"
        title_app.typeface = MyApp.typeFace.heavy
        title_game.typeface = MyApp.typeFace.heavy
        desc_game.typeface = MyApp.typeFace.medium
        title_learn.typeface = MyApp.typeFace.heavy
        desc_learn.typeface = MyApp.typeFace.medium


        rv_learn_game.layoutManager = LinearLayoutManager(context)
        rv_learn_game.setHasFixedSize(true)
        rv_learn_game.adapter = LearnGameAdapter(context)

        btn_game.setOnClickListener {
            val data =Intent()
            data.putExtra("title","GAMES")
            data.putExtra("key",1)
            startActivity(data.setClass(context, LearnGameMainActivity::class.java))
        }
        btn_learn.setOnClickListener {
            val data =Intent()
            data.putExtra("title","LEARNS")
            data.putExtra("key",2)
            startActivity(data.setClass(context, LearnGameMainActivity::class.java))
        }

    }
}