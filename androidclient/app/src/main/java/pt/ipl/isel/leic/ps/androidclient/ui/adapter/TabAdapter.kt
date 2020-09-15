package pt.ipl.isel.leic.ps.androidclient.ui.adapter

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.TabViewModel


/**
 * A "Tab" Adapter to be used by multiple fragments that use a ViewPager
 */
class TabAdapter(
    fragment: FragmentManager,
    val viewModel: TabViewModel
) : FragmentPagerAdapter(
    fragment,
    BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
) {

    private val titles = mutableListOf<String>()
    private val fragmentSuppliers = mutableListOf<() -> Fragment>()
    private val fragmentSetupConsumers = mutableListOf<(Fragment) -> Unit>()
    private val existingFragments = mutableListOf<Fragment?>()

    fun addFragment(tagConfig: TabConfig) {
        titles.add(tagConfig.title)
        fragmentSuppliers.add(tagConfig.fragmentSupplier)
        fragmentSetupConsumers.add(tagConfig.fragmentSetupConsumer)
        existingFragments.add(tagConfig.existingFragment)

        //If it's filling for the first time (first tab adapter, not yet saved to bundle)
        if(viewModel.tags.size < titles.size) {
            viewModel.tags.add(null)
        }
    }

    override fun getCount(): Int {
        return titles.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titles[position]
    }

    override fun getItem(position: Int): Fragment {
        val createdFragment = existingFragments[position] ?: fragmentSuppliers[position]().also {
            //Configure new fragment
            fragmentSetupConsumers[position](it)
        }
        existingFragments[position] = createdFragment
        return createdFragment
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val createdFragment = super.instantiateItem(container, position) as Fragment
        existingFragments[position] = createdFragment
        //Configure new fragment
        fragmentSetupConsumers[position](createdFragment)
        //Set tag defined by super.instantiateItem() call
        viewModel.tags[position] = createdFragment.tag
        return createdFragment
    }

    data class TabConfig(
        val title: String,
        val fragmentSupplier: () -> Fragment,
        var fragmentSetupConsumer: (Fragment) -> Unit,
        var existingFragment: Fragment? = null
    )
}