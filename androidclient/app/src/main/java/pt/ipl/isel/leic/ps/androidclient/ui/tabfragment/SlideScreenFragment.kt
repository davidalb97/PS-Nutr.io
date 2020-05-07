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

abstract class SlideScreenFragment(
    private val tabs: Map<Fragment, String>
) : Fragment() {

    lateinit var tabPagerAdapter: TabAdapter
    lateinit var viewPager: ViewPager
    lateinit var tabLayout: TabLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tab, container, false) as ViewGroup
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tabPagerAdapter = TabAdapter(childFragmentManager)

        // Add fragments and tab tiles to show in the tab layout
        tabs.forEach { tab ->
            tabPagerAdapter.addFragment(tab.key, tab.value)
        }

        viewPager = this.requireView().findViewById(R.id.viewPager)
        tabLayout = this.requireView().findViewById(R.id.tab)
        viewPager.adapter = tabPagerAdapter
        tabLayout.setupWithViewPager(viewPager);
    }
}