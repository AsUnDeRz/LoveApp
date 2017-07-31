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
import toche.asunder.loveapp.activity.ActivityMain
import toche.asunder.loveapp.activity.ReminderActivity

/**
 * Created by admin on 7/31/2017 AD.
 */
class NotiAdapter(var context: Context) : RecyclerView.Adapter<NotiAdapter.NotiHolder>() {

    var notiList = arrayOf("Forget to take pill for 2 days","You have appointment with a doctor today","Forget to take pill for 5 days",
            "Forget to take pill for 2 days","You have appointment with a doctor today","Forget to take pill for 5 days")


    override fun onBindViewHolder(holder: NotiHolder?, position: Int) {
        holder?.bindItems(notiList[position])
    }

    override fun getItemCount(): Int {
        return notiList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): NotiHolder {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.notification_item, parent, false)
        return NotiHolder(v)    }


    inner class NotiHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(items: String) {
            val notiItem = itemView.findViewById<View>(R.id.noti_btn)
            val txtNoti = itemView.findViewById<TextView>(R.id.txt_noti)
            txtNoti.text = items
            txtNoti.typeface = MyApp.typeFace.heavy

            notiItem.setOnClickListener {
                context.startActivity(Intent().setClass(context, ReminderActivity::class.java))
                val activity = context as Activity
                activity.finish()

            }

        }


    }
}