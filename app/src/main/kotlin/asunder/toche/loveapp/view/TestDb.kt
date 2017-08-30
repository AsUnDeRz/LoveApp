package asunder.toche.loveapp

import android.app.Dialog
import android.app.DialogFragment
import android.app.TimePickerDialog
import android.databinding.ObservableArrayList
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.format.DateFormat
import android.view.View
import android.widget.TimePicker
import com.github.ajalt.timberkt.Timber.d
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.test_db.*
import java.util.*


/**
 * Created by ToCHe on 8/25/2017 AD.
 */
class TestDb:AppCompatActivity(){

    var service : LoveAppService = LoveAppService.create()

    private var _compoSub = CompositeDisposable()
    private val compoSub: CompositeDisposable
        get() {
            if (_compoSub.isDisposed) {
                _compoSub = CompositeDisposable()
            }
            return _compoSub
        }

    protected final fun manageSub(s: Disposable) = compoSub.add(s)

    fun unsubscribe() { compoSub.dispose() }

    val notiMsnList = ObservableArrayList<Model.NotiMessage>()
    val utils = Utils(this@TestDb)

    fun loadTest(){
        manageSub(
                service.getContentInHome("28")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ c -> run {
                            c
                                    .flatMap { it.gender_id }
                                    .forEach { d{"check gender_id in arraylist $it"} }
                            d { "check response [" + c.size + "]" }
                        }},{
                            d { it.message!! }
                        })
        )
    }

    fun loadPointHistoryKnowledge(){
        manageSub(
                service.getPointHistoryKnowledge("1")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ c -> run {
                           val data = ObservableArrayList<Model.RepoPointHistoryKnowledge>().apply {
                               c.forEach {
                                   item -> add(item)
                                   d{"add ["+item.title_eng+"] to array"}
                               }
                           }
                            d { "check response [" + c.size + "]" }
                        }},{
                            d { it.message!! }
                        })
        )
    }
    fun loadPointHistoryGame(){
        manageSub(
                service.getPointHistoryGame("1")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ c -> run {
                            val data = ObservableArrayList<Model.RepoPointHistoryGame>().apply {
                                c.forEach {
                                    item -> add(item)
                                    d{"add ["+item.game_name_eng+"] to array"}
                                }
                            }
                            d { "check response [" + c.size + "]" }
                        }},{
                            d { it.message!! }
                        })
        )
    }
    fun loadPointHistoryRiskMeter(){
        manageSub(
                service.getPointHistoryRiskMeter("1")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ c -> run {
                            val data = ObservableArrayList<Model.RepoPointHistoryRiskMeter>().apply {
                                c.forEach {
                                    item -> add(item)
                                    d{"add ["+item.risk_status+"] to array"}
                                }
                            }
                            d { "check response [" + c.size + "]" }
                        }},{
                            d { it.message!! }
                        })
        )
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.test_db)
        //loadTest()
        loadPointHistoryKnowledge()
        loadPointHistoryGame()
        loadPointHistoryRiskMeter()

        val appDb = AppDatabase(this)



        /*
        appDb.addNotification(Model.Notification("123","title1","MSN1", Date()),0)
        appDb.addNotification(Model.Notification("456","title2","MSN2", Date()),1)
        appDb.addNotification(Model.Notification("789","titadf","MSN3", Date()),2)
        appDb.addNotification(Model.Notification("125","titl1423","MN4", Date()),2)
        appDb.addNotification(Model.Notification("263","titlasf1","MSN5", Date()),1)
        appDb.addNotification(Model.Notification("263","titlasf1","MSN5", Date()),0)
        d{"Insert db test end "}
        */



        val notiList = appDb.getNotificationList()
        d{"Check data in array"}
        for(i in notiList){
            d{"["+i.id+"]["+i.title+"]["+i.message+"]["+i.time.toString()+"]"}
        }

        delete.setOnClickListener {
            //appDb.deleteNotification(Model.Notification(edt_id.text.toString(),"","",Date()))
            val noList = appDb.getNotificationList()
            d{"Check data in array after delete"}
            for(i in noList){
                d{"["+i.id+"]["+i.title+"]["+i.message+"]["+i.time.toString()+"]"}
            }

        }

        add.setOnClickListener {
            showTimePickerDialog(add,"add","0")
        }

        update.setOnClickListener {
            showTimePickerDialog(add,"update",edt_id.text.toString())

        }

        delete_all.setOnClickListener {
            appDb.deleteAllNoti()

            val notiList = appDb.getNotificationList()
            d{"Check data in array"}
            for(i in notiList){
                d{"["+i.id+"]["+i.title+"]["+i.message+"]["+i.time.toString()+"]"}
            }
        }

        get_id.setOnClickListener {
            val test = appDb.getNotiWithState(edt_id.text.toString())
            d{"Check record ["+test.id+"] ["+test.message+"]"}

        }

        waiting.setOnClickListener {
            val notiList = appDb.getNotiWaiting()
            for(i in notiList){
                d{"["+i.id+"]["+i.title+"]["+i.message+"]["+i.time.toString()+"]"}
            }
        }
        missing.setOnClickListener {
            val notiList = appDb.getNotiMissing()
            for(i in notiList){
                d{"["+i.id+"]["+i.title+"]["+i.message+"]["+i.time.toString()+"]"}
            }
        }
        tracked.setOnClickListener {
            val notiList = appDb.getNotiTracked()
            for(i in notiList){
                d{"["+i.id+"]["+i.title+"]["+i.message+"]["+i.time.toString()+"]"}
            }
        }

        counting.setOnClickListener {
            val countNotificaiton = appDb.getCountNoti()
        }
    }

    fun showTimePickerDialog(v: View,state:String,id:String) {
        val newFragment = TimePickerFragment()
        newFragment.state = state
        newFragment.idUpdate = id
        newFragment.show(fragmentManager, "timePicker")
    }

    class TimePickerFragment : DialogFragment(), TimePickerDialog.OnTimeSetListener {

        var callCount = 0
        var state:String =""
        var idUpdate:String =""


        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
        }

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

            val c = Calendar.getInstance()
            val hour = c.get(Calendar.HOUR_OF_DAY)
            val minute = c.get(Calendar.MINUTE)

            return TimePickerDialog(activity, this, hour, minute,
                    DateFormat.is24HourFormat(activity))
        }


        override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {

            val appDb = AppDatabase(view.context)
            if (callCount == 0) {
                // Do something with the time chosen by the user
                val cal = Calendar.getInstance()
                cal.set(Calendar.HOUR_OF_DAY, hourOfDay)
                cal.set(Calendar.MINUTE, minute)
                if(state == "add" && state != "") {
                    appDb.addNotification(Model.Notification("123", "title hOD=[$hourOfDay]", "MSN minute[$minute]", cal.time))
                }
                if(state == "update" && state !="" && idUpdate != ""){
                    //appDb.updateNotification(Model.Notification(idUpdate, "title hOD=[$hourOfDay]", "MSN minute[$minute]", cal.time))
                }
                    //Notification.EventReceiver.setupAlarm(activity,cal)
                d{"calendat set "+cal.time.toString()}
                val notiList = appDb.getNotificationList()
                d{"Check data in array after add"}
                for(i in notiList){
                    d{"["+i.id+"]["+i.title+"]["+i.message+"]["+i.time.toString()+"]"}
                }
            }
            callCount++
        }
    }

    override fun onPause() {
        super.onPause()
        unsubscribe()
    }
}