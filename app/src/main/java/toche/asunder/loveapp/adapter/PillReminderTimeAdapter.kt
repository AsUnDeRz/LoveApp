package toche.asunder.loveapp.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import toche.asunder.loveapp.MyApp
import toche.asunder.loveapp.R
import toche.asunder.loveapp.activity.ReminderActivity

/**
 * Created by admin on 8/3/2017 AD.
 */
class PillReminderTimeAdapter(var context: Context) : RecyclerView.Adapter<PillReminderTimeAdapter.ReminderTimeHolder>() {


    data class pillTime(val time:String,val values:String)
    var pillTimeList = arrayListOf<pillTime>().apply {
        for(i in 1..4) {
            add(pillTime("1$i.30 pm", "$i pills"))
        }
    }

    override fun onBindViewHolder(holder: ReminderTimeHolder?, position: Int) {
        holder?.bindItems(pillTimeList[position])
    }

    override fun getItemCount(): Int {
        return pillTimeList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ReminderTimeHolder {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.pill_reminder_item, parent, false)
        return ReminderTimeHolder(v)    }


    inner class ReminderTimeHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(items: pillTime) {
            val pillItem = itemView.findViewById<View>(R.id.pill_item)
            val txtTime = itemView.findViewById<TextView>(R.id.txt_time)
            val txtPill = itemView.findViewById<TextView>(R.id.txt_pill)
            txtTime.text = items.time
            txtPill.text = items.values
            txtPill.typeface = MyApp.typeFace.heavy
            txtTime.typeface = MyApp.typeFace.heavy



        }


    }
}