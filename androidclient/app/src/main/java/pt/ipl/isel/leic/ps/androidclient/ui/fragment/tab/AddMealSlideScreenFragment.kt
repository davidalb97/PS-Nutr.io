package pt.ipl.isel.leic.ps.androidclient.ui.fragment.tab

import androidx.fragment.app.Fragment
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.list.CustomMealListFragment
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.list.FavoriteMealListFragment
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.list.IngredientsListFragment
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.list.MealItemListFragment

class AddMealSlideScreenFragment : BaseSlideScreenFragment() {
    override fun addFragments(fragments: HashMap<Fragment, String>) {
        fragments[IngredientsListFragment()] = "Meal Ingredients"
        fragments[MealItemListFragment()] = "Suggested Meals"
        fragments[FavoriteMealListFragment()] = "Favorite Meals"
        fragments[CustomMealListFragment()] = "Custom meals"
    }
}