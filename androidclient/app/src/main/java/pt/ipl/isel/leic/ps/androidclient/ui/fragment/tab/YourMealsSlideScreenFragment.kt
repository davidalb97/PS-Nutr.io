package pt.ipl.isel.leic.ps.androidclient.ui.fragment.tab

import pt.ipl.isel.leic.ps.androidclient.ui.fragment.list.CustomMealListFragment
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.list.FavoriteMealListFragment

class YourMealsSlideScreenFragment : ASlideScreenFragment(
    mapOf(
        Pair(CustomMealListFragment(), "Custom Meals"),
        Pair(FavoriteMealListFragment(), "Favorite Meals")
    )
)