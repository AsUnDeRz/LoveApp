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

        PushDownAnim.setOnTouchPushDownAnim(btn_general)
        btn_general.setOnClickListener {
            val data =Intent()
            data.putExtra("data",KEYPREFER.GENERAL)
            activity.startActivity(data.setClass(activity,NewSetting::class.java))
        }

        PushDownAnim.setOnTouchPushDownAnim(btn_reminder)
        btn_reminder.setOnClickListener {
            val data =Intent()
            data.putExtra("data",KEYPREFER.REMINDERS)
            activity.startActivity(data.setClass(activity,NewSetting::class.java))
        }

        PushDownAnim.setOnTouchPushDownAnim(btn_support)
        btn_support.setOnClickListener {
            val data =Intent()
            data.putExtra("data",KEYPREFER.SUPPORT)
            activity.startActivity(data.setClass(activity,NewSetting::class.java))        }

        PushDownAnim.setOnTouchPushDownAnim(btn_about)
        btn_about.setOnClickListener {
            val data =Intent()
            data.putExtra("data",KEYPREFER.ABOUT)
            activity.startActivity(data.setClass(activity,NewSetting::class.java))        }

        PushDownAnim.setOnTouchPushDownAnim(btn_acc_setting)
        btn_acc_setting.setOnClickListener {
            activity.startActivity(Intent().setClass(activity, AccountSettingActivity::class.java))
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
            editor.putBoolean(KEYPREFER.ISCHECKPASSCODE,false)
            editor.putString(KEYPREFER.PASSCODE,"")
            editor.apply()
            activity.startActivity(Intent().setClass(activity,SelectLangActivity::class.java))
            activity.finish()

        }
    }



    override fun onResume() {
        super.onResume()
    }

}