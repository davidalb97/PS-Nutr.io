package pt.ipl.isel.leic.ps.androidclient.ui.tabfragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import pt.ipl.isel.leic.ps.androidclient.ui.adapter.TabAdapter

abstract class TabFragment(
    private val toInflate: Int,
    private val tabFragments: Map<Fragment, String>,
    private val fragmentViewPager: Int,
    private val firstTab: Int
) : Fragment() {

    lateinit var tabPagerAdapter: TabAdapter
    lateinit var viewPager: ViewPager
    lateinit var tabs: TabLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(toInflate, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tabPagerAdapter = TabAdapter(childFragmentManager)
        tabFragments.forEach { tabFragment ->
            tabPagerAdapter.addFragment(tabFragment.key, tabFragment.value)
        }
        viewPager = this.requireView().findViewById(fragmentViewPager)
        tabs = this.requireView().findViewById(firstTab)
        viewPager.adapter = tabPagerAdapter
        tabs.setupWithViewPager(viewPager)

    }
}