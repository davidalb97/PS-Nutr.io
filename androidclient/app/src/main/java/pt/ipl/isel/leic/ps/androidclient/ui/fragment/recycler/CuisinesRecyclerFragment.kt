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
import pt.ipl.isel.leic.ps.androidclient.data.source.model.Cuisine
import pt.ipl.isel.leic.ps.androidclient.ui.adapter.recycler.CuisineRecyclerAdapter
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.CuisineRecyclerViewModel

class CuisinesRecyclerFragment : ARecyclerListFragment<Cuisine>() {

    private val adapter: CuisineRecyclerAdapter by lazy {
        CuisineRecyclerAdapter(
            viewModel as CuisineRecyclerViewModel,
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
            return CuisineRecyclerViewModel(requireActivity().applicationContext as NutrioApp) as T
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel =
            ViewModelProvider(this.requireActivity(), getViewModelFactory())
                .get(CuisineRecyclerViewModel::class.java)
        return inflater.inflate(R.layout.cuisines_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerList(view)
        list.adapter = adapter
        list.layoutManager = LinearLayoutManager(this.requireContext())
        (viewModel as CuisineRecyclerViewModel)
            .getCuisines(successFunction(), errorFunction())
        startObserver()
        startScrollListener()
    }
}
