package asunder.toche.loveapp

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
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

    lateinit var prefer : SharedPreferences
    lateinit var appDb :AppDatabase

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.setting_layout, container, false)
        prefer = PreferenceManager.getDefaultSharedPreferences(activity)
        appDb = AppDatabase(activity)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        title_app.text = "SETTING"

        if(prefer.getInt(KEYPREFER.HIVSTAT,0) != 0){
            val stat = prefer.getInt(KEYPREFER.HIVSTAT,0)
            when(stat){
                1 ->{ btn_cd4.visibility = View.VISIBLE}
                2 ->{ btn_cd4.visibility = View.GONE}
                3 ->{ btn_cd4.visibility = View.GONE}
            }

        }

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

        PushDownAnim.setOnTouchPushDownAnim(btn_logout)
        btn_logout.setOnClickListener {
            appDb.deleteAllKnowledgeGroup()
            appDb.deleteAllKnowledgeContent()
            val editor = prefer.edit()
            editor.putString(KEYPREFER.UserId,"")
            editor.putBoolean(KEYPREFER.isFirst, true)
            editor.putInt(KEYPREFER.HIVSTAT,0)
            editor.putString(KEYPREFER.GENDER,"")
            editor.apply()
            activity.startActivity(Intent().setClass(activity,SelectLangActivity::class.java))
            activity.finish()

        }





    }


    override fun onResume() {
        super.onResume()
        if(prefer.getInt(KEYPREFER.HIVSTAT,0) != 0){
            val stat = prefer.getInt(KEYPREFER.HIVSTAT,0)
            when(stat){
                1 ->{ btn_cd4.visibility = View.VISIBLE}
                2 ->{ btn_cd4.visibility = View.GONE}
                3 ->{ btn_cd4.visibility = View.GONE}
            }

        }
    }

}