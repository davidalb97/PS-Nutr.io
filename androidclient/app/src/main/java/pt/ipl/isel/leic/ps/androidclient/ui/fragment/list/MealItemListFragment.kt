package pt.ipl.isel.leic.ps.androidclient.ui.fragment.list

import android.content.Intent
import android.os.Bundle
import android.view.View
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.data.model.MealItem
import pt.ipl.isel.leic.ps.androidclient.ui.adapter.recycler.meal.MealItemRecyclerAdapter
import pt.ipl.isel.leic.ps.androidclient.ui.modular.listener.check.ICheckListener
import pt.ipl.isel.leic.ps.androidclient.ui.modular.listener.check.ICheckListenerOwner
import pt.ipl.isel.leic.ps.androidclient.ui.modular.listener.click.IItemClickListener
import pt.ipl.isel.leic.ps.androidclient.ui.modular.listener.click.IItemClickListenerOwner
import pt.ipl.isel.leic.ps.androidclient.ui.provider.BaseViewModelProviderFactory
import pt.ipl.isel.leic.ps.androidclient.ui.provider.MealRecyclerVMProviderFactory
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list.meal.MealItemListViewModel

open class MealItemListFragment :
    BaseListFragment<MealItem, MealItemListViewModel, MealItemRecyclerAdapter>(),
    ICheckListenerOwner<MealItem>,
    IItemClickListenerOwner<MealItem> {

    override var onCheckListener: ICheckListener<MealItem>? = null
    override var onClickListener: IItemClickListener<MealItem>? = null
    override val recyclerAdapter by lazy {
        MealItemRecyclerAdapter(
            recyclerViewModel,
            this.requireContext(),
            onCheckListener,
            onClickListener
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerViewModel.update()
    }

    override fun getRecyclerId() = R.id.itemList

    override fun getProgressBarId() = R.id.progressBar

    override fun getLayout() = R.layout.meal_list

    override fun getNoItemsLabelId() = R.id.no_meals_found

    override fun getVMProviderFactory(
        savedInstanceState: Bundle?,
        intent: Intent
    ): BaseViewModelProviderFactory {
        return MealRecyclerVMProviderFactory(
            arguments,
            savedInstanceState,
            intent
        )
    }

    override fun getRecyclerViewModelClass() = MealItemListViewModel::class.java

}