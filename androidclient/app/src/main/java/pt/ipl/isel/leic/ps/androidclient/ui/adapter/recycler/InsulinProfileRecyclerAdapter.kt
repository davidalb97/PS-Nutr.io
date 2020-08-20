package pt.ipl.isel.leic.ps.androidclient.ui.adapter.recycler

import android.content.Context
import android.view.View
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.data.model.InsulinProfile
import pt.ipl.isel.leic.ps.androidclient.ui.util.Navigation
import pt.ipl.isel.leic.ps.androidclient.ui.util.getUserSession
import pt.ipl.isel.leic.ps.androidclient.ui.viewholder.InsulinProfileRecyclerViewHolder
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list.InsulinProfilesListViewModel

class InsulinProfileRecyclerAdapter(
    viewModel: InsulinProfilesListViewModel,
    ctx: Context
) : BaseRecyclerAdapter
<InsulinProfile, InsulinProfilesListViewModel, InsulinProfileRecyclerViewHolder>(
    viewModel = viewModel,
    ctx = ctx
) {

    override fun getItemViewId(): Int = R.layout.insulin_profile_card

    override fun newViewHolder(layout: View): InsulinProfileRecyclerViewHolder {
        return object : InsulinProfileRecyclerViewHolder(
            navDestination = Navigation.IGNORE,
            actions = viewModel.actions,
            view = layout,
            ctx = ctx
        ) {
            override fun onDelete(onSuccess: () -> Unit, onError: (Throwable) -> Unit) {
                viewModel.deleteItem(item.profileName, getUserSession())
                    .setOnPostExecute {
                        onSuccess()
                    }.execute()
            }
        }
    }
}