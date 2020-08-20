package pt.ipl.isel.leic.ps.androidclient.ui.fragment.constant

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.BaseFragment
import pt.ipl.isel.leic.ps.androidclient.ui.provider.UserProfileVMProviderFactory
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.UserSessionViewModel

class ProfileOverviewFragment : BaseFragment() {

    private lateinit var viewModel: UserSessionViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = buildViewModel(savedInstanceState, UserSessionViewModel::class.java)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun getVMProviderFactory(
        savedInstanceState: Bundle?,
        intent: Intent
    ): UserProfileVMProviderFactory {
        return UserProfileVMProviderFactory(arguments, savedInstanceState, intent)
    }

    override fun getLayout() = R.layout.profile_overview_fragment
}