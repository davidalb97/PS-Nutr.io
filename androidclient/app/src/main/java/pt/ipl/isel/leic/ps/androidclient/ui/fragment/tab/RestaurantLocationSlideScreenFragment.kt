package pt.ipl.isel.leic.ps.androidclient.ui.fragment.tab

import pt.ipl.isel.leic.ps.androidclient.ui.fragment.list.MealItemListFragment
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.list.RestaurantListFragment

class RestaurantLocationSlideScreenFragment : ASlideScreenFragment(
    mapOf(
        Pair(RestaurantListFragment(), "Search by name"),
        Pair(MealItemListFragment(), "Map")
    )
)