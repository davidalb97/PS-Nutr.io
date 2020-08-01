package pt.ipl.isel.leic.ps.androidclient.ui.fragment.tab

import pt.ipl.isel.leic.ps.androidclient.ui.fragment.recycler.request.IngredientsRecyclerFragment
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.recycler.request.MealRecyclerFragment

class MealSlideScreenFragment :
    ASlideScreenFragment(
        mapOf(
            Pair(MealRecyclerFragment(), "Suggested Meals"),
            Pair(IngredientsRecyclerFragment(), "Meal Ingredients")
        ),
        false
    )