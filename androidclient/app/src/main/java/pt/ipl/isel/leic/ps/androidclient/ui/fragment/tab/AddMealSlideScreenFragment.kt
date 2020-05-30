package pt.ipl.isel.leic.ps.androidclient.ui.fragment.tab

import pt.ipl.isel.leic.ps.androidclient.ui.fragment.recycler.request.MealRecyclerFragment
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.recycler.room.CustomMealRecyclerFragment
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.recycler.room.FavoriteMealRecyclerFragment

class AddMealSlideScreenFragment : ASlideScreenFragment(
    mapOf(
        Pair(MealRecyclerFragment(true), "Meals from restaurants"),
        Pair(FavoriteMealRecyclerFragment(true), "Favorite Meals"),
        Pair(CustomMealRecyclerFragment(true), "Custom meals")
    )
)