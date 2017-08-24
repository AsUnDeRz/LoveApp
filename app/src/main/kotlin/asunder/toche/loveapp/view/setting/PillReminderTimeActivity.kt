package asunder.toche.loveapp

import android.content.Intent
import android.databinding.ObservableArrayList
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import asunder.toche.loveapp.R
import android.support.v7.widget.LinearLayoutManager
import asunder.toche.loveapp.databinding.PillReminderItemBinding
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.header_logo_blue_back.*
import kotlinx.android.synthetic.main.pill_reminder_time.*
import java.sql.Timestamp
import java.util.*

/**
 * Created by admin on 8/3/2017 AD.
 */
class PillReminderTimeActivity : AppCompatActivity(){

    val pillList = ObservableArrayList<Model.PillReminder>().apply{
        add(Model.PillReminder(123,"Message", Date()))
        add(Model.PillReminder(123,"Message", Date()))
        add(Model.PillReminder(123,"Message", Date()))

    }
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pill_reminder_time)

        Glide.with(this)
                .load(R.drawable.bg_white)
                .into(bg_root)
        btn_back.setOnClickListener {
            onBackPressed()
        }

        btn_add.setOnClickListener {
            startActivity(Intent().setClass(this@PillReminderTimeActivity,PillReminderActivity::class.java))
        }


        rv_reminder_time.layoutManager = LinearLayoutManager(this)
        rv_reminder_time.setHasFixedSize(true)
        rv_reminder_time.adapter = PillReminderAdapter(pillList,false)

    }

    fun PillReminderAdapter(item: List<Any>, stableIds: Boolean): LastAdapter {
        return LastAdapter(item,BR.pillItem,stableIds).type{ item, position ->
            when(item){
                is Model.PillReminder -> PillReminderType
                else -> null
            }
        }
    }


    private val PillReminderType = Type<PillReminderItemBinding>(R.layout.pill_reminder_item)
            .onCreate { println("Created ${it.binding.pillItem} at #${it.adapterPosition}") }
            .onBind { println("Bound ${it.binding.pillItem} at #${it.adapterPosition}") }
            .onRecycle { println("Recycled ${it.binding.pillItem} at #${it.adapterPosition}") }
            .onClick {
                startActivity(Intent().setClass(this@PillReminderTimeActivity,PillReminderActivity::class.java))
            }
            .onLongClick {}



}