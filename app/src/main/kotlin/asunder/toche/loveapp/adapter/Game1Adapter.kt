package asunder.toche.loveapp

import android.databinding.ObservableList
import android.os.Handler
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.widget.ImageButton
import android.widget.Toast
import asunder.toche.loveapp.*
import asunder.toche.loveapp.databinding.Game1ItemBinding
import com.github.ajalt.timberkt.Timber.d

/**
 * Created by admin on 8/10/2017 AD.
 */
class Game1Adapter(var data:ObservableList<Model.Game1>,maxPoint:Int): RecyclerView.Adapter<Game1Adapter.CircleHolder>() {

    lateinit var item1 : Game1ItemBinding
    lateinit var item2 : Game1ItemBinding
    lateinit var model1 : Model.Game1
    lateinit var model2 : Model.Game1
    var num :Int=0
    var maxPointInChapter :Int =maxPoint
    var currnetPointInChapter :Int =0

    override fun getItemCount(): Int = data.size

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): CircleHolder {
        val gameBinding = Game1ItemBinding.inflate(LayoutInflater.from(parent?.context), parent, false)
        return CircleHolder(gameBinding)    }

    override fun onBindViewHolder(holder: CircleHolder?, position: Int) {
            holder?.bindItemGame(data[position],position)

    }


    fun openItem(image1:ImageButton,image2:ImageButton){
            image1.visibility = View.INVISIBLE
            image2.visibility = View.VISIBLE
    }
    fun closeItem(image1:ImageButton,image2:ImageButton){
        image1.visibility = View.VISIBLE
        image2.visibility = View.INVISIBLE
    }

    fun actionItem(from:Float,to:Float,p1:ImageButton,p2:ImageButton,isFirst: Boolean) {
        // Find the center of image
        val centerX = p1.width / 2.0f
        val centerY = p1.height / 2.0f

        // Create a new 3D rotation with the supplied parameter
        // The animation listener is used to trigger the next animation
        val rotation = Flip3d(from, to, centerX, centerY)
        rotation.duration = 100
        rotation.fillAfter = true
        rotation.interpolator = AccelerateInterpolator()
        rotation.setAnimationListener(DisplayNextView(isFirst,p1,p2))
        if(isFirst) {
            p1.startAnimation(rotation)
        }else{
            p2.startAnimation(rotation)
        }
    }


    fun checkNum(number: Int,btn:Game1ItemBinding,model:Model.Game1,isFirst: Boolean){
        if(number == 1) {
            item1 = btn
            model1 = model
        }
        if(number == 2){
            item2 = btn
            model2 = model
            num = 0


            if(model1.id == model2.id){
                item1.pos1.isEnabled = false
                item1.pos2.isEnabled = false
                item2.pos1.isEnabled = false
                item2.pos2.isEnabled = false
                //plus point
                currnetPointInChapter+=1
            }else{
                    val pos= Handler()
                    pos.postDelayed({
                        closeItem(item1.pos1,item1.pos2)
                        closeItem(item2.pos1,item2.pos2)
                    },200)


                /*
                if (isFirst) {
                    d{"Is First close it?"}
                    //actionItem(0f,90f,item1.pos1,item1.pos2,isFirst)
                    //actionItem(0f,90f,item2.pos1,item2.pos2,isFirst)

                } else {
                    d{"don't Is First close it?"}
                    val splash = Handler()
                    splash.postDelayed({
                        //actionItem(0f,-90f,item1.pos1,item1.pos2,isFirst)
                        //actionItem(0f,-90f,item2.pos1,item2.pos2,isFirst)
                    },200)
                }
                */

            }

        }
    }



    inner class CircleHolder(val binding:Game1ItemBinding) : RecyclerView.ViewHolder(binding.root) {
        var isFrist = true
        fun bindItemGame(gameItem: Model.Game1,pos:Int) {
            val viewModel = ViewModel.Game1ViewModel(itemView.context,gameItem)
            binding.pos1.setOnClickListener({
                num++
                openItem(binding.pos1,binding.pos2)
                //viewModel.onClickImage(binding.pos1,binding.pos2,isFrist)
                isFrist = !isFrist
                checkNum(num,binding,gameItem,isFrist)

            })



            binding.game1 = viewModel
        }

    }




}