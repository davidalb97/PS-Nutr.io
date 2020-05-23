package pt.ipl.isel.leic.ps.androidclient.ui.fragment.tab

import pt.ipl.isel.leic.ps.androidclient.ui.fragment.recycler.request.MealRecyclerFragment
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.recycler.room.SavedMealRecyclerFragment

class AddMealSlideScreenFragment : ASlideScreenFragment(
    mapOf(
        Pair(MealRecyclerFragment(), "Meals from restaurants"),
        Pair(SavedMealRecyclerFragment(), "Saved meals")
    )
)