package pt.ipl.isel.leic.ps.androidclient.ui.provider

import android.content.Intent
import android.os.Bundle
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.InsulinProfilesRecyclerViewModel

const val INSULIN_PROFILE_LIST_VIEW_STATE: String = "INSULIN_PROFILE_LIST_VIEW_STATE"

class InsulinProfilesVMProviderFactory(
    savedInstanceState: Bundle?,
    intent: Intent
) : AViewModelProviderFactory<InsulinProfilesRecyclerViewModel>(
    savedInstanceState,
    intent
) {
    override fun getStateName(): String = INSULIN_PROFILE_LIST_VIEW_STATE

    override fun getViewModelClass(): Class<InsulinProfilesRecyclerViewModel> =
        InsulinProfilesRecyclerViewModel::class.java

    override fun newViewModel(): InsulinProfilesRecyclerViewModel =
        InsulinProfilesRecyclerViewModel()


}