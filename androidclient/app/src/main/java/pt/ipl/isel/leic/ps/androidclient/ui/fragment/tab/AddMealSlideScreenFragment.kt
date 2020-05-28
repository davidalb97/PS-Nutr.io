package pt.ipl.isel.leic.ps.androidclient.ui.fragment.tab

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.recycler.request.MealRecyclerFragment
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.recycler.room.CustomMealRecyclerFragment
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.recycler.room.FavoriteMealRecyclerFragment

class AddMealSlideScreenFragment : ASlideScreenFragment(
    mapOf(
        Pair(MealRecyclerFragment(), "Meals from restaurants"),
        Pair(FavoriteMealRecyclerFragment(), "Favorite Meals"),
        Pair(CustomMealRecyclerFragment(), "Custom meals")
    )
) {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.broadcastBundle = requireArguments()
        return super.onCreateView(inflater, container, savedInstanceState)
    }
}