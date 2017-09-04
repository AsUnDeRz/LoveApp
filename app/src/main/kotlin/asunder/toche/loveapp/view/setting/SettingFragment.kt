package asunder.toche.loveapp

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import asunder.toche.loveapp.R
import kotlinx.android.synthetic.main.header_logo_blue.*
import kotlinx.android.synthetic.main.setting_layout.*

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

        btn_passcode.setOnClickListener {
            context.startActivity(Intent().setClass(context,ChangePasscodeActivity::class.java))
        }

        btn_reminder.setOnClickListener {
            context.startActivity(Intent().setClass(context,PillReminderTimeActivity::class.java))

        }

        btn_test_hiv.setOnClickListener {
            context.startActivity(Intent().setClass(context,HivTestActivity::class.java))

        }

        btn_cd4.setOnClickListener {
            context.startActivity(Intent().setClass(context,Cd4VLActivity::class.java))
        }

        btn_acc_setting.setOnClickListener {
            context.startActivity(Intent().setClass(context, AccountSettingActivity::class.java))

        }

        btn_feedback.setOnClickListener {
            context.startActivity(Intent().setClass(context, FeedbackActivity::class.java))

        }

    }
}