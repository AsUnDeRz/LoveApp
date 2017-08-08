package asunder.toche.loveapp

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

/**
 * Created by admin on 8/7/2017 AD.
 */
class MainAdapter(fragmentManager: FragmentManager, var context: Context) : FragmentPagerAdapter(fragmentManager) {



    override fun getCount(): Int {
        return NUM_ITEMS
    }

    // Returns the fragment to display for that page
    override fun getItem(position: Int): Fragment? {
        when (position) {
            0 -> return HomeFragment.newInstance()
            1 -> return LabFragment.newInstance()
            2 -> return IamFragment.newInstance()
            3 -> return LearnAndGameFragment.newInstance()
            4 -> return SettingFragment.newInstance()
            5 -> return ClinicFragment.newInstance()
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