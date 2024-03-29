package pt.ipl.isel.leic.ps.androidclient.ui.adapter.recycler.meal

import android.content.Context
import android.view.View
import pt.ipl.isel.leic.ps.androidclient.data.model.MealItem
import pt.ipl.isel.leic.ps.androidclient.ui.modular.listener.check.ICheckListener
import pt.ipl.isel.leic.ps.androidclient.ui.modular.listener.click.IItemClickListener
import pt.ipl.isel.leic.ps.androidclient.ui.util.Navigation
import pt.ipl.isel.leic.ps.androidclient.ui.viewholder.meal.MealItemRecyclerViewHolder
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list.meal.MealItemListViewModel

class MealItemRecyclerAdapter(
    viewModel: MealItemListViewModel,
    ctx: Context,
    itemCheckPredicator: ((MealItem) -> Boolean)? = null,
    onCheckListener: ICheckListener<MealItem>? = null,
    onClickListener: IItemClickListener<MealItem>? = null,
    onEditNavigation: Navigation? = null
) : BaseMealRecyclerAdapter<MealItem, MealItemListViewModel, MealItemRecyclerViewHolder>(
    viewModel = viewModel,
    ctx = ctx,
    itemCheckPredicator = itemCheckPredicator,
    onCheckListener = onCheckListener,
    onClickListener = onClickListener,
    onEditNavigation = onEditNavigation
) {

    override fun newViewHolder(layout: View): MealItemRecyclerViewHolder {
        return object : MealItemRecyclerViewHolder(
            onEditNavigation = onEditNavigation,
            navDestination = viewModel.navDestination,
            actions = viewModel.actions,
            view = layout,
            ctx = ctx
        ) {
            init {
                this@MealItemRecyclerAdapter.setupOnClick(this)
            }

            override fun onDelete(onSuccess: () -> Unit, onError: (Throwable) -> Unit) {
                viewModel.deleteCustomMeal(item, onSuccess, onError)
            }

            override fun onFavorite(onSuccess: () -> Unit, onError: (Throwable) -> Unit) =
                this@MealItemRecyclerAdapter.onFavorite(this.item, onSuccess, onError)

            override fun onReport(
                reportStr: String,
                onSuccess: () -> Unit,
                onError: (Throwable) -> Unit
            ) = this@MealItemRecyclerAdapter.onReport(this.item, reportStr, onSuccess, onError)

            override fun onCheck(isChecked: Boolean) =
                this@MealItemRecyclerAdapter.onCheck(
                    viewHolder = this,
                    isChecked = isChecked
                )

            override fun isRestored(): Boolean =
                this@MealItemRecyclerAdapter.isAlreadyChecked(item)
        }
    }
}
