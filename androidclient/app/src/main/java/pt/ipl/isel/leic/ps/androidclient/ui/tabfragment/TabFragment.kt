package pt.ipl.isel.leic.ps.androidclient.ui.tabfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.ui.adapter.TabAdapter

abstract class TabFragment(
    private val tabFragments: Map<Fragment, String>
) : Fragment() {

    lateinit var tabPagerAdapter: TabAdapter
    lateinit var viewPager: ViewPager
    lateinit var tabs: TabLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tab, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tabPagerAdapter = TabAdapter(childFragmentManager)
        tabFragments.forEach { tabFragment ->
            tabPagerAdapter.addFragment(tabFragment.key, tabFragment.value)
        }
        viewPager = this.requireView().findViewById(R.id.viewPager)
        tabs = this.requireView().findViewById(R.id.tab)
        viewPager.adapter = tabPagerAdapter
        tabs.setupWithViewPager(viewPager)

    }
}