package pt.ipl.isel.leic.ps.androidclient.ui.fragment.recycler.room

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.data.model.MealItem
import pt.ipl.isel.leic.ps.androidclient.ui.adapter.recycler.FavoriteMealRecyclerAdapter
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.tab.CALCULATOR_BUNDLE_FLAG
import pt.ipl.isel.leic.ps.androidclient.ui.provider.FavoriteMealRecyclerVMProviderFactory
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.FavoriteMealRecyclerViewModel

class FavoriteMealRecyclerFragment :
    ARoomRecyclerListFragment<MealItem, FavoriteMealRecyclerViewModel>() {

    private val isCalculatorMode: Boolean by lazy {
        this.requireArguments().getBoolean(CALCULATOR_BUNDLE_FLAG)
    }

    private val adapter: FavoriteMealRecyclerAdapter by lazy {
        FavoriteMealRecyclerAdapter(
            viewModel,
            this.requireContext(),
            isCalculatorMode
        )
    }

    private fun buildViewModel(savedInstanceState: Bundle?) {
        val rootActivity = this.requireActivity()
        val factory = FavoriteMealRecyclerVMProviderFactory(savedInstanceState, rootActivity.intent)
        viewModel =
            ViewModelProvider(rootActivity, factory)[FavoriteMealRecyclerViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        buildViewModel(savedInstanceState)
        return inflater.inflate(R.layout.meal_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        noItemsLabel = view.findViewById(R.id.no_favorite_meals)
        initRecyclerList(view)
        setErrorFunction()
        list.adapter = adapter
        list.layoutManager = LinearLayoutManager(this.requireContext())
        startObserver()
        viewModel.update()
    }

    override fun startScrollListener() {
        TODO("Not yet implemented")
    }

    override fun successFunction(list: List<MealItem>) {
        progressWheel.visibility = View.INVISIBLE
        noItemsLabel.visibility = if (list.isEmpty()) View.VISIBLE else View.INVISIBLE
    }

    override fun getRecyclerId() = R.id.itemList

    override fun getProgressBarId() = R.id.progressBar
}