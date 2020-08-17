package pt.ipl.isel.leic.ps.androidclient.ui.adapter.spinner.pick

import android.content.Context
import android.view.View
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.data.model.MealIngredient
import pt.ipl.isel.leic.ps.androidclient.ui.util.Navigation
import pt.ipl.isel.leic.ps.androidclient.ui.viewholder.BaseRecyclerViewHolder
import pt.ipl.isel.leic.ps.androidclient.ui.viewholder.meal.IngredientRecyclerViewHolder
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list.pick.IngredientPickViewModel

class IngredientPickSpinnerAdapter(
    viewModel: IngredientPickViewModel,
    ctx: Context
) : BasePickSpinnerAdapter<MealIngredient, IngredientPickViewModel>(
    viewModel = viewModel,
    ctx = ctx
) {

    override fun getDropDownItemLayoutId(): Int = R.layout.meal_card

    override fun newDropDownItemViewHolder(layout: View): BaseRecyclerViewHolder<MealIngredient> {
        return object : IngredientRecyclerViewHolder(
            navDestination = Navigation.IGNORE,
            actions = emptyList(),
            view = layout,
            ctx = ctx
        ) {
            init {
                this@IngredientPickSpinnerAdapter.setOnClickHandler(this)
            }

            override fun onDelete(onSuccess: () -> Unit, onError: (Throwable) -> Unit) {}
            override fun onFavorite(onSuccess: () -> Unit, onError: (Throwable) -> Unit) {}
            override fun onReport(
                reportStr: String,
                onSuccess: () -> Unit,
                onError: (Throwable) -> Unit
            ) {
            }
        }
    }
}