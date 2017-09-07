package asunder.toche.loveapp

import android.content.Intent
import android.content.SharedPreferences
import android.databinding.ObservableArrayList
import android.databinding.ObservableList
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import asunder.toche.loveapp.R
import asunder.toche.loveapp.databinding.LearnTopicItemBinding
import com.bumptech.glide.Glide
import com.github.ajalt.timberkt.Timber.d
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.header_logo_white.*
import kotlinx.android.synthetic.main.learn_topic.*

/**
 * Created by admin on 8/14/2017 AD.
 */
class LearnTopicActivity : AppCompatActivity() {

    var knowledgeGroups = ObservableArrayList<Model.LearnTopicContent>()
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
    lateinit var appDb : AppDatabase


    override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.learn_topic)
            utils = Utils(this@LearnTopicActivity)
            preference = PreferenceManager.getDefaultSharedPreferences(this@LearnTopicActivity)
            appDb = AppDatabase(this@LearnTopicActivity)
            loadKnowledgeGroup(preference.getString(KEYPREFER.GENDER,"1"),appDb)
            title_app.text = "LEARNS"

            rv_learn_topic.setHasFixedSize(true)
            rv_learn_topic.layoutManager = LinearLayoutManager(this)

            Glide.with(this)
                    .load(R.drawable.bg_blue)
                    .into(bg_root)

        }



    private val LearnTopicType = Type<LearnTopicItemBinding>(R.layout.learn_topic_item)
            .onCreate { println("Created ${it.binding.topic} at #${it.adapterPosition}") }
            .onBind { println("Bound ${it.binding.topic} at #${it.adapterPosition}") }
            .onRecycle { println("Recycled ${it.binding.topic} at #${it.adapterPosition}") }
            .onClick {
                val data = Intent()
                data.putExtra("title","LEARNS")
                data.putExtra("key",2)
                data.putExtra("id",it.binding.topic.id)
                startActivity(data.setClass(this@LearnTopicActivity, LearnGameMainActivity::class.java))
            }
            .onLongClick {}


    fun LearnTopicAdapter(item: List<Any>, stableIds: Boolean): LastAdapter {
        return LastAdapter(item,BR.topic,stableIds).type{ item, position ->
            when(item){
                is Model.LearnTopicContent -> LearnTopicType
                else -> null
            }
        }
    }

    fun loadKnowledgeGroup(genderID:String,appDatabase: AppDatabase){
        if(appDatabase.getKnowledgeGroup().size != 0){
            val c = appDatabase.getKnowledgeGroup()
            val data = ObservableArrayList<Model.LearnTopicContent>().apply {
                c.forEach {
                    item -> add(Model.LearnTopicContent(
                        item.group_id.toInt(),utils.txtLocale(item.group_name_th,item.group_name_eng),
                        item.sumpoint+" Points","5 Topics",R.drawable.clinic_img))
                    d { "add [" + item.group_name_eng + "] to array" }
                }
            }
            knowledgeGroups = data
            rv_learn_topic.adapter =LearnTopicAdapter(knowledgeGroups,false)

        }else{
            manageSub(
                service.getKnowledgeGroup(genderID)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ c -> run {
                            val data = ObservableArrayList<Model.LearnTopicContent>().apply {
                                c.forEach {
                                    item -> add(Model.LearnTopicContent(
                                        item.group_id.toInt(),utils.txtLocale(item.group_name_th,item.group_name_eng),
                                        item.sumpoint+" Points","5 Topics",R.drawable.clinic_img))
                                    d { "add [" + item.group_name_eng + "] to array" }
                                }
                            }
                            knowledgeGroups = data
                            appDatabase.addKnowledgeGroup(c)
                            rv_learn_topic.adapter =LearnTopicAdapter(knowledgeGroups,false)

                            d { "check response [" + c.size + "]" }
                        }},{
                            d { it.message!! }
                        })
                )
        }
    }


}
