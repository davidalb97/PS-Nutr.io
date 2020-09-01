package pt.ipl.isel.leic.ps.androidclient.ui.fragment.constant

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.encryptedSharedPreferences
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.ui.adapter.TabAdapter
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.BaseFragment
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.list.InsulinProfilesListFragment
import pt.ipl.isel.leic.ps.androidclient.ui.provider.UserProfileVMProviderFactory
import pt.ipl.isel.leic.ps.androidclient.ui.util.ItemAction
import pt.ipl.isel.leic.ps.androidclient.ui.util.getUsername
import pt.ipl.isel.leic.ps.androidclient.ui.util.putItemActions

class ProfileFragment : BaseFragment() {

    private lateinit var tabPagerAdapter: TabAdapter
    private lateinit var viewPager: ViewPager
    private lateinit var tabLayout: TabLayout

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userNameView = view.findViewById<TextView>(R.id.user_name)

        userNameView.text = encryptedSharedPreferences.getUsername()

        tabPagerAdapter = TabAdapter(childFragmentManager)

        // Add fragments and tab tiles to show in the tab layout
        tabPagerAdapter.addFragment(ProfileOverviewFragment(), "Overview")
        tabPagerAdapter.addFragment(InsulinProfilesListFragment().also { fragment ->
            fragment.arguments = Bundle().also { bundle ->
                bundle.putItemActions(ItemAction.EDIT, ItemAction.DELETE)
            }
        }, "Profiles")

        tabLayout = view.findViewById(R.id.profile_tab)
        viewPager = view.findViewById(R.id.profile_view_pager)
        viewPager.adapter = tabPagerAdapter
        tabLayout.setupWithViewPager(viewPager)

    }

    override fun getLayout() = R.layout.profile_fragment

    override fun getVMProviderFactory(
        savedInstanceState: Bundle?,
        intent: Intent
    ): UserProfileVMProviderFactory {
        return UserProfileVMProviderFactory(arguments, savedInstanceState, intent)
    }
}