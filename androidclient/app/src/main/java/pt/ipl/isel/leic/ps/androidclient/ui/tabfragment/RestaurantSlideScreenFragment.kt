package pt.ipl.isel.leic.ps.androidclient.ui.tabfragment

import pt.ipl.isel.leic.ps.androidclient.ui.fragment.CuisinesFragment
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.LocationFragment
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.MealsFragment

class RestaurantSlideScreenFragment :
    SlideScreenFragment(
        mapOf(
            Pair(CuisinesFragment(), "Search by Cuisines"),
            Pair(LocationFragment(), "Search by Location"),
            Pair(MealsFragment(), "Search by Meals")
        )
    )