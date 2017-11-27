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


    fun PillsAdapter(item: List<Any>, stableIds: Boolean): LastAdapter {
        return LastAdapter(item,BR.pillItem,stableIds).type{ item, position ->
            when(item){
                is Model.PillTracking -> PillType
                else -> null
            }
        }
    }



    private val PillType = Type<PillItemBinding>(R.layout.pill_item)
            .onClick {}
            .onLongClick {}



    private val HomeType = Type<HomeItemBinding>(R.layout.home_item)
            .onClick {}
            .onLongClick {}

    private val ClinicType = Type<ClinicItemBinding>(R.layout.clinic_item)
            .onClick {
                val intent = Intent()
                val context = it.binding.clinicRoot.context
                intent.putExtra(KEYPREFER.CLINICMODEL,it.binding.clinicItem)
                context.startActivity(intent.setClass(context,ClinicInfo::class.java))

            }
            .onLongClick {}







    private val PointType = Type<PointItemBinding>(R.layout.point_item)
            .onClick {}
            .onLongClick {}







}