package pt.ipl.isel.leic.ps.androidclient.ui.tabfragment

import pt.ipl.isel.leic.ps.androidclient.ui.fragment.CuisinesFragment
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.LocationFragment
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.RestaurantFragment

class MealSlideScreenFragment :
    SlideScreenFragment(
        mapOf(
            Pair(RestaurantFragment(), "Search by Restaurants"),
            Pair(LocationFragment(), "Search by Location"),
            Pair(CuisinesFragment(), "Search by Cuisines")
        )
    )