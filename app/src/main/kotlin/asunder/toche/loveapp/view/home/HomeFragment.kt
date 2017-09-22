package asunder.toche.loveapp

import android.arch.lifecycle.ViewModelProviders
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
import asunder.toche.loveapp.databinding.HomeItemBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.github.ajalt.timberkt.Timber
import com.github.ajalt.timberkt.Timber.d
import com.synnapps.carouselview.CarouselView
import com.synnapps.carouselview.ImageListener
import kotlinx.android.synthetic.main.header_home.*
import kotlinx.android.synthetic.main.home.*
import kotlinx.android.synthetic.main.home_item.view.*


/**
 * Created by admin on 8/1/2017 AD.
 */
class HomeFragment : Fragment(),ViewModel.HomeViewModel.HomeInterface {


    override fun endCallProgress(any:Any) {
        val data = any as ObservableArrayList<*>
        val content =ObservableArrayList<Model.HomeContent>().apply {
            //check update user
            if(prefer.getBoolean(KEYPREFER.isUpdateProfile,false)){

            }else{
                var item = data[0] as Model.RepositoryKnowledge
                add(Model.HomeContent(1111111111,utils.txtLocale("กรอกข้อมูลของคุณ","Update Your info"),"500 "+context.getString(R.string.points),item))
            }
            for (a in data){
                if (a is Model.RepositoryKnowledge){
                    add(Model.HomeContent(a.id.toLong(), utils.txtLocale(a.title_th, a.title_eng), a.point + " "+context.getString(R.string.points),a))
                }
            }
        }

        homeList = content
        rv_home.adapter = HomeAdapter(homeList, false)


        /*
        imaHome = data
        cV.setImageListener(listener)
        cV.pageCount = imaHome.size
        */
    }


    lateinit var homeViewModel: ViewModel.HomeViewModel
    //lateinit var imaHome : ObservableList<Model.ImageHome>
    lateinit var utils :Utils
    lateinit var appDb :AppDatabase
    lateinit var prefer : SharedPreferences


    companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }
    var homeList = ObservableArrayList<Model.HomeContent>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeViewModel = ViewModelProviders.of(this).get(ViewModel.HomeViewModel::class.java)
        utils = Utils(activity)
        appDb = AppDatabase(activity)
        prefer = PreferenceManager.getDefaultSharedPreferences(activity)
    }


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.home, container, false)
        val cv = view?.findViewById<CarouselView>(R.id.cV)
        cv?.setImageListener(listener)
        cv?.pageCount = DataSimple.imageHome.size
        //cv?.setImageListener(listener)
        //cv?.pageCount



        return view
    }
    fun loadNotiColor(image:Int){
        Glide.with(this)
                .load(image)
                .into(button_noti)
    }

    var listener: ImageListener = ImageListener { position, imageView ->
        //imageView.scaleType = ImageView.ScaleType.FIT_XY
        Glide.with(activity)
                .load(DataSimple.imageHome[position])
                .into(imageView)


    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeViewModel.loadContentHome(this, prefer.getString(KEYPREFER.UserId, ""),appDb,context)
        button_noti.setOnClickListener {
            activity.startActivity(Intent().setClass(activity, NotificationActivity::class.java))
        }
        rv_home.layoutManager = LinearLayoutManager(context)
        rv_home.setHasFixedSize(true)
        rv_home.adapter = HomeAdapter(homeList, false)




        /*
        var notiList = appDb.getNotiMissing()
        when {
            notiList.size >= 3 -> loadNotiColor(R.drawable.noti_red)
            notiList.size >= 1 -> loadNotiColor(R.drawable.noti_yellow)
            else -> loadNotiColor(R.drawable.noti_green)
        }
        */

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == KEYPREFER.LEARNGAME && data != null){
            Timber.d { "result for knowledge content " }
            val point = data.getStringExtra(KEYPREFER.POINT)
            val contentID = data.getStringExtra(KEYPREFER.CONTENT)
            val userID = prefer.getString(KEYPREFER.UserId,"")
            d{"check result $point   $contentID"}
            homeViewModel.addUpdatePoint(point,contentID,userID)
        }
        if(requestCode == KEYPREFER.HOME){
            //load check data and
            d{"result for update account"}
            val isUpdate = prefer.getBoolean(KEYPREFER.isUpdateProfile,false)
            if(isUpdate){
                //addpoint
                d{"isUpdate"}
                homeViewModel.addUpdatePoint("500",prefer.getString(KEYPREFER.UserId,""))
            }else{
                d{"noUpdate"}

            }
        }
    }

    override fun onPause() {
        super.onPause()
        homeViewModel.unsubscribe()
    }

    override fun onResume() {
        super.onResume()
        var notiList = appDb.getNotiMissing()
        when {
            notiList.size >= 3 -> loadNotiColor(R.drawable.noti_red)
            notiList.size >= 1 -> loadNotiColor(R.drawable.noti_yellow)
            else -> loadNotiColor(R.drawable.noti_green)
        }
    }



    fun HomeAdapter(item: List<Any>, stableIds: Boolean): LastAdapter {
        return LastAdapter(item,BR.homeItem,stableIds).type{ item, position ->
            when(item){
                is Model.HomeContent -> HomeType
                else -> null
            }
        }
    }

    private val HomeType = Type<HomeItemBinding>(R.layout.home_item)
            .onCreate { }
            .onBind { }
            .onRecycle {  }
            .onClick {
                val item = it.binding.getHomeItem().data
                if (it.binding.getHomeItem().id == 1111111111L) {
                    startActivityForResult(Intent().setClass(activity, AccountSettingActivity::class.java),KEYPREFER.HOME)
                } else {
                    val model = Model.RepositoryKnowledge(item.id, item.group_id, item.title_th, item.title_eng, item.content_th, item.content_eng,
                            "image", item.point, arrayListOf("2", "3"), item.version, item.content_th_long, item.content_eng_long, item.link)
                    var data = Intent()
                    data.putExtra(KEYPREFER.CONTENT, model)
                    startActivityForResult(data.setClass(activity, LearnNewsActivity::class.java), KEYPREFER.LEARNGAME)
                }
            }
            .onLongClick {}
}