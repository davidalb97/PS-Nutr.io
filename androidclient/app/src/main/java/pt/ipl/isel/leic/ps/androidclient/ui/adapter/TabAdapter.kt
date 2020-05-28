package pt.ipl.isel.leic.ps.androidclient.ui.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

/**
 * A "Tab" Adapter to be used by multiple fragments that use a ViewPager
 */
class TabAdapter(fragment: FragmentManager) : FragmentPagerAdapter(fragment) {

    private val fragmentList: MutableList<Fragment> = ArrayList()
    private val titleList: MutableList<String> = ArrayList()
    private var broadcastBundle: Bundle? = null

    fun addBroadcastBundle(bundle: Bundle?) {
        broadcastBundle = bundle
    }

    fun addFragment(fragment: Fragment, title: String) {
        if (broadcastBundle != null) {
            fragment.arguments = broadcastBundle
        }
        fragmentList.add(fragment)
        titleList.add(title)
    }

    override fun getCount(): Int {
        return fragmentList.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titleList[position]
    }

    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }
}