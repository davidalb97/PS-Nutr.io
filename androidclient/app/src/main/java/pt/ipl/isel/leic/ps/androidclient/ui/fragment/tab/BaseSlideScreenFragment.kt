package pt.ipl.isel.leic.ps.androidclient.ui.fragment.tab

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.ui.adapter.TabAdapter
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.BaseFragment

abstract class BaseSlideScreenFragment(private val propagateArguments: Boolean) : BaseFragment() {

    override val layout: Int = R.layout.tab_fragment
    open val viewPagerId: Int = R.id.viewPager
    open val tabLayoutId: Int = R.id.tab

    private lateinit var tabPagerAdapter: TabAdapter
    private lateinit var viewPager: ViewPager
    private lateinit var tabLayout: TabLayout

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tabPagerAdapter = TabAdapter(childFragmentManager)

        val tabs = hashMapOf<Fragment, String>()

        addFragments(tabs)

        //Sets the fragment mode
        if (propagateArguments) {
            tabs.keys.forEach { fragment ->
                fragment.arguments = arguments
            }
        }

        // Add fragments and tab tiles to show in the tab layout
        tabs.forEach { tab ->
            tabPagerAdapter.addFragment(tab.key, tab.value)
        }


        viewPager = view.findViewById(viewPagerId)
        tabLayout = view.findViewById(tabLayoutId)
        viewPager.adapter = tabPagerAdapter
        tabLayout.setupWithViewPager(viewPager)
    }

    abstract fun addFragments(fragments: HashMap<Fragment, String>)
}