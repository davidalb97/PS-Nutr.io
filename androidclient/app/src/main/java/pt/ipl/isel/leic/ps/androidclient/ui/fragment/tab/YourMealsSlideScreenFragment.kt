package pt.ipl.isel.leic.ps.androidclient.ui.fragment.tab

import androidx.fragment.app.Fragment
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.list.CustomMealListFragment
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.list.FavoriteMealListFragment
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.list.MealItemListFragment
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.list.RestaurantListFragment

class YourMealsSlideScreenFragment : BaseSlideScreenFragment() {

    override fun addFragments(fragments: HashMap<Fragment, String>) {
        fragments[CustomMealListFragment()] = "Custom Meals"
        fragments[FavoriteMealListFragment()] = "Favorite Meals"
    }

}