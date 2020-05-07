package pt.ipl.isel.leic.ps.androidclient.ui.fragment.slide

import pt.ipl.isel.leic.ps.androidclient.ui.fragment.recycler.CuisinesFragment
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.LocationFragment
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.recycler.RestaurantFragment

class MealSlideScreenFragment :
    SlideScreenFragment(
        mapOf(
            Pair(RestaurantFragment(), "Search by Restaurants"),
            Pair(LocationFragment(), "Search by Location"),
            Pair(CuisinesFragment(), "Search by Cuisines")
        )
    )