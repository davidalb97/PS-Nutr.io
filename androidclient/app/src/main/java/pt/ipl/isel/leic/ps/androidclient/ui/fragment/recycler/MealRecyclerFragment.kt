package pt.ipl.isel.leic.ps.androidclient.ui.fragment.recycler

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import pt.ipl.isel.leic.ps.androidclient.NutrioApp
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.data.source.model.Meal
import pt.ipl.isel.leic.ps.androidclient.ui.adapter.recycler.MealRecyclerAdapter
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.MealRecyclerViewModel

class MealRecyclerFragment: ARecyclerListFragment<Meal>() {

    private val adapter: MealRecyclerAdapter by lazy {
        MealRecyclerAdapter(
            viewModel as MealRecyclerViewModel,
            this.requireContext()
        )
    }

    /**
     * ViewModel factory
     * Initializes the view model
     */
    private fun getViewModelFactory() = object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return MealRecyclerViewModel() as T
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel =
            ViewModelProvider(this.requireActivity(), getViewModelFactory())
                .get(MealRecyclerViewModel::class.java)
        return inflater.inflate(R.layout.meal_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerList(view)
        list.adapter = adapter
        list.layoutManager = LinearLayoutManager(this.requireContext())
        startObserver()
        startScrollListener()
    }
}