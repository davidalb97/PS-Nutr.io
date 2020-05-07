package pt.ipl.isel.leic.ps.androidclient.ui.fragment.slide

import pt.ipl.isel.leic.ps.androidclient.ui.fragment.recycler.CuisinesRecyclerFragment
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.LocationFragment
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.recycler.RestaurantRecyclerFragment

class MealSlideScreenFragment :
    ASlideScreenFragment(
        mapOf(
            Pair(RestaurantRecyclerFragment(), "Search by Restaurants"),
            Pair(LocationFragment(), "Search by Location"),
            Pair(CuisinesRecyclerFragment(), "Search by Cuisines")
        )
    )