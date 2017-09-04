package asunder.toche.loveapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.databinding.ObservableArrayList
import android.os.Bundle
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.app.AppCompatActivity
import asunder.toche.loveapp.R
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.InputType
import android.widget.Toast
import asunder.toche.loveapp.databinding.PillReminderItemBinding
import com.afollestad.materialdialogs.MaterialDialog
import com.bumptech.glide.Glide
import com.github.ajalt.timberkt.Timber.d
import kotlinx.android.synthetic.main.header_logo_blue_back.*
import kotlinx.android.synthetic.main.pill_reminder_time.*
import java.sql.Timestamp
import java.util.*

/**
 * Created by admin on 8/3/2017 AD.
 */
class PillReminderTimeActivity : AppCompatActivity(){



    companion object {
        private val PillReminderType = Type<PillReminderItemBinding>(R.layout.pill_reminder_item)
                .onCreate { println("Created ${it.binding.pillItem} at #${it.adapterPosition}") }
                .onBind { println("Bound ${it.binding.pillItem} at #${it.adapterPosition}") }
                .onRecycle { println("Recycled ${it.binding.pillItem} at #${it.adapterPosition}") }
                .onClick {
                    val data =Intent()
                    data.putExtra(KEYPREFER.isFirst,false)
                    data.putExtra(KEYPREFER.NOTIID,it.binding.getPillItem().id.toString())
                    data.putExtra(KEYPREFER.POSITION,it.adapterPosition)
                    it.itemView.context.startActivity(data.setClass(it.itemView.context,PillReminderActivity::class.java))
                }
                .onLongClick {
                    val utils = Utils(it.itemView.context)
                    MaterialDialog.Builder(it.itemView.context)
                            .typeface(utils.medium,utils.heavy)
                            .content("Do you want to delete your alerts?")
                            .negativeText("Cancel")
                            .onPositive { dialog, which -> run {
                                // remove notification remove alarm remove data in array but don't remove in sqlite
                                AppDatabase(it.itemView.context).deleteNotification(it.binding.getPillItem().id.toString())
                                Notification.EventReceiver.cancelAlarm(it.itemView.context,it.binding.getPillItem().id.toInt())
                                Notification.EventReceiver.getDeleteIntent(it.itemView.context,it.binding.getPillItem().id.toInt())
                                pillList.removeAt(it.adapterPosition)
                                rvReminder.adapter.notifyItemRemoved(it.adapterPosition)
                                rvReminder.adapter.notifyItemRangeChanged(it.adapterPosition, pillList.size)
                                dialog.dismiss()
                            }}
                            .positiveText("Confirm")
                            .onNegative { dialog, which -> run {
                                dialog.dismiss()
                            }}
                            .show()
                }
        fun PillReminderAdapter(item: List<Any>, stableIds: Boolean): LastAdapter {
            return LastAdapter(item,BR.pillItem,stableIds).type{ item, position ->
                when(item){
                    is Model.PillReminder -> PillReminderType
                    else -> null
                }
            }
        }


        var pillList = ObservableArrayList<Model.PillReminder>()
        @SuppressLint("StaticFieldLeak")
        lateinit var rvReminder :RecyclerView
        fun add(data:Model.PillReminder){
            pillList.add(data)
            rvReminder.adapter = PillReminderAdapter(pillList,false)
        }
        fun update(data:Model.PillReminder,position:Int){
            pillList[position] = data
            rvReminder.adapter = PillReminderAdapter(pillList,false)
        }
    }

    lateinit var appDb :AppDatabase

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pill_reminder_time)
        rvReminder = findViewById(R.id.rv_reminder_time)
        appDb = AppDatabase(this)

        val data =ObservableArrayList<Model.PillReminder>().apply {
            val data =appDb.getNotiWaiting()
            for(i in data){
                add(Model.PillReminder(i.id.toLong(),i.message,i.time))
            }
        }
        pillList = data

        Glide.with(this)
                .load(R.drawable.bg_white)
                .into(bg_root)
        btn_back.setOnClickListener {
            onBackPressed()
        }

        btn_add.setOnClickListener {
            val data =Intent()
            data.putExtra(KEYPREFER.isFirst,true)
            startActivity(data.setClass(this@PillReminderTimeActivity,PillReminderActivity::class.java))
        }


        rvReminder.layoutManager = LinearLayoutManager(this)
        rvReminder.setHasFixedSize(true)
        rvReminder.adapter = PillReminderAdapter(pillList,false)

    }







}