package pt.ipl.isel.leic.ps.androidclient.ui.fragment.tab

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.ui.adapter.TabAdapter
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.BaseViewModelFragment
import pt.ipl.isel.leic.ps.androidclient.ui.modular.IViewModelManager
import pt.ipl.isel.leic.ps.androidclient.ui.provider.BaseViewModelProviderFactory
import pt.ipl.isel.leic.ps.androidclient.ui.provider.TabVMProviderFactory
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.TabViewModel

abstract class BaseSlideScreenFragment(
    private val propagateArguments: Boolean
) : BaseViewModelFragment<TabViewModel>(), IViewModelManager {

    override val layout: Int = R.layout.tab_fragment
    open val viewPagerId: Int = R.id.viewPager
    open val tabLayoutId: Int = R.id.tab
    override val vMProviderFactorySupplier:
                (Bundle?, Bundle?, Intent) -> BaseViewModelProviderFactory = ::TabVMProviderFactory
    override val vmClass: Class<TabViewModel> = TabViewModel::class.java

    private lateinit var tabPagerAdapter: TabAdapter
    private lateinit var viewPager: ViewPager
    private lateinit var tabLayout: TabLayout

    private val tabs = mutableListOf<TabAdapter.TabConfig>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addTab(tabs)

        if (savedInstanceState != null) {
            tabs.forEachIndexed { index, tabConfig ->
                val tag = viewModel.tags[index]
                if (tag != null) {
                    val fragment: Fragment? = parentFragmentManager.findFragmentByTag(tag)
                    if (fragment != null) {
                        tabConfig.fragmentSetupConsumer(fragment)
                        tabConfig.existingFragment = fragment
                    }
                }
            }
        }

        //Propagate the parent fragment arguments to child
        if (propagateArguments) {
            tabs.forEach { config ->
                config.fragmentSetupConsumer = { fragment ->
                    config.fragmentSetupConsumer(fragment)
                    fragment.arguments = arguments
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tabPagerAdapter = TabAdapter(childFragmentManager, viewModel)

        // Add fragments and tab tiles to show in the tab layout
        tabs.forEach(tabPagerAdapter::addFragment)

        viewPager = view.findViewById(viewPagerId)
        tabLayout = view.findViewById(tabLayoutId)
        viewPager.adapter = tabPagerAdapter
        tabLayout.setupWithViewPager(viewPager)
    }

    abstract fun addTab(mutableList: MutableList<TabAdapter.TabConfig>)
}