package toche.asunder.loveapp.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.clinic_list.*
import kotlinx.android.synthetic.main.header_logo_blue.*
import toche.asunder.loveapp.MyApp
import toche.asunder.loveapp.R
import toche.asunder.loveapp.activity.ActivityMain
import toche.asunder.loveapp.adapter.ClinicAdapter

/**
 * Created by admin on 8/2/2017 AD.
 */
class ClinicFragment: Fragment() {

    companion object {
        fun newInstance(): ClinicFragment {
            return ClinicFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.clinic_list, container, false)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        title_app.typeface = MyApp.typeFace.heavy
        txt_location.typeface = MyApp.typeFace.heavy
        txt_location_desc.typeface = MyApp.typeFace.medium

        //set title
        title_app.text ="BOOK\nA TEST"



        btn_map.setOnClickListener {
            ActivityMain.vp_main.setCurrentItem(1,false)
        }


        rv_clinic_list.layoutManager = GridLayoutManager(context,2)
        rv_clinic_list.setHasFixedSize(true)
        rv_clinic_list.adapter = ClinicAdapter(context)

    }
}