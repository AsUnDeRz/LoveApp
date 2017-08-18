package asunder.toche.loveapp

import android.databinding.ObservableArrayList
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.header_logo_white_back.*
import kotlinx.android.synthetic.main.noti_message.*

/**
 * Created by admin on 8/13/2017 AD.
 */
class NotiMessageActivity:AppCompatActivity(){

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    val data = ObservableArrayList<Model.NotiMessage>().apply {
        for(i in 1..4) {
            add(Model.NotiMessage(123213, "Take a pill please! $i"))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.noti_message)

        Glide.with(this)
                .load(R.drawable.bg_white)
                .into(bg_root)
        btn_back.setOnClickListener {
            onBackPressed()
        }

        rv_noti_msn.layoutManager = LinearLayoutManager(this@NotiMessageActivity)
        rv_noti_msn.adapter = MasterAdapter.NotiMessageAdapter(data,false)
        rv_noti_msn.setHasFixedSize(true)
    }
}