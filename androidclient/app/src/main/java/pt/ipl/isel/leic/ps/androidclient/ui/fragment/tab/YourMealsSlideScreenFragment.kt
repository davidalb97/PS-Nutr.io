package pt.ipl.isel.leic.ps.androidclient.ui.fragment.tab

import pt.ipl.isel.leic.ps.androidclient.ui.fragment.recycler.room.CustomMealRecyclerFragment
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.recycler.room.FavoriteMealRecyclerFragment

class YourMealsSlideScreenFragment :
    ASlideScreenFragment(
        mapOf(
            Pair(CustomMealRecyclerFragment(), "Custom Meals"),
            Pair(FavoriteMealRecyclerFragment(), "Favorite Meals")
        )
    )