package toche.asunder.loveapp.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import toche.asunder.loveapp.MyApp
import toche.asunder.loveapp.R
import toche.asunder.loveapp.activity.ActivityMain

/**
 * Created by admin on 7/30/2017 AD.
 */
class HomeAdapter(var context: Context) : RecyclerView.Adapter<HomeAdapter.HomeHolder>() {


    override fun onBindViewHolder(holder: HomeHolder?, position: Int) {
        holder?.bindItems(position)
    }

    override fun getItemCount(): Int {
        return 5
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): HomeHolder {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.home_item, parent, false)
        return HomeHolder(v)    }


    inner class HomeHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(items: Int) {
            val cardView = itemView.findViewById<View>(R.id.home_item)
            val title = itemView.findViewById<TextView>(R.id.title)
            val desc = itemView.findViewById<TextView>(R.id.desc)
            title.typeface = MyApp.typeFace.heavy
            desc.typeface = MyApp.typeFace.medium
            cardView.setOnClickListener {
                //Toast.makeText(context,"Click position $items",Toast.LENGTH_SHORT).show()
                context.startActivity(Intent().setClass(context, ActivityMain::class.java))
                val activity = context as Activity
                activity.finish()

            }
        }


    }
}