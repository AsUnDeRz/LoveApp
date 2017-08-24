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

    fun HomeAdapter(item: List<Any>, stableIds: Boolean): LastAdapter {
        return LastAdapter(item,BR.homeItem,stableIds).type{ item, position ->
            when(item){
                is Model.HomeContent -> HomeType
                else -> null
            }
        }
    }

    fun ClinicAdapter(item: List<Any>, stableIds: Boolean): LastAdapter {
        return LastAdapter(item,BR.clinicItem,stableIds).type{ item, position ->
            when(item){
                is Model.Clinic -> ClinicType
                else -> null
            }
        }
    }







    fun NotificationAdapter(item: List<Any>, stableIds: Boolean): LastAdapter {
        return LastAdapter(item,BR.notiItem,stableIds).type{ item, position ->
            when(item){
                is Model.Notification -> NotificationType
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

    fun NotiMessageAdapter(item: List<Any>, stableIds: Boolean): LastAdapter {
        return LastAdapter(item,BR.msnItem,stableIds).type{ item, position ->
            when(item){
                is Model.NotiMessage -> NotiMessageType
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
                val context = it.binding.clinicRoot.context
                context.startActivity(Intent().setClass(context,ClinicInfo::class.java))

            }
            .onLongClick {}





    private val NotificationType = Type<NotificationItemBinding>(R.layout.notification_item)
            .onCreate { println("Created ${it.binding.notiItem} at #${it.adapterPosition}") }
            .onBind { println("Bound ${it.binding.notiItem} at #${it.adapterPosition}") }
            .onRecycle { println("Recycled ${it.binding.notiItem} at #${it.adapterPosition}") }
            .onClick {}
            .onLongClick {}


    private val PointType = Type<PointItemBinding>(R.layout.point_item)
            .onCreate { println("Created ${it.binding.pointItem} at #${it.adapterPosition}") }
            .onBind { println("Bound ${it.binding.pointItem} at #${it.adapterPosition}") }
            .onRecycle { println("Recycled ${it.binding.pointItem} at #${it.adapterPosition}") }
            .onClick {}
            .onLongClick {}



    private val NotiMessageType = Type<NotiMessageItemBinding>(R.layout.noti_message_item)
            .onCreate { println("Created ${it.binding.msnItem} at #${it.adapterPosition}") }
            .onBind { println("Bound ${it.binding.msnItem} at #${it.adapterPosition}") }
            .onRecycle { println("Recycled ${it.binding.msnItem} at #${it.adapterPosition}") }
            .onClick {}
            .onLongClick {}



}