package pt.ipl.isel.leic.ps.androidclient.ui.adapter.recycler

import android.content.Context
import android.view.ViewGroup
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.data.db.dto.InsulinProfileDto
import pt.ipl.isel.leic.ps.androidclient.ui.viewholder.InsulinProfileRecyclerViewHolder
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.InsulinProfilesRecyclerViewModel

class InsulinProfileRecyclerAdapter(
    viewModelInsulin: InsulinProfilesRecyclerViewModel,
    ctx: Context
) : ARecyclerAdapter<InsulinProfileDto, InsulinProfilesRecyclerViewModel, InsulinProfileRecyclerViewHolder>(viewModelInsulin, ctx) {

    override fun onBindViewHolder(holder: InsulinProfileRecyclerViewHolder, position: Int) {
        val item: InsulinProfileDto = viewModel.items[position]
        holder.bindTo(item)
        holder.onDelete = { viewModel.deleteItem(item) }
    }

    override fun getItemViewId(): Int = R.layout.insulin_profile_card

    override fun newViewHolder(layout: ViewGroup): InsulinProfileRecyclerViewHolder =
        InsulinProfileRecyclerViewHolder(layout, ctx)
}