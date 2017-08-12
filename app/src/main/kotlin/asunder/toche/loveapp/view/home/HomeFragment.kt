package asunder.toche.loveapp

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.ObservableArrayList
import android.databinding.ObservableList
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import asunder.toche.loveapp.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.synnapps.carouselview.CarouselView
import com.synnapps.carouselview.ImageListener
import kotlinx.android.synthetic.main.header_home.*
import kotlinx.android.synthetic.main.home.*



/**
 * Created by admin on 8/1/2017 AD.
 */
class HomeFragment : Fragment(),ViewModel.HomeViewModel.HomeInterface {


    override fun endCallProgress(data: ObservableList<Model.ImageHome>) {
        imaHome = data
        cV.setImageListener(listener)
        cV.pageCount = imaHome.size
    }


    lateinit var homeViewModel: ViewModel.HomeViewModel
    lateinit var imaHome : ObservableList<Model.ImageHome>


    companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }
    val banner = arrayOf(R.drawable.silde_img_1,R.drawable.silde_img_1,R.drawable.silde_img_1)
    var homeList = ObservableArrayList<Model.HomeContent>().apply{
        add(Model.HomeContent(1,"utils.Test"))
        add(Model.HomeContent(1,"utils.Test"))
        add(Model.HomeContent(1,"utils.Test"))
        add(Model.HomeContent(1,"utils.Test"))
        add(Model.HomeContent(1,"utils.Test"))

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeViewModel = ViewModelProviders.of(this).get(ViewModel.HomeViewModel::class.java)

    }


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.home, container, false)
        val cv = view?.findViewById<CarouselView>(R.id.cV)
        homeViewModel.loadImage(this)
        //cv?.setImageListener(listener)
        //cv?.pageCount






        return view
    }

     var listener: ImageListener = ImageListener { position, imageView ->
        //imageView.scaleType = ImageView.ScaleType.FIT_XY
         var image = imaHome[position]
        Glide.with(this)
                .load("http://loveapponline.com/"+image.image_byte)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        button_noti.setOnClickListener {
            activity.startActivity(Intent().setClass(activity, NotificationActivity::class.java))
        }


        rv_home.layoutManager = LinearLayoutManager(context)
        rv_home.setHasFixedSize(true)
        rv_home.adapter = MasterAdapter.HomeAdapter(homeList,false)

    }

    override fun onPause() {
        super.onPause()
        homeViewModel.unsubscribe()
    }
}