package pt.ipl.isel.leic.ps.androidclient.ui.fragment.tab

import android.os.Bundle
import androidx.fragment.app.Fragment
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.data.model.Source
import pt.ipl.isel.leic.ps.androidclient.ui.adapter.TabAdapter.TabConfig
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.list.IngredientsListFragment
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.list.MealItemListFragment
import pt.ipl.isel.leic.ps.androidclient.ui.util.*

class MealSlideScreenFragment : BaseSlideScreenFragment(propagateArguments = false) {

    override fun addTab(mutableList: MutableList<TabConfig>) {
        mutableList.add(
            TabConfig(
                title = getString(R.string.tab_suggested_meals),
                fragmentSupplier = ::MealItemListFragment,
                fragmentSetupConsumer = this::setupMealItemFragment
            )
        )
        mutableList.add(
            TabConfig(
                title = getString(R.string.tab_suggested_ingredients),
                fragmentSupplier = ::IngredientsListFragment,
                fragmentSetupConsumer = this::setupIngredientItemFragment
            )
        )
    }

    private fun setupMealItemFragment(mealItemFragment: Fragment) {
        val bundle = Bundle()
        bundle.putSource(Source.API)
        bundle.putNavigation(Navigation.SEND_TO_MEAL_DETAIL)
        bundle.putItemActions(ItemAction.FAVORITE, ItemAction.CALCULATE)
        mealItemFragment.arguments = bundle
    }

    private fun setupIngredientItemFragment(ingredientItemFragment: Fragment) {
        val bundle = Bundle()
        bundle.putItemActions(ItemAction.FAVORITE, ItemAction.CALCULATE)
        ingredientItemFragment.arguments = bundle
    }
}