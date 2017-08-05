package toche.asunder.loveapp.adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView
import toche.asunder.loveapp.MyApp
import toche.asunder.loveapp.R
import toche.asunder.loveapp.activity.MemoryMasterActivity

/**
 * Created by admin on 8/5/2017 AD.
 */
class LearnGameListAdapter(var context: Context,val key:Int) : RecyclerView.Adapter<LearnGameListAdapter.ModelHolder>() {

    data class model(val icon:Int,val title:String,val points:String,val percent:String)
    var LearnGameList = arrayListOf<model>()

    init {
        when(key){
            1 ->{
                LearnGameList = arrayListOf<model>().apply {
                    for(i in 1..3) {
                        add(model(R.drawable.clinic_img, "Memory Master #$i", "1"+i+"0", "20"))
                        add(model(R.drawable.clinic_img, "Co-op Game", "100", "10"))

                    }
                }
            }
            2 ->{
                LearnGameList = arrayListOf<model>().apply {
                    for(i in 1..3) {
                        add(model(R.drawable.clinic_img, "HIV Safe Sex #$i", "1"+i+"0", "20"))
                        add(model(R.drawable.clinic_img, "Safe Sex", "100", "10"))

                    }
                }
            }
        }
    }


    override fun onBindViewHolder(holder: ModelHolder?, position: Int) {
        holder?.bindItems(LearnGameList[position])
    }

    override fun getItemCount(): Int {
        return LearnGameList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ModelHolder {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.game_item, parent, false)
        return ModelHolder(v)    }


    inner class ModelHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(items: model) {
            val GameItem = itemView.findViewById<View>(R.id.btn_game)
            val titleGame = itemView.findViewById<TextView>(R.id.title_game)
            val txtPoints = itemView.findViewById<TextView>(R.id.txt_points)
            val txtPercent = itemView.findViewById<TextView>(R.id.txt_percent)
            val iconGame = itemView.findViewById<CircleImageView>(R.id.icon_game)


            titleGame.typeface = MyApp.typeFace.heavy
            txtPercent.typeface = MyApp.typeFace.heavy
            txtPoints.typeface = MyApp.typeFace.heavy

            titleGame.text = items.title
            txtPoints.text = items.points+" Points"
            txtPercent.text = items.percent+"% Done"
            Glide.with(context)
                    .load(items.icon)
                    .into(iconGame)

            GameItem.setOnClickListener {
                when(key){
                    1 ->{
                        context.startActivity(Intent().setClass(context, MemoryMasterActivity::class.java))
                    }
                    2 ->{

                    }
                }
            }


        }


    }
}