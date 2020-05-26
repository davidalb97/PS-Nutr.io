package pt.ipl.isel.leic.ps.androidclient.ui.fragment.recycler.room

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.data.model.Meal
import pt.ipl.isel.leic.ps.androidclient.ui.adapter.recycler.MealRecyclerAdapter
import pt.ipl.isel.leic.ps.androidclient.ui.provider.MealRecyclerVMProviderFactory
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.MealRecyclerViewModel

class SavedMealRecyclerFragment : ARoomRecyclerListFragment<Meal, MealRecyclerViewModel>() {

    private val adapter: MealRecyclerAdapter by lazy {
        MealRecyclerAdapter(
            viewModel,
            this.requireContext()
        )
    }

    private fun buildViewModel(savedInstanceState: Bundle?) {
        val rootActivity = this.requireActivity()
        val factory = MealRecyclerVMProviderFactory(savedInstanceState, rootActivity.intent)
        viewModel = ViewModelProvider(rootActivity, factory)[MealRecyclerViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        buildViewModel(savedInstanceState)
        return inflater.inflate(R.layout.saved_meals_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        noItemsLabel = view.findViewById(R.id.no_saved_meals)
        val addButton = view.findViewById<ImageButton>(R.id.add_custom_meal)
        initRecyclerList(view)
        setCallbackFunctions()
        list.adapter = adapter
        list.layoutManager = LinearLayoutManager(this.requireContext())
        startObserver()
        viewModel.updateListFromLiveData()

        addButton.setOnClickListener {
            view.findNavController().navigate(R.id.nav_add_custom_meal)
        }
    }

    override fun startScrollListener() {
        TODO("Not yet implemented")
    }

}