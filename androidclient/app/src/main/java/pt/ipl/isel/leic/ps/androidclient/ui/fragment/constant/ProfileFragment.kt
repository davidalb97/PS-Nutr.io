package pt.ipl.isel.leic.ps.androidclient.ui.fragment.constant

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.ui.adapter.TabAdapter
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.recycler.ProfileProfilesRecyclerFragment
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.recycler.ProfileSubmissionsRecyclerFragment

class ProfileFragment : Fragment() {

    lateinit var tabPagerAdapter: TabAdapter
    lateinit var viewPager: ViewPager
    lateinit var tabLayout: TabLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.profile_fragment, container, false) as ViewGroup
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tabPagerAdapter = TabAdapter(childFragmentManager)

        // Add fragments and tab tiles to show in the tab layout
        tabPagerAdapter.addFragment(ProfileOverviewFragment(), "Overview")
        tabPagerAdapter.addFragment(ProfileProfilesRecyclerFragment(), "Profiles")
        tabPagerAdapter.addFragment(ProfileSubmissionsRecyclerFragment(), "Submissions")


        tabLayout = view.findViewById(R.id.profile_tab)
        viewPager = view.findViewById(R.id.profile_view_pager)
        viewPager.adapter = tabPagerAdapter
        tabLayout.setupWithViewPager(viewPager);
    }
}