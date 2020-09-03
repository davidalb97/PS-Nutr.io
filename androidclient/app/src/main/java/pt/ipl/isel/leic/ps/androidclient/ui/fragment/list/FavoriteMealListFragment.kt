package pt.ipl.isel.leic.ps.androidclient.ui.fragment.list

import android.content.Intent
import android.os.Bundle
import android.view.View
import pt.ipl.isel.leic.ps.androidclient.ui.modular.IUserSession
import pt.ipl.isel.leic.ps.androidclient.ui.provider.FavoriteMealRecyclerVMProviderFactory

class FavoriteMealListFragment : MealItemListFragment(), IUserSession {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.initRecyclerView(view)

        ensureUserSession(requireContext(), failConsumer = {
            super.recyclerHandler.onNoRecyclerItems()
        }) {
            recyclerViewModel.update()
        }
    }

    override fun getVMProviderFactory(
        savedInstanceState: Bundle?,
        intent: Intent
    ): FavoriteMealRecyclerVMProviderFactory {
        return FavoriteMealRecyclerVMProviderFactory(
            arguments,
            savedInstanceState,
            intent
        )
    }
}