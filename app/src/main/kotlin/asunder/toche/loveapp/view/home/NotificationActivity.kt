package asunder.toche.loveapp

import android.databinding.ObservableArrayList
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import asunder.toche.loveapp.R
import asunder.toche.loveapp.databinding.NotificationItemBinding
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.header_logo_blue_back.*
import kotlinx.android.synthetic.main.notification.*
import java.sql.Timestamp
import java.util.*

/**
 * Created by admin on 7/31/2017 AD.
 */
class NotificationActivity : AppCompatActivity() {
   // "Forget to take pill for 2 days","You have appointment with a doctor today","Forget to take pill for 5 days"

    var notiList = ObservableArrayList<Model.Notification>()

    lateinit var appDb :AppDatabase
    lateinit var utils : Utils
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.notification)
        val preferences = PreferenceManager.getDefaultSharedPreferences(this@NotificationActivity)
        appDb = AppDatabase(this)
        utils = Utils(this)



        notiList.apply {
            //check hiv test date
            /*
            if (preferences.getLong(KEYPREFER.HIVTEST, 0L) != 0L) {
                val hivTestDate = preferences.getLong(KEYPREFER.HIVTEST,0L)
                if (hivTestDate > Date().time){
                    add(Model.Notification("0","Hiv test",utils.getDifDate(Date(hivTestDate)),Date(hivTestDate)))
                }
            }
            */
            val data = appDb.getNotiMissing()
            for(noti in data){
                add(Model.Notification(noti.id,noti.title,utils.getDifDate(noti.time),noti.time))
            }
        }
        number_noti.text = notiList.size.toString()
        when {
            notiList.size >= 3 -> loadNotiColor(R.drawable.noti_red_circle)
            notiList.size >= 1 -> loadNotiColor(R.drawable.noti_yellow_circle)
            else -> loadNotiColor(R.drawable.noti_green_circle)
        }

        Glide.with(this)
                .load(R.drawable.bg_white)
                .into(bg_root)

        btn_back.setOnClickListener {
            onBackPressed()
        }

        rv_notification.layoutManager = LinearLayoutManager(this)
        rv_notification.setHasFixedSize(true)
        rv_notification.adapter = NotificationAdapter(notiList,false)

    }

    fun loadNotiColor(image:Int){
        Glide.with(this)
                .load(image)
                .into(noti_color)
    }

    fun NotificationAdapter(item: List<Any>, stableIds: Boolean): LastAdapter {
        return LastAdapter(item,BR.notiItem,stableIds).type{ item, position ->
            when(item){
                is Model.Notification -> NotificationType
                else -> null
            }
        }
    }


    private val NotificationType = Type<NotificationItemBinding>(R.layout.notification_item)
            .onClick {
                AppDatabase(it.itemView.context).deleteNotification(it.binding.notiItem.id)
                notiList.removeAt(it.adapterPosition)
                rv_notification.adapter.notifyItemRemoved(it.adapterPosition)
                rv_notification.adapter.notifyItemRangeChanged(it.adapterPosition, notiList.size)

                number_noti.text = notiList.size.toString()
                when {
                    notiList.size >= 3 -> loadNotiColor(R.drawable.noti_red_circle)
                    notiList.size >= 1 -> loadNotiColor(R.drawable.noti_yellow_circle)
                    else -> loadNotiColor(R.drawable.noti_green_circle)
                }

            }
            .onLongClick {}
}