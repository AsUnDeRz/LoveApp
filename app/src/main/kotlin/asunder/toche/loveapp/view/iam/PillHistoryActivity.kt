package asunder.toche.loveapp

import android.content.SharedPreferences
import android.databinding.ObservableArrayList
import android.databinding.ObservableList
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.github.ajalt.timberkt.Timber.d
import kotlinx.android.synthetic.main.header_logo_blue_back.*
import kotlinx.android.synthetic.main.pill_history.*
import java.util.*


/**
 * Created by ToCHe on 9/28/2017 AD.
 */
class PillHistoryActivity : AppCompatActivity(){



    lateinit var utils :Utils
    lateinit var prefer : SharedPreferences
    lateinit var appDb : AppDatabase
    val pillList = ObservableArrayList<Model.PillTracking>()
    val dataDate = ArrayList<Long>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pill_history)
        utils = Utils(this@PillHistoryActivity)
        prefer = PreferenceManager.getDefaultSharedPreferences(this@PillHistoryActivity)
        appDb = AppDatabase(this@PillHistoryActivity)

        Glide.with(this@PillHistoryActivity)
                .load(R.drawable.bg_white)
                .into(bg_root)

        loadPillHistory()

        rv_pillhistory.layoutManager = LinearLayoutManager(this)
        rv_pillhistory.setHasFixedSize(true)


        btn_back.setOnClickListener {
            onBackPressed()
        }
    }



    fun loadPillHistory(){
        val data = ObservableArrayList<Model.PillTracking>()
        appDb.getNotiMissing().forEach {
            item ->
            data.add(Model.PillTracking(item.id.toLong(),item.time.time,getString(R.string.no)))
            dataDate.add(item.time.time)
        }
        appDb.getNotiTracked().forEach {
            item ->
            data.add(Model.PillTracking(item.id.toLong(),item.time.time,getString(R.string.yes)))
            dataDate.add(item.time.time)
        }

        rv_pillhistory.adapter = MasterAdapter.PillsAdapter(sortDate(data,dataDate),false)


    }

    fun sortDate(master:ObservableArrayList<Model.PillTracking>,dataLong:ArrayList<Long>):ObservableArrayList<Model.PillTracking>{
        Collections.sort(dataLong)

        var result = ObservableArrayList<Model.PillTracking>()

        for (long in dataLong){
            for(data in master){
                if(data.date == long){
                    d{"add [$long] ["+data.id+"]["+data.status+"] to result"}
                    result.add(data)
                }
            }
        }


        return result
    }
}