package pt.ipl.isel.leic.ps.androidclient.ui.tabfragments

import pt.ipl.isel.leic.ps.androidclient.ui.fragment.CuisinesFragment
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.LocationFragment
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.MealsFragment

class RestaurantTabFragment :
    TabFragment(
        mapOf(
            Pair(CuisinesFragment(), "Search by Cuisines"),
            Pair(LocationFragment(), "Search by Location"),
            Pair(MealsFragment(), "Search by Meals")
        )
    )