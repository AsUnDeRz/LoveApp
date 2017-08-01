package toche.asunder.loveapp.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.synnapps.carouselview.CarouselView
import com.synnapps.carouselview.ImageListener
import kotlinx.android.synthetic.main.header_home.*
import kotlinx.android.synthetic.main.home.*
import kotlinx.android.synthetic.main.lab_result.*
import toche.asunder.loveapp.MyApp
import toche.asunder.loveapp.R
import toche.asunder.loveapp.activity.ActivityMain
import toche.asunder.loveapp.activity.NotificationActivity
import toche.asunder.loveapp.adapter.HomeAdapter

/**
 * Created by admin on 8/1/2017 AD.
 */
class HomeFragment : Fragment() {

    companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }
    val banner = arrayOf(R.drawable.silde_img_1,R.drawable.silde_img_1,R.drawable.silde_img_1)


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.home, container, false)
        val cv = view?.findViewById<CarouselView>(R.id.cV)
        cv?.setImageListener(listener)
        cv?.pageCount = banner.size






        return view
    }

     var listener: ImageListener = ImageListener { position, imageView ->
        //imageView.scaleType = ImageView.ScaleType.FIT_XY
        Glide.with(this)
                .load(banner[position])
                .into(imageView)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        title_app.typeface = MyApp.typeFace.heavy
        txt_hello.typeface = MyApp.typeFace.heavy


        button_noti.setOnClickListener {
            activity.startActivity(Intent().setClass(activity, NotificationActivity::class.java))
        }


        rv_home.layoutManager = LinearLayoutManager(context)
        rv_home.setHasFixedSize(true)
        rv_home.adapter = HomeAdapter(context)

    }
}