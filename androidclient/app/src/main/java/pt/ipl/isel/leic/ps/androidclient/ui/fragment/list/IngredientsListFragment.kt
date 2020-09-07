package pt.ipl.isel.leic.ps.androidclient.ui.fragment.list

import android.os.Bundle
import android.view.View
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.data.model.MealIngredient
import pt.ipl.isel.leic.ps.androidclient.ui.adapter.recycler.meal.MealIngredientRecyclerAdapter
import pt.ipl.isel.leic.ps.androidclient.ui.modular.listener.check.ICheckListener
import pt.ipl.isel.leic.ps.androidclient.ui.modular.listener.check.ICheckListenerOwner
import pt.ipl.isel.leic.ps.androidclient.ui.modular.listener.click.IItemClickListener
import pt.ipl.isel.leic.ps.androidclient.ui.modular.listener.click.IItemClickListenerOwner
import pt.ipl.isel.leic.ps.androidclient.ui.provider.IngredientRecyclerVMProviderFactory
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list.meal.IngredientListViewModel

open class IngredientsListFragment
    : BaseListFragment<MealIngredient, IngredientListViewModel, MealIngredientRecyclerAdapter>(),
    ICheckListenerOwner<MealIngredient>,
    IItemClickListenerOwner<MealIngredient> {

    override val paginated = true
    override val recyclerViewId = R.id.itemList
    override val progressBarId = R.id.progressBar
    override val layout = R.layout.meal_list
    override val noItemsTextViewId = R.id.no_meals_found

    override val vmClass = IngredientListViewModel::class.java
    override val vMProviderFactorySupplier = ::IngredientRecyclerVMProviderFactory

    override var restoredItemPredicator: ((MealIngredient) -> Boolean)? = null
    override var onCheckListener: ICheckListener<MealIngredient>? = null
    override var onClickListener: IItemClickListener<MealIngredient>? = null

    override val recyclerAdapter by lazy {
        MealIngredientRecyclerAdapter(
            viewModel,
            this.requireContext(),
            restoredItemPredicator,
            onCheckListener,
            onClickListener
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setupList()
    }
}