package pt.ipl.isel.leic.ps.androidclient.ui.fragment.tab

import androidx.fragment.app.Fragment
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.list.MealItemListFragment
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.list.RestaurantListFragment

class RestaurantSlideScreenFragment : BaseSlideScreenFragment() {

    override fun addFragments(fragments: HashMap<Fragment, String>) {
        fragments[RestaurantListFragment()] = "Search by name"
        fragments[MealItemListFragment()] = "Search by meals"
    }
}