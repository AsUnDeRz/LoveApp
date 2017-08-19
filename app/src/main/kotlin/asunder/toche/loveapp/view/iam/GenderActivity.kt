package asunder.toche.loveapp

import android.databinding.ObservableArrayList
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import asunder.toche.loveapp.R
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.gender_list.*
import kotlinx.android.synthetic.main.header_logo_white_back.*


/**
 * Created by admin on 7/30/2017 AD.
 */
class GenderActivity : AppCompatActivity(){

    val genderList = ObservableArrayList<Model.Gender>().apply {
        add(Model.Gender(1,"Men"))
        add(Model.Gender(1,"Women"))
        add(Model.Gender(1,"Gay"))
        add(Model.Gender(1,"Bi"))
        add(Model.Gender(1,"Lady Boy"))

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.gender_list)
        Glide.with(this)
                .load(R.drawable.bg_blue)
                .into(bg_root)
        Glide.with(this)
                .load(R.drawable.image_cycle)
                .into(circle_icon)


        rv_gender.layoutManager = LinearLayoutManager(this) 
        rv_gender.setHasFixedSize(true)
        rv_gender.adapter = MasterAdapter.GenderAdapter(genderList,false)

    }



}