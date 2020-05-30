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
import pt.ipl.isel.leic.ps.androidclient.data.db.dto.CustomMealDto
import pt.ipl.isel.leic.ps.androidclient.ui.adapter.recycler.CustomMealRecyclerAdapter
import pt.ipl.isel.leic.ps.androidclient.ui.provider.CustomMealRecyclerVMProviderFactory
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.CustomMealRecyclerViewModel

class CustomMealRecyclerFragment(
    private val addToCalculatorMode: Boolean = false
) : ARoomRecyclerListFragment<CustomMealDto, CustomMealRecyclerViewModel>() {

    private val adapter: CustomMealRecyclerAdapter by lazy {
        CustomMealRecyclerAdapter(
            viewModel,
            this.requireContext()
        )
    }

    private fun buildViewModel(savedInstanceState: Bundle?) {
        val rootActivity = this.requireActivity()
        val factory = CustomMealRecyclerVMProviderFactory(savedInstanceState, rootActivity.intent)
        viewModel =
            ViewModelProvider(rootActivity, factory)[CustomMealRecyclerViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        buildViewModel(savedInstanceState)
        // If the mode is active and it's the first onCreateView, then update viewModel
        if (!viewModel.addToCalculatorMode && this.addToCalculatorMode) {
            viewModel.addToCalculatorMode = this.addToCalculatorMode
        }
        return inflater.inflate(R.layout.custom_meals_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        noItemsLabel = view.findViewById(R.id.no_saved_meals)
        val addButton = view.findViewById<ImageButton>(R.id.add_custom_meal)
        addButton.visibility = View.INVISIBLE
        initRecyclerList(view)
        setCallbackFunctions()
        list.adapter = adapter
        list.layoutManager = LinearLayoutManager(this.requireContext())
        startObserver()
        viewModel.updateListFromLiveData()
        if (!viewModel.addToCalculatorMode) {
            addButton.visibility = View.VISIBLE
            addButton.setOnClickListener {
                view.findNavController().navigate(R.id.nav_add_custom_meal)
            }
        }
    }

    override fun startScrollListener() {
        TODO("Not yet implemented")
    }

}