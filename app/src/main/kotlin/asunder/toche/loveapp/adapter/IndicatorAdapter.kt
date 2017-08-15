package asunder.toche.loveapp

import android.databinding.ObservableList
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.View.inflate
import android.view.ViewGroup
import android.widget.Toast
import asunder.toche.loveapp.MemoryMaster2Activity.Companion.data
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.question_item.view.*
import java.util.zip.Inflater
import asunder.toche.loveapp.R.id.imageView
import android.widget.LinearLayout



/**
 * Created by admin on 8/14/2017 AD.
 */
class IndicatorAdapter(var size:Int,var select:Int): RecyclerView.Adapter<IndicatorAdapter.IndicatorHolder>() {


    override fun getItemCount(): Int = size

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): IndicatorHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.question_item,null)
        return IndicatorHolder(view)
    }

    override fun onBindViewHolder(holder: IndicatorHolder?, position: Int) {
        holder?.bindingItem(position)

    }

    inner class IndicatorHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        fun bindingItem(position:Int){
            if(select == position){
                Glide.with(itemView.context)
                        .load(R.drawable.dot_active)
                        .into(itemView.icon_circle)

            }else{

            }


        }


    }
}