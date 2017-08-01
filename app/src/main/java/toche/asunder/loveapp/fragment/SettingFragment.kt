package toche.asunder.loveapp.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.header_logo_blue.*
import kotlinx.android.synthetic.main.setting_layout.*
import toche.asunder.loveapp.MyApp
import toche.asunder.loveapp.R

/**
 * Created by admin on 8/1/2017 AD.
 */
class SettingFragment : Fragment() {

    companion object {
        fun newInstance(): SettingFragment {
            return SettingFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.setting_layout, container, false)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        title_app.text = "SETTING"
        title_app.typeface = MyApp.typeFace.heavy
        txt_test_hiv.typeface = MyApp.typeFace.heavy
        txt_acc_setting.typeface = MyApp.typeFace.heavy
        txt_passcode.typeface = MyApp.typeFace.heavy
        txt_reminder.typeface = MyApp.typeFace.heavy

    }
}