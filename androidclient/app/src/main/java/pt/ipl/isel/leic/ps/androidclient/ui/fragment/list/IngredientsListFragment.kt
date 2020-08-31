package pt.ipl.isel.leic.ps.androidclient.ui.fragment.list

import android.content.Intent
import android.os.Bundle
import android.view.View
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.data.model.MealIngredient
import pt.ipl.isel.leic.ps.androidclient.ui.adapter.recycler.meal.MealIngredientRecyclerAdapter
import pt.ipl.isel.leic.ps.androidclient.ui.modular.listener.check.ICheckListener
import pt.ipl.isel.leic.ps.androidclient.ui.modular.listener.check.ICheckListenerOwner
import pt.ipl.isel.leic.ps.androidclient.ui.modular.listener.click.IItemClickListener
import pt.ipl.isel.leic.ps.androidclient.ui.modular.listener.click.IItemClickListenerOwner
import pt.ipl.isel.leic.ps.androidclient.ui.provider.BaseViewModelProviderFactory
import pt.ipl.isel.leic.ps.androidclient.ui.provider.IngredientRecyclerVMProviderFactory
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list.meal.IngredientListViewModel

open class IngredientsListFragment
    : BaseListFragment<MealIngredient, IngredientListViewModel, MealIngredientRecyclerAdapter>(),
    ICheckListenerOwner<MealIngredient>,
    IItemClickListenerOwner<MealIngredient> {

    override var restoredItemPredicator: ((MealIngredient) -> Boolean)? = null
    override var onCheckListener: ICheckListener<MealIngredient>? = null
    override var onClickListener: IItemClickListener<MealIngredient>? = null
    override val recyclerAdapter by lazy {
        MealIngredientRecyclerAdapter(
            recyclerViewModel,
            this.requireContext(),
            restoredItemPredicator,
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

    override fun getRecyclerViewModelClass() = IngredientListViewModel::class.java

    override fun getVMProviderFactory(
        savedInstanceState: Bundle?,
        intent: Intent
    ): BaseViewModelProviderFactory {
        return IngredientRecyclerVMProviderFactory(
            arguments,
            savedInstanceState,
            intent
        )
    }
}