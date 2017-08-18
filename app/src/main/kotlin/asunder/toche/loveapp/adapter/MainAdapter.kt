package asunder.toche.loveapp

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.app.FragmentStatePagerAdapter

/**
 * Created by admin on 8/7/2017 AD.
 */
class MainAdapter(fragmentManager: FragmentManager, var context: Context) : FragmentStatePagerAdapter(fragmentManager) {



    override fun getCount(): Int {
        return NUM_ITEMS
    }

    // Returns the fragment to display for that page
    override fun getItem(position: Int): Fragment? {
        when (position) {
            KEYPREFER.HOME -> return HomeFragment.newInstance()
            KEYPREFER.LAB -> return LabFragment.newInstance()
            KEYPREFER.IAM -> return IamFragment.newInstance()
            KEYPREFER.LEARNGAME -> return LearnAndGameFragment.newInstance()
            KEYPREFER.SETTING -> return SettingFragment.newInstance()
            KEYPREFER.CLINIC -> return ClinicFragment.newInstance()
        //6 -> return ClinicInfoFragment.newInstance()
            else -> return null
        }
    }

    // Returns the page title for the top indicator
    override fun getPageTitle(position: Int): CharSequence {
        return "Page " + position
    }

    companion object {
        private val NUM_ITEMS = 6
    }

}