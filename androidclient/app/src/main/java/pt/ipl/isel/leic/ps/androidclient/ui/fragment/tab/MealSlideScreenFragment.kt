package pt.ipl.isel.leic.ps.androidclient.ui.fragment.tab

import pt.ipl.isel.leic.ps.androidclient.ui.fragment.recycler.request.CuisinesRecyclerFragment
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.map.MapFragment
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.recycler.request.RestaurantRecyclerFragment

class MealSlideScreenFragment :
    ASlideScreenFragment(
        mapOf(
            Pair(RestaurantRecyclerFragment(), "Search by Restaurants"),
            Pair(MapFragment(), "Search by Location"),
            Pair(CuisinesRecyclerFragment(), "Search by Cuisines")
        ),
        false
    )