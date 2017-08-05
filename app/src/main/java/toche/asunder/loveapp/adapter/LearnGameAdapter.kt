package toche.asunder.loveapp.adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import toche.asunder.loveapp.MyApp
import toche.asunder.loveapp.R
import toche.asunder.loveapp.activity.LearnNewActivity

/**
 * Created by admin on 8/5/2017 AD.
 */
class LearnGameAdapter(var context: Context) : RecyclerView.Adapter<LearnGameAdapter.LearnGameHolder>() {

    data class learnGame(var title:String,var points:String)

    var learnGameList = arrayListOf<learnGame>().apply {
        for(i in 0..3) {
            add(learnGame("HIV and Safe sex", "120"))
            add(learnGame("Safe sex and HIV", "110"))
        }
    }

    override fun onBindViewHolder(holder: LearnGameHolder?, position: Int) {
        holder?.bindItems(learnGameList[position])
    }

    override fun getItemCount(): Int {
        return learnGameList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): LearnGameHolder {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.learn_game_item, parent, false)
        return LearnGameHolder(v)    }


    inner class LearnGameHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(items: learnGame) {
            val rootView = itemView.findViewById<View>(R.id.btn_learn_game)
            val titleName = itemView.findViewById<TextView>(R.id.title_name)
            val points = itemView.findViewById<TextView>(R.id.desc_point)

            titleName.typeface = MyApp.typeFace.heavy
            points.typeface = MyApp.typeFace.heavy

            titleName.text = items.title
            points.text = items.points+" Points "


            rootView.setOnClickListener {
                context.startActivity(Intent().setClass(context,LearnNewActivity::class.java))
            }
        }


    }
}