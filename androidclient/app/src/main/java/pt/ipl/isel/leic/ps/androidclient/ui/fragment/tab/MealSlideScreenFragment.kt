package pt.ipl.isel.leic.ps.androidclient.ui.fragment.tab

import androidx.fragment.app.Fragment
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.list.IngredientsListFragment
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.list.MealItemListFragment

class MealSlideScreenFragment : BaseSlideScreenFragment(propagateArguments = true) {

    override fun addFragments(fragments: HashMap<Fragment, String>) {
        fragments[MealItemListFragment()] = "Suggested Meals"
        fragments[IngredientsListFragment()] = "Meal Ingredients"
    }
}