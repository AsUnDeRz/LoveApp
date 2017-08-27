package asunder.toche.loveapp

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.ObservableArrayList
import android.databinding.ObservableList
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import asunder.toche.loveapp.R
import asunder.toche.loveapp.databinding.HomeItemBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.target.Target
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MarkerOptions
import com.synnapps.carouselview.CarouselView
import com.synnapps.carouselview.ImageListener
import io.reactivex.Observable
import kotlinx.android.synthetic.main.header_home.*
import kotlinx.android.synthetic.main.home.*
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg


/**
 * Created by admin on 8/1/2017 AD.
 */
class HomeFragment : Fragment(),ViewModel.HomeViewModel.HomeInterface {


    override fun endCallProgress(any:Any) {
        val data = any as ObservableArrayList<*>
        homeList.apply {
            for (a in data){
                if (a is Model.RepositoryContentHome){
                    add(Model.HomeContent(a.id.toLong(), utils.txtLocale(a.title_th, a.title_eng), a.point + " Points",a))
                }
            }
        }
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

    }


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.home, container, false)
        val cv = view?.findViewById<CarouselView>(R.id.cV)
        appDb = AppDatabase(activity)
        //homeViewModel.loadImage(this)
        val prefer = PreferenceManager.getDefaultSharedPreferences(activity)
        homeViewModel.loadContentHome(this,prefer.getString(KEYPREFER.UserId,""))
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
                 .diskCacheStrategy(DiskCacheStrategy.ALL)
                 .into(imageView)
         /*
         var image = imaHome[position]
                 Glide.with(activity)
                         .load("http://backend.loveapponline.com/"+image.image_byte)
                         .diskCacheStrategy(DiskCacheStrategy.ALL)
                         .into(imageView)
        */

    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        button_noti.setOnClickListener {
            activity.startActivity(Intent().setClass(activity, NotificationActivity::class.java))
        }


        rv_home.layoutManager = LinearLayoutManager(context)
        rv_home.setHasFixedSize(true)



        var notiList = appDb.getNotiMissing()

        when {
            notiList.size >= 3 -> loadNotiColor(R.drawable.noti_red)
            notiList.size <=2 -> loadNotiColor(R.drawable.noti_yellow)
            else -> loadNotiColor(R.drawable.noti_green)
        }

    }

    override fun onPause() {
        super.onPause()
        homeViewModel.unsubscribe()
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
            .onCreate { println("Created ${it.binding.homeItem} at #${it.adapterPosition}") }
            .onBind { println("Bound ${it.binding.homeItem} at #${it.adapterPosition}") }
            .onRecycle { println("Recycled ${it.binding.homeItem} at #${it.adapterPosition}") }
            .onClick {

                val item = it.binding.getHomeItem().data
                val model = Model.RepositoryContentHome("1","1","title_th",item.title_eng,"content_th",item.content_eng,
                        "image","100", arrayListOf("2","3"),"1","content_th_long",item.content_eng_long,"link")

                var data = Intent()
                data.putExtra(KEYPREFER.CONTENT,model)
                startActivity(data.setClass(activity,LearnNewsActivity::class.java))
            }
            .onLongClick {}
}