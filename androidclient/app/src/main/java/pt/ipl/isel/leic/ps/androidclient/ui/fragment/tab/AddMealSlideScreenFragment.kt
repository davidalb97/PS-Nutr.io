package pt.ipl.isel.leic.ps.androidclient.ui.fragment.tab

import pt.ipl.isel.leic.ps.androidclient.ui.fragment.recycler.request.MealRecyclerFragment
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.recycler.room.CustomMealRecyclerFragment
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.recycler.room.FavoriteMealRecyclerFragment

class AddMealSlideScreenFragment : ASlideScreenFragment(
    mapOf(
        Pair(MealRecyclerFragment(), "Meals from restaurants"),
        Pair(FavoriteMealRecyclerFragment(), "Favorite Meals"),
        Pair(CustomMealRecyclerFragment(), "Custom meals")
    ),
    true
)