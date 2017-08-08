package asunder.toche.loveapp

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import asunder.toche.loveapp.RiskQuestionFragment

/**
 * Created by admin on 8/8/2017 AD.
 */
class RiskMeterAdapter(fragmentManager: FragmentManager, var context: Context) : FragmentPagerAdapter(fragmentManager) {


    override fun getCount(): Int {
        return NUM_ITEMS
    }

    // Returns the fragment to display for that page
    override fun getItem(position: Int): Fragment? {
        when (position) {
            0 -> return RiskQuestionFragment.newInstance()
            1 -> return RiskQuestionFragment2.newInstance()
            2 -> return RiskQuestionFragment3.newInstance()
            3 -> return RiskQuestionFragment3b.newInstance()
            4 -> return RiskQuestionFragment4.newInstance()
            5 -> return RiskQuestionFragment5.newInstance()
            6 -> return RiskQuestionFragment6.newInstance()
        //6 -> return ClinicInfoFragment.newInstance()
            else -> return null
        }
    }

    // Returns the page title for the top indicator
    override fun getPageTitle(position: Int): CharSequence {
        return "Page " + position
    }

    companion object {
        private val NUM_ITEMS = 7
    }
}