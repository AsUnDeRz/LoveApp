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

        PushDownAnim.setOnTouchPushDownAnim(btn_passcode)
        btn_passcode.setOnClickListener {
            activity.startActivity(Intent().setClass(activity,ChangePasscodeActivity::class.java))
        }

        PushDownAnim.setOnTouchPushDownAnim(btn_reminder)
        btn_reminder.setOnClickListener {
            activity.startActivity(Intent().setClass(activity,PillReminderTimeActivity::class.java))
        }

        PushDownAnim.setOnTouchPushDownAnim(btn_test_hiv)
        btn_test_hiv.setOnClickListener {
            activity.startActivity(Intent().setClass(activity,HivTestActivity::class.java))
        }

        PushDownAnim.setOnTouchPushDownAnim(btn_cd4)
        btn_cd4.setOnClickListener {
            activity.startActivity(Intent().setClass(activity,Cd4VLActivity::class.java))
        }

        PushDownAnim.setOnTouchPushDownAnim(btn_acc_setting)
        btn_acc_setting.setOnClickListener {
            activity.startActivity(Intent().setClass(activity, AccountSettingActivity::class.java))
        }

        PushDownAnim.setOnTouchPushDownAnim(btn_feedback)
        btn_feedback.setOnClickListener {
            activity.startActivity(Intent().setClass(activity, FeedbackActivity::class.java))
        }

        PushDownAnim.setOnTouchPushDownAnim(btn_language)
        btn_language.setOnClickListener{
            activity.startActivity(Intent().setClass(activity,LanguageActivity::class.java))
        }




    }
}