package pt.ipl.isel.leic.ps.androidclient.ui.fragment.constant

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.ui.adapter.TabAdapter
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.recycler.room.InsulinProfilesRecyclerFragment
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.recycler.room.ProfileSubmissionsRecyclerFragment
import pt.ipl.isel.leic.ps.androidclient.ui.provider.UserProfileVMProviderFactory
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.UserProfileViewModel

class ProfileFragment : Fragment() {

    lateinit var tabPagerAdapter: TabAdapter
    lateinit var viewPager: ViewPager
    lateinit var tabLayout: TabLayout

    lateinit var viewModel: UserProfileViewModel

    private fun buildViewModel(savedInstanceState: Bundle?) {
        val rootActivity = this.requireActivity()
        val factory = UserProfileVMProviderFactory(savedInstanceState, rootActivity.intent)
        viewModel =
            ViewModelProvider(rootActivity, factory)[UserProfileViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        buildViewModel(savedInstanceState)
        return inflater.inflate(R.layout.profile_fragment, container, false) as ViewGroup
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userNameView = view.findViewById<TextView>(R.id.user_name)

        viewModel.userId = 3
        viewModel.observe(this) {
            if (it.isEmpty()) {
                Toast.makeText(context, "No users configured!", Toast.LENGTH_SHORT)
                    .show()
            } else {
                userNameView.text = it.first().name
            }
        }
        viewModel.update()

        tabPagerAdapter = TabAdapter(childFragmentManager)

        // Add fragments and tab tiles to show in the tab layout
        tabPagerAdapter.addFragment(ProfileOverviewFragment(), "Overview")
        tabPagerAdapter.addFragment(InsulinProfilesRecyclerFragment(), "Profiles")
        tabPagerAdapter.addFragment(ProfileSubmissionsRecyclerFragment(), "Submissions")

        tabLayout = view.findViewById(R.id.profile_tab)
        viewPager = view.findViewById(R.id.profile_view_pager)
        viewPager.adapter = tabPagerAdapter
        tabLayout.setupWithViewPager(viewPager);
    }
}