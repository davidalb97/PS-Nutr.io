package pt.ipl.isel.leic.ps.androidclient.ui.adapter.recycler.meal

import android.content.Context
import android.view.View
import pt.ipl.isel.leic.ps.androidclient.data.model.MealIngredient
import pt.ipl.isel.leic.ps.androidclient.ui.modular.listener.check.ICheckListener
import pt.ipl.isel.leic.ps.androidclient.ui.modular.listener.click.IItemClickListener
import pt.ipl.isel.leic.ps.androidclient.ui.viewholder.meal.IngredientRecyclerViewHolder
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list.meal.IngredientListViewModel

class MealIngredientRecyclerAdapter(
    viewModel: IngredientListViewModel,
    ctx: Context,
    itemCheckPredicator: ((MealIngredient) -> Boolean)? = null,
    onCheckListener: ICheckListener<MealIngredient>? = null,
    onClickListener: IItemClickListener<MealIngredient>? = null
) : BaseMealRecyclerAdapter<MealIngredient, IngredientListViewModel, IngredientRecyclerViewHolder>(
    viewModel = viewModel,
    ctx = ctx,
    itemCheckPredicator = itemCheckPredicator,
    onCheckListener = onCheckListener,
    onClickListener = onClickListener
) {

    override fun newViewHolder(layout: View): IngredientRecyclerViewHolder {
        return object : IngredientRecyclerViewHolder(
            navDestination = viewModel.navDestination,
            actions = viewModel.actions,
            view = layout,
            ctx = ctx
        ) {
            init {
                this@MealIngredientRecyclerAdapter.setupOnClick(this)
            }

            override fun onDelete(onSuccess: () -> Unit, onError: (Throwable) -> Unit) =
                throw UnsupportedOperationException()

            override fun onFavorite(onSuccess: () -> Unit, onError: (Throwable) -> Unit) =
                this@MealIngredientRecyclerAdapter.onFavorite(this.item, onSuccess, onError)

            override fun onReport(
                reportStr: String,
                onSuccess: () -> Unit,
                onError: (Throwable) -> Unit
            ) = this@MealIngredientRecyclerAdapter.onReport(
                this.item,
                reportStr,
                onSuccess,
                onError
            )

            override fun onCheck(isChecked: Boolean) =
                this@MealIngredientRecyclerAdapter.onCheck(
                    viewHolder = this,
                    isChecked = isChecked
                )

            override fun isRestored(): Boolean =
                this@MealIngredientRecyclerAdapter.isAlreadyChecked(item)
        }
    }
}
