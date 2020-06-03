package pt.ipl.isel.leic.ps.androidclient.ui.fragment.recycler.room

import pt.ipl.isel.leic.ps.androidclient.data.db.entity.DbFavoriteMeal
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.tab.CALCULATOR_BUNDLE_FLAG
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.FavoriteMealRecyclerViewModel

class FavoriteMealRecyclerFragment() :
    ARoomRecyclerListFragment<DbFavoriteMeal, FavoriteMealRecyclerViewModel>() {

    private val isCalculatorMode: Boolean by lazy {
        this.requireArguments().getBoolean(CALCULATOR_BUNDLE_FLAG)
    }

    override fun startScrollListener() {
        TODO("Not yet implemented")
    }
}