package pt.ipl.isel.leic.ps.androidclient.ui.fragment.tab

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.ui.adapter.TabAdapter
import pt.ipl.isel.leic.ps.androidclient.ui.util.Logger

abstract class BaseSlideScreenFragment(private val propagateArguments: Boolean): Fragment() {

    val log: Logger by lazy { Logger(javaClass) }

    private lateinit var tabPagerAdapter: TabAdapter
    private lateinit var viewPager: ViewPager
    private lateinit var tabLayout: TabLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.tab_fragment, container, false) as ViewGroup
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tabPagerAdapter = TabAdapter(childFragmentManager)

        val tabs = hashMapOf<Fragment, String>()

        addFragments(tabs)

        //Sets the fragment mode
        if(propagateArguments) {
            tabs.keys.forEach { fragment ->
                fragment.arguments = arguments
            }
        }

        // Add fragments and tab tiles to show in the tab layout
        tabs.forEach { tab ->
            tabPagerAdapter.addFragment(tab.key, tab.value)
        }


        viewPager = view.findViewById(R.id.viewPager)
        tabLayout = view.findViewById(R.id.tab)
        viewPager.adapter = tabPagerAdapter
        tabLayout.setupWithViewPager(viewPager)
    }

    abstract fun addFragments(fragments: HashMap<Fragment, String>)
}