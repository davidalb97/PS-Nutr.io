package pt.ipl.isel.leic.ps.androidclient.ui.fragment.slide

import pt.ipl.isel.leic.ps.androidclient.ui.fragment.recycler.CuisinesRecyclerFragment
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.LocationFragment
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.recycler.MealRecyclerFragment

class RestaurantSlideScreenFragment :
    ASlideScreenFragment(
        mapOf(
            Pair(CuisinesRecyclerFragment(), "Search by Cuisines"),
            Pair(LocationFragment(), "Search by Location"),
            Pair(MealRecyclerFragment(), "Search by Meals")
        )
    )