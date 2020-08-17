package pt.ipl.isel.leic.ps.androidclient.ui.fragment.tab

import pt.ipl.isel.leic.ps.androidclient.ui.fragment.list.CustomMealListFragment
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.list.FavoriteMealListFragment
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.list.MealItemListFragment

class AddMealSlideScreenFragment : ASlideScreenFragment(
    mapOf(
        Pair(MealItemListFragment(), "Meals from restaurants"),
        Pair(FavoriteMealListFragment(), "Favorite Meals"),
        Pair(CustomMealListFragment(), "Custom meals")
    )
)