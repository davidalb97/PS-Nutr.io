package pt.ipl.isel.leic.ps.androidclient.ui.adapter.recycler.meal

import android.content.Context
import android.view.View
import pt.ipl.isel.leic.ps.androidclient.data.model.MealInfo
import pt.ipl.isel.leic.ps.androidclient.ui.viewholder.meal.MealInfoRecyclerViewHolder
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list.meal.MealInfoListViewModel

class MealInfoRecyclerAdapter(
    viewModel: MealInfoListViewModel,
    ctx: Context
) : BaseMealRecyclerAdapter<MealInfo, MealInfoListViewModel, MealInfoRecyclerViewHolder>(
    viewModel = viewModel,
    ctx = ctx
) {

    override fun newViewHolder(layout: View): MealInfoRecyclerViewHolder {
        return object : MealInfoRecyclerViewHolder(
            navDestination = viewModel.navDestination,
            actions = viewModel.actions,
            view = layout,
            ctx = ctx
        ) {
            override fun onDelete(onSuccess: () -> Unit, onError: (Throwable) -> Unit) =
                this@MealInfoRecyclerAdapter.onDelete(this.item, onSuccess)

            override fun onFavorite(onSuccess: () -> Unit, onError: (Throwable) -> Unit) =
                this@MealInfoRecyclerAdapter.onFavorite(this.item, onSuccess, onError)

            override fun onReport(
                reportStr: String,
                onSuccess: () -> Unit,
                onError: (Throwable) -> Unit
            ) = this@MealInfoRecyclerAdapter.onReport(
                mealItem = this.item,
                reportStr = reportStr,
                onSuccess = onSuccess,
                onError = onError
            )
        }
    }
}
