package pt.ipl.isel.leic.ps.androidclient.ui.adapter.recycler.meal

import android.content.Context
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.data.model.MealItem
import pt.ipl.isel.leic.ps.androidclient.ui.adapter.recycler.BaseRecyclerAdapter
import pt.ipl.isel.leic.ps.androidclient.ui.viewholder.meal.BaseMealRecyclerViewHolder
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list.meal.BaseMealListViewModel

abstract class BaseMealRecyclerAdapter
<T : MealItem, VM : BaseMealListViewModel<T>, VH : BaseMealRecyclerViewHolder<T>>(
    viewModel: VM,
    ctx: Context
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
        viewModel.deleteItemById(mealItem.dbId)
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
}
