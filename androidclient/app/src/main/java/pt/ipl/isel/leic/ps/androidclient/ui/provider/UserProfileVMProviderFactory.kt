package pt.ipl.isel.leic.ps.androidclient.ui.provider

import android.content.Intent
import android.os.Bundle
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.UserProfileViewModel

const val USER_PROFILE_VIEW_STATE: String = "USER_PROFILE_VIEW_STATE"

class UserProfileVMProviderFactory(
    savedInstanceState: Bundle?,
    intent: Intent
) : AViewModelProviderFactory<UserProfileViewModel>(
    savedInstanceState,
    intent
) {
    override fun getStateName(): String = RESTAURANT_LIST_VIEW_STATE

    override fun getViewModelClass(): Class<UserProfileViewModel> =
        UserProfileViewModel::class.java

    override fun newViewModel(): UserProfileViewModel =
        UserProfileViewModel()
}