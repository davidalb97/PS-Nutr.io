package pt.ipl.isel.leic.ps.androidclient.ui.fragment.tab

import pt.ipl.isel.leic.ps.androidclient.ui.fragment.recycler.request.MealRecyclerFragment
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.recycler.request.RestaurantRecyclerFragment

class RestaurantLocationSlideScreenFragment :
    ASlideScreenFragment(
        mapOf(
            Pair(RestaurantRecyclerFragment(), "Search by name"),
            Pair(MealRecyclerFragment(), "Map")
        ),
        false
    )