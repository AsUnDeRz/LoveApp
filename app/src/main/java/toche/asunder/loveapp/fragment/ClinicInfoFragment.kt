package toche.asunder.loveapp.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.clinic_info.*
import kotlinx.android.synthetic.main.header_logo_white_back.*
import toche.asunder.loveapp.MyApp
import toche.asunder.loveapp.R
import toche.asunder.loveapp.activity.ActivityMain

/**
 * Created by admin on 8/2/2017 AD.
 */
class ClinicInfoFragment: Fragment() {

    companion object {
        fun newInstance(): ClinicInfoFragment {
            return ClinicInfoFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.clinic_info, container, false)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        title_header.typeface = MyApp.typeFace.heavy
        title_clinic.typeface = MyApp.typeFace.heavy
        txt_infomation.typeface = MyApp.typeFace.heavy
        txt_contact.typeface = MyApp.typeFace.heavy
        txt_nametest.typeface = MyApp.typeFace.medium
        txt_worktime.typeface = MyApp.typeFace.medium
        txt_web.typeface = MyApp.typeFace.medium
        txt_phone.typeface = MyApp.typeFace.medium
        txt_map.typeface = MyApp.typeFace.medium


        //set title



        btn_booknow.setOnClickListener {
        }



    }
}