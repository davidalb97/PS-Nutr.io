package pt.ipl.isel.leic.ps.androidclient.ui.fragment.list

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.navigation.findNavController
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.data.model.MealItem
import pt.ipl.isel.leic.ps.androidclient.ui.adapter.recycler.meal.MealItemRecyclerAdapter
import pt.ipl.isel.leic.ps.androidclient.ui.modular.IUserSession
import pt.ipl.isel.leic.ps.androidclient.ui.modular.filter.IItemListFilter
import pt.ipl.isel.leic.ps.androidclient.ui.modular.filter.IItemListFilterOwner
import pt.ipl.isel.leic.ps.androidclient.ui.modular.listener.check.ICheckListener
import pt.ipl.isel.leic.ps.androidclient.ui.modular.listener.check.ICheckListenerOwner
import pt.ipl.isel.leic.ps.androidclient.ui.modular.listener.click.IItemClickListener
import pt.ipl.isel.leic.ps.androidclient.ui.modular.listener.click.IItemClickListenerOwner
import pt.ipl.isel.leic.ps.androidclient.ui.provider.CustomMealRecyclerVMProviderFactory
import pt.ipl.isel.leic.ps.androidclient.ui.util.ItemAction
import pt.ipl.isel.leic.ps.androidclient.ui.util.Navigation
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list.meal.MealItemListViewModel

class CustomMealListFragment
    : BaseListFragment<MealItem, MealItemListViewModel, MealItemRecyclerAdapter>(),
    ICheckListenerOwner<MealItem>,
    IItemClickListenerOwner<MealItem>,
    IItemListFilterOwner<MealItem>,
    IUserSession {

    override val paginated = true
    override val recyclerViewId = R.id.itemList
    override val progressBarId = R.id.progressBar
    override val layout = R.layout.meal_list
    override val noItemsTextViewId = R.id.no_meals_found

    override var restoredItemPredicator: ((MealItem) -> Boolean)? = null
    override var onCheckListener: ICheckListener<MealItem>? = null
    override var onClickListener: IItemClickListener<MealItem>? = null
    override var itemFilter: IItemListFilter<MealItem>? = null

    override val vmClass = MealItemListViewModel::class.java
    override val vMProviderFactorySupplier = ::CustomMealRecyclerVMProviderFactory
    override val recyclerAdapter by lazy {
        MealItemRecyclerAdapter(
            viewModel,
            this.requireContext(),
            restoredItemPredicator,
            onCheckListener,
            onClickListener,
            Navigation.SEND_TO_ADD_CUSTOM_MEAL
        )
    }

    lateinit var addButton: ImageButton

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //Init super's recycler list handler
        super.onViewCreated(view, savedInstanceState)

        addButton = view.findViewById(R.id.add_meal)

        if (viewModel.actions.contains(ItemAction.ADD)) {
            addButton.visibility = View.VISIBLE
            addButton.setOnClickListener {
                ensureUserSession(requireContext()) {
                    view.findNavController().navigate(Navigation.SEND_TO_ADD_CUSTOM_MEAL.navId)
                }
            }
        }

        ensureUserSession(requireContext(), failConsumer = {
            super.recyclerHandler.onNoRecyclerItems()
        }) {
            viewModel.liveDataHandler.filter = itemFilter
            viewModel.setupList()
        }
    }
}