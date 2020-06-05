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
import pt.ipl.isel.leic.ps.androidclient.data.model.CustomMeal
import pt.ipl.isel.leic.ps.androidclient.ui.adapter.recycler.CustomMealRecyclerAdapter
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.tab.CALCULATOR_BUNDLE_FLAG
import pt.ipl.isel.leic.ps.androidclient.ui.provider.CustomMealRecyclerVMProviderFactory
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.CustomMealRecyclerViewModel

class CustomMealRecyclerFragment :
    ARoomRecyclerListFragment<CustomMeal, CustomMealRecyclerViewModel>() {

    private val isCalculatorMode: Boolean by lazy {
        this.requireArguments().getBoolean(CALCULATOR_BUNDLE_FLAG)
    }

    private val adapter: CustomMealRecyclerAdapter by lazy {
        CustomMealRecyclerAdapter(
            viewModel,
            this.requireContext(),
            isCalculatorMode
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
        return inflater.inflate(R.layout.custom_meals_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        noItemsLabel = view.findViewById(R.id.no_saved_meals)
        val addButton = view.findViewById<ImageButton>(R.id.add_custom_meal)
        addButton.visibility = View.INVISIBLE
        initRecyclerList(view)
        setErrorFunction()
        list.adapter = adapter
        list.layoutManager = LinearLayoutManager(this.requireContext())
        startObserver()
        viewModel.update()
        if (!isCalculatorMode) {
            addButton.visibility = View.VISIBLE
            addButton.setOnClickListener {
                view.findNavController().navigate(R.id.nav_add_custom_meal)
            }
        }
    }

    override fun startScrollListener() {
        TODO("Not yet implemented")
    }

    override fun successFunction(list: List<CustomMeal>) {
        noItemsLabel.visibility = if (list.isEmpty()) View.VISIBLE
        else View.INVISIBLE
    }
}