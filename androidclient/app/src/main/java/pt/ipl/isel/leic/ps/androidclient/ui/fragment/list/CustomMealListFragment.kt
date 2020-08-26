package pt.ipl.isel.leic.ps.androidclient.ui.fragment.list

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.navigation.findNavController
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.data.model.MealItem
import pt.ipl.isel.leic.ps.androidclient.ui.adapter.recycler.meal.MealItemRecyclerAdapter
import pt.ipl.isel.leic.ps.androidclient.ui.modular.IUserSession
import pt.ipl.isel.leic.ps.androidclient.ui.modular.listener.check.ICheckListener
import pt.ipl.isel.leic.ps.androidclient.ui.modular.listener.check.ICheckListenerOwner
import pt.ipl.isel.leic.ps.androidclient.ui.modular.listener.click.IItemClickListener
import pt.ipl.isel.leic.ps.androidclient.ui.modular.listener.click.IItemClickListenerOwner
import pt.ipl.isel.leic.ps.androidclient.ui.provider.CustomMealRecyclerVMProviderFactory
import pt.ipl.isel.leic.ps.androidclient.ui.util.Navigation
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list.meal.MealItemListViewModel

class CustomMealListFragment
    : BaseListFragment<MealItem, MealItemListViewModel, MealItemRecyclerAdapter>(),
    ICheckListenerOwner<MealItem>,
    IItemClickListenerOwner<MealItem>,
    IUserSession {

    override var onCheckListener: ICheckListener<MealItem>? = null
    override var onClickListener: IItemClickListener<MealItem>? = null
    override val recyclerAdapter by lazy {
        MealItemRecyclerAdapter(
            recyclerViewModel,
            this.requireContext(),
            onCheckListener,
            onClickListener
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ensureUserSession(requireContext(), failConsumer = {
            super.recyclerHandler.onNoRecyclerItems()
        }) {
            val addButton = view.findViewById<ImageButton>(R.id.add_meal)

            if (recyclerViewModel.navDestination != Navigation.SEND_TO_CALCULATOR) {
                addButton.visibility = View.VISIBLE
                addButton.setOnClickListener {
                    view.findNavController().navigate(R.id.nav_add_custom_meal)
                }
            }
            recyclerViewModel.update()
        }
    }

    override fun getVMProviderFactory(
        savedInstanceState: Bundle?,
        intent: Intent
    ) = CustomMealRecyclerVMProviderFactory(
        arguments,
        savedInstanceState,
        intent
    )

    override fun getRecyclerViewModelClass() = MealItemListViewModel::class.java

    override fun getRecyclerId() = R.id.itemList

    override fun getProgressBarId() = R.id.progressBar

    override fun getLayout() = R.layout.meal_list

    override fun getNoItemsLabelId() = R.id.no_meals_found

}