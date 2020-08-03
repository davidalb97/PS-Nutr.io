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

const val CALCULATOR_BUNDLE_FLAG = "calculatorMode"

abstract class ASlideScreenFragment(
    private val tabs: Map<Fragment, String>,
    private val calculatorMode: Boolean
) : Fragment() {

    lateinit var tabPagerAdapter: TabAdapter
    lateinit var viewPager: ViewPager
    lateinit var tabLayout: TabLayout

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

        //Sets the fragment mode
        tabs.keys.forEach { fragment ->
            val bundle = Bundle()
            bundle.putBoolean(CALCULATOR_BUNDLE_FLAG, calculatorMode)
            fragment.arguments = bundle
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
}