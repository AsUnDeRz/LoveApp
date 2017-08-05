package toche.asunder.loveapp.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import toche.asunder.loveapp.MyApp
import toche.asunder.loveapp.R

/**
 * Created by admin on 8/3/2017 AD.
 */
class PointHistriesAdapter(var context: Context) : RecyclerView.Adapter<PointHistriesAdapter.PointHisHolder>() {


    data class pointModel(val date:String,val point:String,val title:String)
    var pillTimeList = arrayListOf<pointModel>().apply {
        for(i in 1..4) {
            add(pointModel("2017 - 04 - 22", "3"+i+"0","HIV & Safe Sex 1"))
        }
    }

    override fun onBindViewHolder(holder: PointHisHolder?, position: Int) {
        holder?.bindItems(pillTimeList[position])
    }

    override fun getItemCount(): Int {
        return pillTimeList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): PointHisHolder {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.point_item, parent, false)
        return PointHisHolder(v)    }


    inner class PointHisHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(items: pointModel) {
            val pointItem = itemView.findViewById<View>(R.id.btn_point)
            val txtDate = itemView.findViewById<TextView>(R.id.txt_date)
            val txtTitle = itemView.findViewById<TextView>(R.id.txt_title)
            val txtPoint = itemView.findViewById<TextView>(R.id.txt_points)
            val txtTitlePoint = itemView.findViewById<TextView>(R.id.txt_name_point)
            txtDate.text = items.date
            txtPoint.text = items.point
            txtTitle.text = items.title
            txtDate.typeface = MyApp.typeFace.medium
            txtTitle.typeface = MyApp.typeFace.heavy
            txtPoint.typeface = MyApp.typeFace.heavy
            txtTitlePoint.typeface = MyApp.typeFace.medium



        }


    }
}