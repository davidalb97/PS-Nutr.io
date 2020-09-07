package pt.ipl.isel.leic.ps.androidclient.ui.fragment.list

import android.os.Bundle
import android.view.View
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.data.model.MealItem
import pt.ipl.isel.leic.ps.androidclient.ui.adapter.recycler.meal.MealItemRecyclerAdapter
import pt.ipl.isel.leic.ps.androidclient.ui.modular.filter.IItemListFilter
import pt.ipl.isel.leic.ps.androidclient.ui.modular.filter.IItemListFilterOwner
import pt.ipl.isel.leic.ps.androidclient.ui.modular.listener.check.ICheckListener
import pt.ipl.isel.leic.ps.androidclient.ui.modular.listener.check.ICheckListenerOwner
import pt.ipl.isel.leic.ps.androidclient.ui.modular.listener.click.IItemClickListener
import pt.ipl.isel.leic.ps.androidclient.ui.modular.listener.click.IItemClickListenerOwner
import pt.ipl.isel.leic.ps.androidclient.ui.provider.MealRecyclerVMProviderFactory
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list.meal.MealItemListViewModel

open class MealItemListFragment :
    BaseListFragment<MealItem, MealItemListViewModel, MealItemRecyclerAdapter>(),
    ICheckListenerOwner<MealItem>,
    IItemClickListenerOwner<MealItem>,
    IItemListFilterOwner<MealItem> {

    override val paginated = true
    override val recyclerViewId = R.id.itemList
    override val progressBarId = R.id.progressBar
    override val layout = R.layout.meal_list
    override val noItemsTextViewId = R.id.no_meals_found

    override var restoredItemPredicator: ((MealItem) -> Boolean)? = null
    override var onCheckListener: ICheckListener<MealItem>? = null
    override var onClickListener: IItemClickListener<MealItem>? = null
    override var itemFilter: IItemListFilter<MealItem>? = null

    override val vmClass = MealItemListViewModel::class.java
    override val vMProviderFactorySupplier = ::MealRecyclerVMProviderFactory
    override val recyclerAdapter by lazy {
        MealItemRecyclerAdapter(
            viewModel,
            this.requireContext(),
            restoredItemPredicator,
            onCheckListener,
            onClickListener
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.liveDataHandler.filter = itemFilter
        viewModel.setupList()
    }
}