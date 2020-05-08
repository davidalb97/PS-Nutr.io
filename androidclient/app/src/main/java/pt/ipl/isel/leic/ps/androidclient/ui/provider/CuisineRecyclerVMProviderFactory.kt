package pt.ipl.isel.leic.ps.androidclient.ui.provider

import android.content.Intent
import android.os.Bundle
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.CuisineRecyclerViewModel

const val CUISINE_LIST_VIEW_STATE: String = "CUISINE_LIST_VIEW_STATE"

class CuisineRecyclerVMProviderFactory(
    savedInstanceState: Bundle?,
    intent: Intent
) : AViewModelProviderFactory<CuisineRecyclerViewModel>(
    savedInstanceState,
    intent
) {
    override fun getStateName(): String = CUISINE_LIST_VIEW_STATE

    override fun getViewModelClass(): Class<CuisineRecyclerViewModel> =
        CuisineRecyclerViewModel::class.java

    override fun newViewModel(): CuisineRecyclerViewModel =
        CuisineRecyclerViewModel()
}