package pt.ipl.isel.leic.ps.androidclient.ui.fragment.slide

import pt.ipl.isel.leic.ps.androidclient.ui.fragment.recycler.CuisinesFragment
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.LocationFragment
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.recycler.MealFragment

class RestaurantSlideScreenFragment :
    SlideScreenFragment(
        mapOf(
            Pair(CuisinesFragment(), "Search by Cuisines"),
            Pair(LocationFragment(), "Search by Location"),
            Pair(MealFragment(), "Search by Meals")
        )
    )