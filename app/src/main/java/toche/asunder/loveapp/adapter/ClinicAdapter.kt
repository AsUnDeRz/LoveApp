package toche.asunder.loveapp.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import org.w3c.dom.Text
import toche.asunder.loveapp.MyApp
import toche.asunder.loveapp.R
import toche.asunder.loveapp.activity.ClinicInfo
import toche.asunder.loveapp.activity.PasscodeActivity

/**
 * Created by admin on 8/2/2017 AD.
 */
class ClinicAdapter(var context: Context) : RecyclerView.Adapter<ClinicAdapter.ClinicHolder>() {

    data class clinic(var name:String,var testname:String,var worktime:String,var icon:Int)

    var clinicList = arrayListOf<clinic>().apply {
        for(i in 0..3) {
            add(clinic("Bangkok Clinic", "HIV test,STD PrEP", "open 10.00 am - 19.00 pm", R.drawable.clinic_img))
            add(clinic("HIV Center", "HIV test,STD PrEP", "open 10.00 am - 19.00 pm", R.drawable.clinic_img))
        }
    }

    override fun onBindViewHolder(holder: ClinicHolder?, position: Int) {
        holder?.bindItems(clinicList[position])
    }

    override fun getItemCount(): Int {
        return clinicList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ClinicHolder {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.clinic_item, parent, false)
        return ClinicHolder(v)    }


    inner class ClinicHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(items: clinic) {
            val rootView = itemView.findViewById<View>(R.id.clinic_root)
            val clinicName = itemView.findViewById<TextView>(R.id.clinic_name)
            val testName = itemView.findViewById<TextView>(R.id.test_name)
            val workTime = itemView.findViewById<TextView>(R.id.worktime)

            clinicName.typeface = MyApp.typeFace.heavy
            testName.typeface = MyApp.typeFace.medium
            workTime.typeface = MyApp.typeFace.medium

            clinicName.text = items.name
            testName.text = items.testname
            workTime.text = items.worktime


            rootView.setOnClickListener {
                context.startActivity(Intent().setClass(context, ClinicInfo::class.java))
            }
        }


    }
}