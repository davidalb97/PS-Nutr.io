package pt.ipl.isel.leic.ps.androidclient.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * A "Tab" Adapter to be used by multiple fragments that use a ViewPager
 */
class TabAdapter(fragment: FragmentManager) : FragmentPagerAdapter(fragment) {

    private val fragmentList: MutableList<Fragment> = ArrayList()
    private val titleList: MutableList<String> = ArrayList()

    fun addFragment(fragment: Fragment, title: String) {
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