package pt.ipl.isel.leic.ps.androidclient.ui.fragment.tab

import android.os.Bundle
import androidx.fragment.app.Fragment
import pt.ipl.isel.leic.ps.androidclient.data.model.Source
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.list.IngredientsListFragment
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.list.MealItemListFragment
import pt.ipl.isel.leic.ps.androidclient.ui.util.*

class MealSlideScreenFragment : BaseSlideScreenFragment(propagateArguments = false) {

    override fun addFragments(fragments: HashMap<Fragment, String>) {
        fragments[setupMealItemFragment()] = "Suggested Meals"
        fragments[setupIngredientItemFragment()] = "Suggested Ingredients"
    }

    private fun setupMealItemFragment(): MealItemListFragment {
        val mealItemFragment = MealItemListFragment()
        val bundle = Bundle()
        bundle.putSource(Source.API)
        bundle.putNavigation(Navigation.SEND_TO_MEAL_DETAIL)
        bundle.putItemActions(ItemAction.FAVORITE, ItemAction.CALCULATE)
        mealItemFragment.arguments = bundle
        return mealItemFragment
    }

    private fun setupIngredientItemFragment(): IngredientsListFragment {
        val ingredientItemFragment = IngredientsListFragment()
        val bundle = Bundle()
        bundle.putItemActions(ItemAction.FAVORITE, ItemAction.CALCULATE)
        ingredientItemFragment.arguments = bundle
        return ingredientItemFragment
    }
}