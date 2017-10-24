package asunder.toche.loveapp

import android.content.Intent
import android.content.SharedPreferences
import android.databinding.ObservableArrayList
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.widget.TextView
import asunder.toche.loveapp.R
import asunder.toche.loveapp.databinding.LearnGameItemBinding
import com.bumptech.glide.Glide
import com.github.ajalt.timberkt.Timber
import com.github.ajalt.timberkt.Timber.d
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.header_logo_white_back.*
import kotlinx.android.synthetic.main.learn_game_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


/**
 * Created by admin on 8/5/2017 AD.
 */
class LearnGameMainActivity: AppCompatActivity(){

    var LearnGameList = ObservableArrayList<Model.LearnGameContent>()
    var contentList = ObservableArrayList<Model.RepositoryKnowledge>()
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

    lateinit var utils:Utils
    lateinit var preference : SharedPreferences
    lateinit var appDb:AppDatabase



    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.learn_game_list)
        appDb = AppDatabase(this@LearnGameMainActivity)
        utils = Utils(this@LearnGameMainActivity)
        preference = PreferenceManager.getDefaultSharedPreferences(this@LearnGameMainActivity)
        title_header.text = intent.extras.getString("title")
        Glide.with(this)
                .load(R.drawable.bg_blue)
                .into(bg_root)

        when(intent.extras.getInt("key")){
            1 ->{
                /*
                LearnGameList.apply {
                    for(i in 1..2) {
                        add(Model.LearnGameContent(R.drawable.clinic_img, "Memory Master #$i", "100 Point", "20% Done",R.drawable.clinic_img))
                    }
                }
                */
                rv_learngame.adapter = LearnGameAdapter(LearnGameList,false)
                loadContentGame()
            }
            2 ->{
                //loadKnowledge in group
                loadKnowLedgeInGroup(preference.getString(KEYPREFER.GENDER,"1"),intent.extras.getInt("id").toString(),appDb)
            }
        }


        rv_learngame.layoutManager = LinearLayoutManager(this@LearnGameMainActivity)
        rv_learngame.setHasFixedSize(true)
        //rv_learngame.adapter = LearnGameListAdapter(this@LearnGameMainActivity,intent.extras.getInt("key"))
        //set title
        btn_back.setOnClickListener {
            onBackPressed()
        }



    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == KEYPREFER.LEARNGAME && data != null){
            Timber.d { "result for knowledge content " }
            val point = data.getStringExtra(KEYPREFER.POINT)
            val contentID = data.getStringExtra(KEYPREFER.CONTENT)
            val userID = preference.getString(KEYPREFER.UserId,"")
            d{"check result $point   $contentID"}
            //addUpdatePoint(point,contentID,userID)
        }
    }

    fun LearnGameAdapter(item: List<Any>, stableIds: Boolean): LastAdapter {
        return LastAdapter(item,BR.learnGameItem,stableIds).type{ item, position ->
            when(item){
                is Model.LearnGameContent -> LearnGameType
                else -> null
            }
        }
    }

    private val LearnGameType = Type<LearnGameItemBinding>(R.layout.learn_game_item)
            .onCreate { println("Created ${it.binding.learnGameItem} at #${it.adapterPosition}") }
            .onBind { println("Bound ${it.binding.learnGameItem} at #${it.adapterPosition}") }
            .onRecycle { println("Recycled ${it.binding.learnGameItem} at #${it.adapterPosition}") }
            .onClick {
                it.binding.titleGame as TextView
                when(intent.extras.getInt("key")){
                    1 ->{
                        if(it.binding.learnGameItem.id  == 0){
                            startActivity(Intent().setClass(this,MemoryMasterActivity::class.java))
                        }else if(it.binding.learnGameItem.id == 1){
                            startActivity(Intent().setClass(this,MemoryMaster2Activity::class.java))
                        }else{
                            val data = Intent()
                            data.putExtra(KEYPREFER.CONTENT,it.binding.learnGameItem.id.toString())
                            data.putExtra(KEYPREFER.TYPE,KEYPREFER.GAME)
                            startActivity(data.setClass(this@LearnGameMainActivity, QuestionActivity::class.java))
                        }
                    }
                    2 ->{
                        val item = contentList[it.adapterPosition]
                        val model = Model.RepositoryKnowledge(item.id,item.group_id,item.title_th,item.title_eng,item.content_th,item.content_eng,
                                "image",item.point, arrayListOf("2","3"),"1",item.content_th_long,item.content_eng_long,item.link)
                        var data = Intent()
                        data.putExtra(KEYPREFER.CONTENT,model)
                        startActivityForResult(data.setClass(this@LearnGameMainActivity,LearnNewsActivity::class.java),KEYPREFER.LEARNGAME)
                    }
                }

            }
            .onLongClick {}


    fun loadContentGame(){
        manageSub(
                service.getGames()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ c ->
                            run {
                                LearnGameList.apply {
                                    c.forEach { item ->
                                        add(Model.LearnGameContent(
                                                item.game_id.toInt(), utils.txtLocale(item.game_name_th, item.game_name_eng),
                                                if(item.sum_point != null){item.sum_point+ " "+getString(R.string.points)}else{""}, "", R.drawable.joy))
                                        d { "add [" + item.game_name_th + "] to array" }
                                    }
                                }

                                rv_learngame.adapter = LearnGameAdapter(LearnGameList, false)
                                d { "check response [" + c.size + "]" }
                            }
                        }, {
                            d { it.message!! }
                        })
        )

    }
    fun loadKnowLedgeInGroup(genderID:String,groupID:String,appDatabase: AppDatabase) {
            manageSub(
                    service.getKnowledgeInGroup(genderID, groupID)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({ c ->
                                run {
                                    val content = ObservableArrayList<Model.RepositoryKnowledge>()
                                    val data = ObservableArrayList<Model.LearnGameContent>().apply {
                                        c.forEach { item ->
                                            add(Model.LearnGameContent(
                                                    item.id.toInt(), utils.txtLocale(item.title_th, item.title_eng),
                                                    item.point+ " "+getString(R.string.points), "", R.drawable.book))
                                            content.add(item)
                                            d { "add [" + item.title_eng + "] to array" }
                                        }
                                    }
                                    contentList = content
                                    LearnGameList = data
                                    rv_learngame.adapter = LearnGameAdapter(LearnGameList, false)
                                    d { "check response [" + c.size + "]" }
                                }
                            }, {
                                d { it.message!! }
                                //load in local
                                if (appDatabase.getKnowledgeContent(groupID).size != 0) {
                                    val c = appDatabase.getKnowledgeContent(groupID)
                                    val content = ObservableArrayList<Model.RepositoryKnowledge>()
                                    val data = ObservableArrayList<Model.LearnGameContent>().apply {
                                        c.forEach { item ->
                                            add(Model.LearnGameContent(
                                                    item.id.toInt(), utils.txtLocale(item.title_th, item.title_eng),
                                                    item.point, "", R.drawable.book))
                                            content.add(item)
                                            d { "add [" + item.title_eng + "] to array" }
                                        }
                                    }
                                    contentList = content
                                    LearnGameList = data
                                    rv_learngame.adapter = LearnGameAdapter(LearnGameList, false)

                                }
                            })
            )

    }

    fun addUpdatePoint(point:String,contentId:String,userId: String){
        val addPoint = service.addUserPoint(userId,point)
        addPoint.enqueue(object : Callback<Void> {
            override fun onFailure(call: Call<Void>?, t: Throwable?) {
                Timber.d { t?.message.toString() }
            }
            override fun onResponse(call: Call<Void>?, response: Response<Void>?) {
                if(response!!.isSuccessful){
                    Timber.d { "addPoint Successful" }
                    inputPointHistory(point,contentId,userId)
                }
            }
        })


    }

    fun inputPointHistory(point:String,knowledgeID:String,id:String){
        Timber.d { "input point history" }
        Timber.d { "point [$point] user_id[$id]" }
        val addPH = service.addPointHistory(knowledgeID,id,"2",point, Date())
        addPH.enqueue(object : Callback<Void> {
            override fun onFailure(call: Call<Void>?, t: Throwable?) {
                Timber.d { t?.message.toString() }
            }
            override fun onResponse(call: Call<Void>?, response: Response<Void>?) {
                if (response!!.isSuccessful) {
                    Timber.d { "addPointHistory successful" }
                }
            }
        })

    }


}
