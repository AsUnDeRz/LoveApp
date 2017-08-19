package asunder.toche.loveapp

import android.databinding.ObservableArrayList
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import asunder.toche.loveapp.R
import kotlinx.android.synthetic.main.clinic_list.*
import kotlinx.android.synthetic.main.header_logo_blue.*



/**
 * Created by admin on 8/2/2017 AD.
 */
class ClinicFragment: Fragment() {

    val clinicList = ObservableArrayList<Model.Clinic>().apply {
        for(i in 1..6) {
            add(Model.Clinic(1123, "Bangkok", "HIV utils.Test", "10.00 am - 19.00 pm",R.drawable.clinic_img))
        }
    }
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
        txt_search.typeface = MyApp.typeFace.medium

        //set title
        title_app.text ="BOOK\nA TEST"



        btn_map.setOnClickListener {
            ActivityMain.vp_main.setCurrentItem(KEYPREFER.LAB,false)
        }


        rv_clinic_list.layoutManager = GridLayoutManager(context,2)
        rv_clinic_list.setHasFixedSize(true)
        rv_clinic_list.adapter = MasterAdapter.ClinicAdapter(clinicList,false)

    }
}