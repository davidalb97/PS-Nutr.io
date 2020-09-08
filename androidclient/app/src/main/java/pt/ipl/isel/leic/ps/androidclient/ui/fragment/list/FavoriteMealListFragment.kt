package pt.ipl.isel.leic.ps.androidclient.ui.fragment.list

import android.os.Bundle
import android.view.View
import pt.ipl.isel.leic.ps.androidclient.ui.modular.IUserSession
import pt.ipl.isel.leic.ps.androidclient.ui.provider.FavoriteMealRecyclerVMProviderFactory

class FavoriteMealListFragment : MealItemListFragment(), IUserSession {

    override val vMProviderFactorySupplier = ::FavoriteMealRecyclerVMProviderFactory

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.initRecyclerView(view)

        ensureUserSession(requireContext(), failConsumer = {
            super.recyclerHandler.onNoRecyclerItems()
        }) {
            viewModel.setupList()
        }
    }
}