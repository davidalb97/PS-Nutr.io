package pt.ipl.isel.leic.ps.androidclient.ui.fragment.tab

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.navGraphViewModels
import pt.ipl.isel.leic.ps.androidclient.NutrioApp
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.data.model.MealItem
import pt.ipl.isel.leic.ps.androidclient.data.model.Source
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.list.CustomMealListFragment
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.list.FavoriteMealListFragment
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.list.MealItemListFragment
import pt.ipl.isel.leic.ps.androidclient.ui.modular.ISend
import pt.ipl.isel.leic.ps.androidclient.ui.modular.filter.IItemListFilter
import pt.ipl.isel.leic.ps.androidclient.ui.modular.filter.IItemListFilterOwner
import pt.ipl.isel.leic.ps.androidclient.ui.modular.listener.click.IItemClickListener
import pt.ipl.isel.leic.ps.androidclient.ui.modular.listener.click.IItemClickListenerOwner
import pt.ipl.isel.leic.ps.androidclient.ui.util.Navigation
import pt.ipl.isel.leic.ps.androidclient.ui.util.prompt.PromptConfirm
import pt.ipl.isel.leic.ps.androidclient.ui.util.putNavigation
import pt.ipl.isel.leic.ps.androidclient.ui.util.putSource
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list.meal.info.RestaurantInfoViewModel

class AddRestaurantMealSlideScreenFragment : BaseSlideScreenFragment(propagateArguments = false),
    ISend,
    IItemListFilterOwner<MealItem>,
    IItemClickListenerOwner<MealItem> {

    private lateinit var okButton: Button
    private val viewModel: RestaurantInfoViewModel
            by navGraphViewModels(Navigation.SEND_TO_RESTAURANT_DETAIL.navId)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        okButton = view.findViewById(R.id.tab_fragment_ok_btn)
        okButton.text = getString(R.string.cancel)
        okButton.visibility = View.VISIBLE
        okButton.setOnClickListener {
            sendToDestination(view, Navigation.BACK_TO_RESTAURANT_DETAIL)
        }
    }

    override fun addFragments(fragments: HashMap<Fragment, String>) {
        fragments[setupFragment(MealItemListFragment(), Source.API)] =
            "Suggested Meals"
        fragments[setupFragment(FavoriteMealListFragment(), Source.FAVORITE_MEAL)] =
            "Favorite Meals"
        fragments[setupFragment(FavoriteMealListFragment(), Source.FAVORITE_RESTAURANT_MEAL)] =
            "Favorite Restaurant Meals"
        fragments[setupFragment(CustomMealListFragment(), Source.CUSTOM_MEAL)] =
            "Custom meals"
    }

    private fun <F> setupFragment(fragment: F, source: Source): F
            where F : Fragment,
                  F : IItemListFilterOwner<MealItem>,
                  F : IItemClickListenerOwner<MealItem> {
        val bundle = Bundle()
        bundle.putSource(source)
        bundle.putNavigation(Navigation.IGNORE)
        fragment.arguments = bundle
        fragment.itemFilter = itemFilter
        fragment.onClickListener = onClickListener
        return fragment
    }

    override var itemFilter: IItemListFilter<MealItem>? = IItemListFilter { newItem ->
        viewModel.items.none {
            it.submissionId == newItem.submissionId
        }
    }

    override var onClickListener: IItemClickListener<MealItem>? =
        IItemClickListener { mealItem: MealItem, _: () -> Unit ->
            PromptConfirm(
                ctx = requireContext(),
                titleId = R.string.add_restaurant_meal
            ) {
                viewModel.addedMeal = mealItem
                viewModel.addRestaurantMeal(mealItem, log::e) {
                    Toast.makeText(NutrioApp.app, R.string.meal_added, Toast.LENGTH_SHORT).show()
                    sendToDestination(requireView(), Navigation.BACK_TO_RESTAURANT_DETAIL)
                }
            }.show()
        }

    override fun onSendToDestination(bundle: Bundle) {

    }
}