package pt.ipl.isel.leic.ps.androidclient.ui.fragment.recycler.request

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.data.model.MealIngredient
import pt.ipl.isel.leic.ps.androidclient.data.model.MealItem
import pt.ipl.isel.leic.ps.androidclient.ui.adapter.recycler.MealRecyclerAdapter
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.tab.CALCULATOR_BUNDLE_FLAG
import pt.ipl.isel.leic.ps.androidclient.ui.listener.ScrollListener
import pt.ipl.isel.leic.ps.androidclient.ui.provider.BUNDLE_INGREDIENT_LIST
import pt.ipl.isel.leic.ps.androidclient.ui.provider.IngredientRecyclerVMProviderFactory
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.IngredientRecyclerViewModel
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.MealRecyclerViewModel

open class IngredientsRecyclerFragment
    : ARequestRecyclerListFragment<MealItem, IngredientRecyclerViewModel>() {

    protected val adapter: MealRecyclerAdapter by lazy {
        MealRecyclerAdapter(
            viewModel,
            this.requireContext(),
            false
        )
    }

    /**
     * ViewModel builder
     * Initializes the view model, calling the respective
     * view model provider factory
     */
    protected open fun buildViewModel(savedInstanceState: Bundle?) {
        val rootActivity = this.requireActivity()
        val factory = IngredientRecyclerVMProviderFactory(savedInstanceState, rootActivity.intent)
        viewModel = ViewModelProvider(rootActivity, factory)[IngredientRecyclerViewModel::class.java]
        if(arguments != null) {
            val ingredients = requireArguments().getParcelableArrayList<MealIngredient>(
                BUNDLE_INGREDIENT_LIST
            )
            viewModel.ingredients = ingredients
        }
        activityApp = requireActivity().application
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        buildViewModel(savedInstanceState)
        return inflater.inflate(R.layout.meal_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerList(view)
        setErrorFunction()
        list.adapter = adapter
        list.layoutManager = LinearLayoutManager(this.requireContext())
        startObserver()
        viewModel.update()
    }

    override fun startScrollListener() {
        list.addOnScrollListener(object :
            ScrollListener(list.layoutManager as LinearLayoutManager, progressWheel) {

            var minimumListSize = 1

            override fun loadMore() {
                minimumListSize = viewModel.items.size + 1
                if (!isLoading && progressBar.visibility == View.INVISIBLE) {
                    startLoading()
                    viewModel.update()
                    stopLoading()
                }
            }

            override fun shouldGetMore(): Boolean =
                !isLoading && minimumListSize < viewModel.items.size
        })
    }

    override fun getRecyclerId() = R.id.itemList

    override fun getProgressBarId() = R.id.progressBar
}