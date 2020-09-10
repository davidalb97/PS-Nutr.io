package pt.ipl.isel.leic.ps.androidclient.ui.fragment.tab

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.encryptedSharedPreferences
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.constant.ProfileOverviewFragment
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.list.InsulinProfilesListFragment
import pt.ipl.isel.leic.ps.androidclient.ui.util.ItemAction
import pt.ipl.isel.leic.ps.androidclient.ui.util.getUsername
import pt.ipl.isel.leic.ps.androidclient.ui.util.putItemActions

class ProfileFragment : BaseSlideScreenFragment(propagateArguments = false) {

    override val layout = R.layout.profile_fragment
    override val viewPagerId: Int = R.id.profile_view_pager
    override val tabLayoutId: Int = R.id.profile_tab

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userNameView = view.findViewById<TextView>(R.id.user_name)
        userNameView.text = encryptedSharedPreferences.getUsername()
    }

    override fun addFragments(fragments: HashMap<Fragment, String>) {
        fragments[ProfileOverviewFragment()] = "Overview"
        val insulinProfilesFragment = InsulinProfilesListFragment()
        insulinProfilesFragment.arguments = Bundle().also { bundle ->
            bundle.putItemActions(ItemAction.EDIT, ItemAction.DELETE)
        }
        fragments[insulinProfilesFragment] = "Profiles"
    }
}