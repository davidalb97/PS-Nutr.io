package pt.ipl.isel.leic.ps.androidclient.ui.fragment.tab

import android.os.Bundle
import androidx.fragment.app.Fragment
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.data.model.Source
import pt.ipl.isel.leic.ps.androidclient.ui.adapter.TabAdapter
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.list.CustomMealListFragment
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.list.FavoriteMealListFragment
import pt.ipl.isel.leic.ps.androidclient.ui.util.*

class YourMealsSlideScreenFragment : BaseSlideScreenFragment(propagateArguments = false) {

    override fun addTab(mutableList: MutableList<TabAdapter.TabConfig>) {
        mutableList.add(
            TabAdapter.TabConfig(
                title = getString(R.string.tab_custom_meals),
                fragmentSupplier = ::CustomMealListFragment,
                fragmentSetupConsumer = this::setupCustomMeals
            )
        )
        mutableList.add(
            TabAdapter.TabConfig(
                title = getString(R.string.tab_favorite_meals),
                fragmentSupplier = ::FavoriteMealListFragment,
                fragmentSetupConsumer = this::setupFavoriteMeals
            )
        )
        mutableList.add(
            TabAdapter.TabConfig(
                title = getString(R.string.tab_favorite_restaurant_meals),
                fragmentSupplier = ::FavoriteMealListFragment,
                fragmentSetupConsumer = this::setupFavoriteRestaurantMeals
            )
        )
    }

    private fun setupCustomMeals(customMealListFragment: Fragment) {
        val bundle = Bundle()
        bundle.putSource(Source.CUSTOM_MEAL)
        bundle.putNavigation(Navigation.SEND_TO_MEAL_DETAIL)
        bundle.putItemActions(
            ItemAction.CALCULATE, ItemAction.EDIT, ItemAction.DELETE, ItemAction.ADD
        )
        customMealListFragment.arguments = bundle
    }

    private fun setupFavoriteMeals(favoriteMealListFragment: Fragment) {
        val bundle = Bundle()
        bundle.putSource(Source.FAVORITE_MEAL)
        bundle.putNavigation(Navigation.SEND_TO_MEAL_DETAIL)
        favoriteMealListFragment.arguments = bundle
    }

    private fun setupFavoriteRestaurantMeals(favoriteRestaurantMealListFragment: Fragment) {
        val bundle = Bundle()
        bundle.putSource(Source.FAVORITE_RESTAURANT_MEAL)
        bundle.putNavigation(Navigation.SEND_TO_MEAL_DETAIL)
        favoriteRestaurantMealListFragment.arguments = bundle
    }
}