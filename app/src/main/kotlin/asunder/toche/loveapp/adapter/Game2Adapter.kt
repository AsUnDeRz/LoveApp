package asunder.toche.loveapp

import android.content.Context
import android.content.res.Resources
import android.databinding.ObservableList
import android.os.Handler
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.widget.ImageButton
import android.widget.Toast
import asunder.toche.loveapp.databinding.Game2ItemBinding
import java.util.*

/**
 * Created by admin on 8/13/2017 AD.
 */
class Game2Adapter(var data: ObservableList<Model.Game1>, maxPoint:Int): RecyclerView.Adapter<Game2Adapter.ItemHolder>() {


    override fun getItemCount(): Int = data.size

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ItemHolder {
        val gameBinding = Game2ItemBinding.inflate(LayoutInflater.from(parent?.context), parent, false)
        return ItemHolder(gameBinding)
    }

    override fun onBindViewHolder(holder: ItemHolder?, position: Int) {
        holder?.bindItemGame(data[position], position)

    }

    fun randomPosition(){
        val rnd = Random()
        for (i in data.size - 1 downTo 1) {
            val index = rnd.nextInt(i + 1)
            // Simple swap
            val a = data[index]
            data[index] = data[i]
            data[i] = a
        }
        notifyDataSetChanged()
    }


    inner class ItemHolder(val binding: Game2ItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindItemGame(gameItem: Model.Game1, pos: Int) {
            val viewModel = ViewModel.Game2ViewModel(itemView.context, gameItem)
            binding.iconGame2.setOnClickListener {
                if(gameItem.icon == MemoryMaster2Activity.randomPick[MemoryMaster2Activity.posPick].icon && MemoryMaster2Activity.isTimeRunner){
                    MemoryMaster2Activity.posPick++
                    MemoryMaster2Activity.nextPic(itemView.context)
                    //Toast.makeText(itemView.context,"Yes i'm",Toast.LENGTH_LONG).show()
                }
            }
            binding.game2 = viewModel
        }

    }
}