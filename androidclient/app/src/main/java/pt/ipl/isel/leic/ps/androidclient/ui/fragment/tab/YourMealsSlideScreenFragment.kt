package pt.ipl.isel.leic.ps.androidclient.ui.fragment.tab

import android.os.Bundle
import androidx.fragment.app.Fragment
import pt.ipl.isel.leic.ps.androidclient.data.model.Source
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.list.CustomMealListFragment
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.list.FavoriteMealListFragment
import pt.ipl.isel.leic.ps.androidclient.ui.util.*

class YourMealsSlideScreenFragment : BaseSlideScreenFragment(propagateArguments = false) {

    override fun addFragments(fragments: HashMap<Fragment, String>) {
        fragments[setupCustomMeals()] = "Custom Meals"
        fragments[setupFavoriteMeals()] = "Favorite Meals"
        fragments[setupFavoriteRestaurantMeals()] = "Favorite Restaurant Meals"
    }

    private fun setupCustomMeals(): CustomMealListFragment {
        val bundle = Bundle()
        bundle.putSource(Source.CUSTOM_MEAL)
        bundle.putNavigation(Navigation.SEND_TO_MEAL_DETAIL)
        bundle.putItemActions(
            ItemAction.CALCULATE, ItemAction.EDIT, ItemAction.DELETE, ItemAction.ADD
        )
        return CustomMealListFragment().also {
            it.arguments = bundle
        }
    }

    private fun setupFavoriteMeals(): FavoriteMealListFragment {
        val bundle = Bundle()
        bundle.putSource(Source.FAVORITE_MEAL)
        bundle.putNavigation(Navigation.SEND_TO_MEAL_DETAIL)
        return FavoriteMealListFragment().also {
            it.arguments = bundle
        }
    }

    private fun setupFavoriteRestaurantMeals(): FavoriteMealListFragment {
        val bundle = Bundle()
        bundle.putSource(Source.FAVORITE_RESTAURANT_MEAL)
        bundle.putNavigation(Navigation.SEND_TO_MEAL_DETAIL)
        return FavoriteMealListFragment().also {
            it.arguments = bundle
        }
    }
}