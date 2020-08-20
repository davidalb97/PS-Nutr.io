package pt.ipl.isel.leic.ps.androidclient.ui.adapter.recycler.pick

import android.content.Context
import android.view.View
import pt.ipl.isel.leic.ps.androidclient.data.model.MealIngredient
import pt.ipl.isel.leic.ps.androidclient.ui.util.Navigation
import pt.ipl.isel.leic.ps.androidclient.ui.viewholder.FilterRecyclerViewHolder
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list.pick.IngredientPickViewModel

class IngredientPickRecyclerAdapter(
    model: IngredientPickViewModel,
    ctx: Context
) : BasePickRecyclerAdapter<MealIngredient, IngredientPickViewModel>(
    viewModel = model,
    ctx = ctx
) {
    override fun newViewHolder(layout: View): FilterRecyclerViewHolder<MealIngredient> {
        return object : FilterRecyclerViewHolder<MealIngredient>(
            navDestination = Navigation.IGNORE,
            view = layout,
            ctx = ctx
        ) {
            override fun onDelete() = viewModel.unPick(item)
            override fun getName(): String = item.name
        }
    }
}