package pt.ipl.isel.leic.ps.androidclient.ui.fragment.tab

import pt.ipl.isel.leic.ps.androidclient.ui.fragment.list.IngredientsListFragment
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.list.MealItemListFragment

class MealSlideScreenFragment : ASlideScreenFragment(
    mapOf(
        Pair(MealItemListFragment(), "Suggested Meals"),
        Pair(IngredientsListFragment(), "Meal Ingredients")
    )
)