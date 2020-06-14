package pt.ipl.isel.leic.ps.androidclient.ui.fragment.tab

import pt.ipl.isel.leic.ps.androidclient.ui.fragment.map.MapFragment
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.recycler.request.RestaurantRecyclerFragment

class MealSlideScreenFragment :
    ASlideScreenFragment(
        mapOf(
            Pair(RestaurantRecyclerFragment(), "Suggest Meals"),
            Pair(MapFragment(), "Meal Ingredients")
        ),
        false
    )