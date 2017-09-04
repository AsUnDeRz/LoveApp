package asunder.toche.loveapp

import android.content.Context
import android.content.Intent
import asunder.toche.loveapp.BR
import asunder.toche.loveapp.R
import asunder.toche.loveapp.databinding.*

/**
 * Created by admin on 8/7/2017 AD.
 */
object MasterAdapter{


    fun ClinicAdapter(item: List<Any>, stableIds: Boolean): LastAdapter {
        return LastAdapter(item,BR.clinicItem,stableIds).type{ item, position ->
            when(item){
                is Model.Clinic -> ClinicType
                else -> null
            }
        }
    }









    fun PointsAdapter(item: List<Any>, stableIds: Boolean): LastAdapter {
        return LastAdapter(item,BR.pointItem,stableIds).type{ item, position ->
            when(item){
                is Model.Point -> PointType
                else -> null
            }
        }
    }





    private val HomeType = Type<HomeItemBinding>(R.layout.home_item)
            .onCreate { println("Created ${it.binding.homeItem} at #${it.adapterPosition}") }
            .onBind { println("Bound ${it.binding.homeItem} at #${it.adapterPosition}") }
            .onRecycle { println("Recycled ${it.binding.homeItem} at #${it.adapterPosition}") }
            .onClick {}
            .onLongClick {}

    private val ClinicType = Type<ClinicItemBinding>(R.layout.clinic_item)
            .onCreate { println("Created ${it.binding.clinicItem} at #${it.adapterPosition}") }
            .onBind { println("Bound ${it.binding.clinicItem} at #${it.adapterPosition}") }
            .onRecycle { println("Recycled ${it.binding.clinicItem} at #${it.adapterPosition}") }
            .onClick {
                val intent = Intent()
                val context = it.binding.clinicRoot.context
                intent.putExtra(KEYPREFER.CLINICMODEL,it.binding.clinicItem)
                context.startActivity(intent.setClass(context,ClinicInfo::class.java))

            }
            .onLongClick {}







    private val PointType = Type<PointItemBinding>(R.layout.point_item)
            .onCreate { println("Created ${it.binding.pointItem} at #${it.adapterPosition}") }
            .onBind { println("Bound ${it.binding.pointItem} at #${it.adapterPosition}") }
            .onRecycle { println("Recycled ${it.binding.pointItem} at #${it.adapterPosition}") }
            .onClick {}
            .onLongClick {}







}