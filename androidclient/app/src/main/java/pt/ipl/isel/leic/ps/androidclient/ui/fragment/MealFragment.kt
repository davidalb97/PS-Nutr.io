package pt.ipl.isel.leic.ps.androidclient.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import pt.ipl.isel.leic.ps.androidclient.NutrioApp
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.data.source.model.Meal
import pt.ipl.isel.leic.ps.androidclient.ui.adapter.CuisineRecyclerAdapter
import pt.ipl.isel.leic.ps.androidclient.ui.adapter.MealRecyclerAdapter
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.CuisineRecyclerViewModel
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.MealRecyclerViewModel

class MealFragment: RecyclerListFragment<Meal>() {

    private val adapter: MealRecyclerAdapter by lazy {
        MealRecyclerAdapter(viewModel as MealRecyclerViewModel, this.requireContext())
    }

    /**
     * ViewModel factory
     * Initializes the view model
     */
    private fun getViewModelFactory() = object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return MealRecyclerViewModel(requireActivity().applicationContext as NutrioApp) as T
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.meal_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerList(view)
        list.adapter = adapter
        list.layoutManager = LinearLayoutManager(this.requireContext())
        (viewModel as MealRecyclerViewModel)
            .getMeals(successFunction(), errorFunction())
        startObserver()
        startScrollListener()
    }
}