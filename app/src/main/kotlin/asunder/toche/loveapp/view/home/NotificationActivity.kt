package asunder.toche.loveapp

import android.databinding.ObservableArrayList
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import asunder.toche.loveapp.R
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

    var notiList = ObservableArrayList<Model.Notification>().apply {
        add(Model.Notification(1,"Forget to take pill for 2 days","utils.Test", Date()))
        add(Model.Notification(1,"Forget to take pill for 2 days","utils.Test", Date()))
        add(Model.Notification(1,"Forget to take pill for 2 days","utils.Test", Date()))
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.notification)

        Glide.with(this)
                .load(R.drawable.bg_white)
                .into(bg_root)

        btn_back.setOnClickListener {
            onBackPressed()
        }

        rv_notification.layoutManager = LinearLayoutManager(this)
        rv_notification.setHasFixedSize(true)
        rv_notification.adapter = MasterAdapter.NotificationAdapter(notiList,false)

    }
}