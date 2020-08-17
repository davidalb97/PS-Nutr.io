package pt.ipl.isel.leic.ps.androidclient.ui.fragment.list

import android.content.Intent
import android.os.Bundle
import android.view.View
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.data.model.MealInfo
import pt.ipl.isel.leic.ps.androidclient.ui.adapter.recycler.meal.MealInfoRecyclerAdapter
import pt.ipl.isel.leic.ps.androidclient.ui.provider.AViewModelProviderFactory
import pt.ipl.isel.leic.ps.androidclient.ui.provider.IngredientRecyclerVMProviderFactory
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list.meal.IngredientListViewModel

open class IngredientsListFragment
    : BaseListFragment<MealInfo, IngredientListViewModel, MealInfoRecyclerAdapter>() {

    override val recyclerAdapter by lazy {
        MealInfoRecyclerAdapter(
            recyclerViewModel,
            this.requireContext()
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
    ): AViewModelProviderFactory {
        return IngredientRecyclerVMProviderFactory(
            arguments,
            savedInstanceState,
            intent
        )
    }
}