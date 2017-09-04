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
import com.github.ajalt.timberkt.Timber.d
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.header_logo_white_back.*
import kotlinx.android.synthetic.main.learn_game_list.*


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



    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.learn_game_list)
        utils = Utils(this@LearnGameMainActivity)
        preference = PreferenceManager.getDefaultSharedPreferences(this@LearnGameMainActivity)
        title_header.text = intent.extras.getString("title")
        Glide.with(this)
                .load(R.drawable.bg_blue)
                .into(bg_root)

        when(intent.extras.getInt("key")){
            1 ->{
                LearnGameList.apply {
                    for(i in 1..3) {
                        add(Model.LearnGameContent(R.drawable.clinic_img, "Memory Master #$i", "1"+i+"0", "20",R.drawable.clinic_img))
                        add(Model.LearnGameContent(R.drawable.clinic_img, "Co-op Game", "100", "10",R.drawable.clinic_img))

                    }
                }
                rv_learngame.adapter = LearnGameAdapter(LearnGameList,false)
            }
            2 ->{
                //loadKnowledge in group
                loadKnowLedgeInGroup(preference.getString(KEYPREFER.GENDER,"1"),intent.extras.getInt("id").toString())
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
                        if(it.binding.titleGame.text  == "Memory Master #1"){
                            startActivity(Intent().setClass(this,MemoryMasterActivity::class.java))
                        }
                        if(it.binding.titleGame.text == "Memory Master #2"){
                            startActivity(Intent().setClass(this,MemoryMaster2Activity::class.java))

                        }
                        if(it.binding.titleGame.text == "Safe Sex"){
                            startActivity(Intent().setClass(this,QuestionActivity::class.java))

                        }
                    }
                    2 ->{
                        val item = contentList[it.adapterPosition]
                        val model = Model.RepositoryKnowledge(item.id,item.group_id,item.title_th,item.title_eng,item.content_th,item.content_eng,
                                "image",item.point, arrayListOf("2","3"),"1",item.content_th_long,item.content_eng_long,item.link)
                        var data = Intent()
                        data.putExtra(KEYPREFER.CONTENT,model)
                        startActivity(data.setClass(this@LearnGameMainActivity,LearnNewsActivity::class.java))
                    }
                }

            }
            .onLongClick {}


    fun loadKnowLedgeInGroup(genderID:String,groupID:String){
        manageSub(
                service.getKnowledgeInGroup(genderID,groupID)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ c -> run {
                            val content =ObservableArrayList<Model.RepositoryKnowledge>()
                            val data = ObservableArrayList<Model.LearnGameContent>().apply {
                                c.forEach { item -> add(Model.LearnGameContent(
                                        item.id.toInt(),utils.txtLocale(item.title_th,item.title_eng),
                                        item.point,"20% done",R.drawable.clinic_img))
                                    content.add(item)
                                    d { "add [" + item.title_eng + "] to array" }
                                }
                            }
                            contentList = content
                            LearnGameList = data
                            rv_learngame.adapter = LearnGameAdapter(LearnGameList,false)
                            d { "check response [" + c.size + "]" }
                        }},{
                            d { it.message!! }
                        })
        )
    }

}
