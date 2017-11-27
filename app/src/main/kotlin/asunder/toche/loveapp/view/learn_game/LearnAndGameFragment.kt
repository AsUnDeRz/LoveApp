package asunder.toche.loveapp

import android.content.Intent
import android.content.SharedPreferences
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
import com.github.ajalt.timberkt.Timber
import com.github.ajalt.timberkt.Timber.d
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.header_logo_blue.*
import kotlinx.android.synthetic.main.learnandgame.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


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
    lateinit var prefer : SharedPreferences

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
        title_app.text = getString(R.string.title_learnandgame)
        prefer = PreferenceManager.getDefaultSharedPreferences(activity)
        rv_learn_game.layoutManager = LinearLayoutManager(context)
        rv_learn_game.setHasFixedSize(true)
        rv_learn_game.adapter = LearnNewAdapter(homeList, false)


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

        if (appDb.getKnowledgeContent().size != 0) {
            val c = appDb.getKnowledgeContent()
            val data = ObservableArrayList<Model.HomeContent>().apply {
                c.forEach { a ->
                    add(Model.HomeContent(a.id.toLong(), utils.txtLocale(a.title_th, a.title_eng), a.point + " "+context.getString(R.string.points), a))
                    d { "add contentId [" + a.id + "]" }
                }
            }
            homeList = data
            rv_learn_game.adapter = LearnNewAdapter(homeList, false)
        }else {
            loadContentHome(prefer.getString(KEYPREFER.UserId, ""), appDb)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == KEYPREFER.LEARNGAME && data != null){
            Timber.d { "result for knowledge content " }
            val point = data.getStringExtra(KEYPREFER.POINT)
            val contentID = data.getStringExtra(KEYPREFER.CONTENT)
            val userID = prefer.getString(KEYPREFER.UserId,"")
            d{"check result $point   $contentID"}
            //addUpdatePoint(point,contentID,userID)
        }
    }


    private val LearnNewType = Type<LearnNewItemBinding>(R.layout.learn_new_item)
            .onClick {
                val item = it.binding.learnNewItem.data
                val model = Model.RepositoryKnowledge(item.id,item.group_id,item.title_th,item.title_eng,item.content_th,item.content_eng,
                        "image",item.point, arrayListOf("2","3"),item.version,item.content_th_long,item.content_eng_long,item.link)

                var data = Intent()
                data.putExtra(KEYPREFER.CONTENT,model)
                startActivityForResult(data.setClass(activity,LearnNewsActivity::class.java),KEYPREFER.LEARNGAME)

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
            manageSub(
                    service.getContentInHome(user_id)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({ c ->
                                run {
                                    val data = ObservableArrayList<Model.HomeContent>().apply {
                                        c.forEach { a ->
                                            add(Model.HomeContent(a.id.toLong(), utils.txtLocale(a.title_th, a.title_eng), a.point + " "+context.getString(R.string.points), a))
                                            d { "add contentId [" + a.id + "]" }
                                        }
                                    }
                                    homeList = data
                                    rv_learn_game.adapter = LearnNewAdapter(homeList, false)
                                    d { "check response [" + c.size + "]" }
                                }
                            }, {
                                d { it.message!! }
                                //load in local
                                if (appDatabase.getKnowledgeContent().size != 0) {
                                    val c = appDatabase.getKnowledgeContent()
                                    val data = ObservableArrayList<Model.HomeContent>().apply {
                                        c.forEach { a ->
                                            add(Model.HomeContent(a.id.toLong(), utils.txtLocale(a.title_th, a.title_eng), a.point + " "+context.getString(R.string.points), a))
                                            d { "add contentId [" + a.id + "]" }
                                        }
                                    }
                                    homeList = data
                                    rv_learn_game.adapter = LearnNewAdapter(homeList, false)
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

    override fun onPause() {
        super.onPause()
        unsubscribe()
    }
}