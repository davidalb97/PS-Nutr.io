package pt.ipl.isel.leic.ps.androidclient.ui.adapter.recycler.meal

import android.content.Context
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.data.model.MealItem
import pt.ipl.isel.leic.ps.androidclient.ui.adapter.recycler.BaseRecyclerAdapter
import pt.ipl.isel.leic.ps.androidclient.ui.modular.listener.check.ICheckListener
import pt.ipl.isel.leic.ps.androidclient.ui.modular.listener.click.IItemClickListener
import pt.ipl.isel.leic.ps.androidclient.ui.viewholder.meal.BaseMealRecyclerViewHolder
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list.meal.BaseMealListViewModel

abstract class BaseMealRecyclerAdapter
<T : MealItem, VM : BaseMealListViewModel<T>, VH : BaseMealRecyclerViewHolder<T>>(
    viewModel: VM,
    ctx: Context,
    val itemCheckPredicator: ((T) -> Boolean)? = null,
    val onCheckListener: ICheckListener<T>? = null,
    val onClickListener: IItemClickListener<T>? = null
) : BaseRecyclerAdapter<T, VM, VH>(
    viewModel = viewModel,
    ctx = ctx
) {

    override fun getItemViewId(): Int = R.layout.meal_card

    fun onFavorite(mealItem: MealItem, onSuccess: () -> Unit, onError: (Throwable) -> Unit) {
        viewModel.putFavorite(
            mealItem = mealItem,
            onSuccess = onSuccess,
            onError = onError
        )
    }

    fun onDelete(mealItem: MealItem, onSuccess: () -> Unit) {
        viewModel.deleteItemById(requireNotNull(mealItem.dbId))
            .setOnPostExecute { onSuccess() }
            .execute()
    }

    fun onReport(
        mealItem: MealItem,
        reportStr: String,
        onSuccess: () -> Unit,
        onError: (Throwable) -> Unit
    ) {
        viewModel.report(
            mealItem = mealItem,
            reportStr = reportStr,
            onSuccess = onSuccess,
            onError = onError
        )
    }

    fun onCheck(viewHolder: BaseMealRecyclerViewHolder<T>, isChecked: Boolean) {
        onCheckListener?.onCheckChange(viewHolder.item, isChecked) {
            viewHolder.bindingAdapter?.notifyItemChanged(viewHolder.layoutPosition)
        }
    }

    fun setupOnClick(viewHolder: BaseMealRecyclerViewHolder<T>) {
        if (onClickListener != null) {
            viewHolder.onClickListener = viewHolder.onClickListener?.appendListener(onClickListener)
        }
    }

    fun isAlreadyChecked(item: T) = itemCheckPredicator?.invoke(item) ?: false
}
