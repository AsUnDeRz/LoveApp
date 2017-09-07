package asunder.toche.loveapp

import android.content.Intent
import android.databinding.ObservableArrayList
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import asunder.toche.loveapp.R
import asunder.toche.loveapp.databinding.LearnNewItemBinding
import com.github.ajalt.timberkt.Timber.d
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.header_logo_blue.*
import kotlinx.android.synthetic.main.learnandgame.*


/**
 * Created by admin on 8/7/2017 AD.
 */
class LearnAndGameFragment : Fragment() {


    var homeList = ObservableArrayList<Model.HomeContent>()
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

    lateinit var utils :Utils
    lateinit var appDb :AppDatabase

    companion object {
        fun newInstance(): LearnAndGameFragment {
            return LearnAndGameFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        utils = Utils(activity)
        appDb = AppDatabase(activity)
    }


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.learnandgame, container, false)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        title_app.text = "LEARNS\nAND GAMES"
        val prefer = PreferenceManager.getDefaultSharedPreferences(activity)
        loadContentHome(prefer.getString(KEYPREFER.UserId,""),appDb)
        rv_learn_game.layoutManager = LinearLayoutManager(context)
        rv_learn_game.setHasFixedSize(true)

        btn_game.setOnClickListener {
            val data = Intent()
            data.putExtra("title","GAMES")
            data.putExtra("key",1)
            startActivity(data.setClass(context, LearnGameMainActivity::class.java))
        }
        btn_learn.setOnClickListener {
            val data = Intent()
            //data.putExtra("title","LEARNS")
            //data.putExtra("key",2)
            startActivity(data.setClass(context, LearnTopicActivity::class.java))
        }

    }

    private val LearnNewType = Type<LearnNewItemBinding>(R.layout.learn_new_item)
            .onCreate { println("Created ${it.binding.learnNewItem} at #${it.adapterPosition}") }
            .onBind { println("Bound ${it.binding.learnNewItem} at #${it.adapterPosition}") }
            .onRecycle { println("Recycled ${it.binding.learnNewItem} at #${it.adapterPosition}") }
            .onClick {
                val item = it.binding.learnNewItem.data
                val model = Model.RepositoryKnowledge("1","1","title_th",item.title_eng,"content_th",item.content_eng,
                        "image",item.point, arrayListOf("2","3"),"1","content_th_long",item.content_eng_long,item.link)

                var data = Intent()
                data.putExtra(KEYPREFER.CONTENT,model)
                startActivity(data.setClass(activity,LearnNewsActivity::class.java))

            }
            .onLongClick {}
    fun LearnNewAdapter(item: List<Any>, stableIds: Boolean): LastAdapter {
        return LastAdapter(item,BR.learnNewItem,stableIds).type{ item, position ->
            when(item){
                is Model.HomeContent -> LearnNewType
                else -> null
            }
        }
    }

    fun loadContentHome(user_id:String,appDatabase: AppDatabase) {
        if (appDatabase.getKnowledgeContent().size != 0) {
            val c = appDatabase.getKnowledgeContent()
            val data = ObservableArrayList<Model.HomeContent>().apply {
                c.forEach { a ->
                    add(Model.HomeContent(a.id.toLong(), utils.txtLocale(a.title_th, a.title_eng), a.point + " Points", a))
                    d { "add contentId [" + a.id + "]" }
                }
            }
            homeList = data
            rv_learn_game.adapter = LearnNewAdapter(homeList, false)
        } else {
            manageSub(
                    service.getContentInHome(user_id)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({ c ->
                                run {
                                    val data = ObservableArrayList<Model.HomeContent>().apply {
                                        c.forEach { a ->
                                            add(Model.HomeContent(a.id.toLong(), utils.txtLocale(a.title_th, a.title_eng), a.point + " Points", a))
                                            d { "add contentId [" + a.id + "]" }
                                        }
                                    }
                                    homeList = data
                                    rv_learn_game.adapter = LearnNewAdapter(homeList, false)
                                    d { "check response [" + c.size + "]" }
                                }
                            }, {
                                d { it.message!! }
                            })
            )
        }
    }

    override fun onPause() {
        super.onPause()
        unsubscribe()
    }
}