package toche.asunder.loveapp.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import toche.asunder.loveapp.MyApp
import toche.asunder.loveapp.R
import toche.asunder.loveapp.activity.ActivityMain
import toche.asunder.loveapp.activity.GenderActivity
import toche.asunder.loveapp.activity.PasscodeActivity

/**
 * Created by admin on 7/30/2017 AD.
 */
class GenderAdapter(var context: Context) : RecyclerView.Adapter<GenderAdapter.GenderHolder>() {

     var genderList = arrayOf("Men","Women","Gay","Bi","Lady boy")

    override fun onBindViewHolder(holder: GenderHolder?, position: Int) {
        holder?.bindItems(genderList[position])
    }

    override fun getItemCount(): Int {
        return genderList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): GenderHolder {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.gender_item, parent, false)
        return GenderHolder(v)    }


    inner class GenderHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(items: String) {
            val textViewName = itemView.findViewById<View>(R.id.txt_gender) as TextView
            textViewName.typeface = MyApp.typeFace.heavy
            textViewName.text = items
            textViewName.setOnClickListener {
                    context.startActivity(Intent().setClass(context, PasscodeActivity::class.java))
                    val activity = context as Activity
                    activity.finish()

            }
        }


    }
}