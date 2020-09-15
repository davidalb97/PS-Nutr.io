package pt.ipl.isel.leic.ps.androidclient.ui.fragment.tab

import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.navGraphViewModels
import pt.ipl.isel.leic.ps.androidclient.NutrioApp
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.data.model.MealItem
import pt.ipl.isel.leic.ps.androidclient.data.model.Source
import pt.ipl.isel.leic.ps.androidclient.ui.adapter.TabAdapter.TabConfig
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.list.CustomMealListFragment
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.list.FavoriteMealListFragment
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.list.MealItemListFragment
import pt.ipl.isel.leic.ps.androidclient.ui.modular.ISend
import pt.ipl.isel.leic.ps.androidclient.ui.modular.IViewModelManager
import pt.ipl.isel.leic.ps.androidclient.ui.modular.filter.IItemListFilter
import pt.ipl.isel.leic.ps.androidclient.ui.modular.filter.IItemListFilterOwner
import pt.ipl.isel.leic.ps.androidclient.ui.modular.listener.click.IItemClickListener
import pt.ipl.isel.leic.ps.androidclient.ui.modular.listener.click.IItemClickListenerOwner
import pt.ipl.isel.leic.ps.androidclient.ui.provider.AddRestaurantVMProfiderFactory
import pt.ipl.isel.leic.ps.androidclient.ui.util.Navigation
import pt.ipl.isel.leic.ps.androidclient.ui.util.prompt.PromptConfirm
import pt.ipl.isel.leic.ps.androidclient.ui.util.putNavigation
import pt.ipl.isel.leic.ps.androidclient.ui.util.putSource
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list.meal.info.RestaurantInfoViewModel

class AddRestaurantMealSlideScreenFragment : BaseSlideScreenFragment(propagateArguments = false),
    ISend,
    IItemListFilterOwner<MealItem>,
    IItemClickListenerOwner<MealItem>,
    IViewModelManager {

    private lateinit var okButton: Button
    override var savedInstanceState: Bundle? = null
    override val vMProviderFactorySupplier = ::AddRestaurantVMProfiderFactory
    private val viewModelInfo: RestaurantInfoViewModel by
    navGraphViewModels(Navigation.SEND_TO_RESTAURANT_DETAIL.navId) {
        vMProviderFactorySupplier(arguments, savedInstanceState, requireActivity().intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.savedInstanceState = savedInstanceState
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        okButton = view.findViewById(R.id.tab_fragment_ok_btn)
        okButton.text = getString(R.string.cancel)
        okButton.visibility = View.VISIBLE
        okButton.setOnClickListener {
            sendToDestination(view, Navigation.BACK_TO_RESTAURANT_DETAIL)
        }
    }

    override fun addTab(mutableList: MutableList<TabConfig>) {
        mutableList.add(TabConfig(
            title = getString(R.string.tab_suggested_meals),
            fragmentSupplier = ::MealItemListFragment,
            fragmentSetupConsumer = {
                setupFragment(it as MealItemListFragment, Source.API)
            }
        ))
        mutableList.add(TabConfig(
            title = getString(R.string.tab_favorite_meals),
            fragmentSupplier = ::FavoriteMealListFragment,
            fragmentSetupConsumer = {
                setupFragment(it as FavoriteMealListFragment, Source.FAVORITE_MEAL)
            }
        ))
        mutableList.add(TabConfig(
            title = getString(R.string.tab_favorite_restaurant_meals),
            fragmentSupplier = ::FavoriteMealListFragment,
            fragmentSetupConsumer = {
                setupFragment(it as FavoriteMealListFragment, Source.FAVORITE_RESTAURANT_MEAL)
            }
        ))
        mutableList.add(TabConfig(
            title = getString(R.string.tab_custom_meals),
            fragmentSupplier = ::CustomMealListFragment,
            fragmentSetupConsumer = {
                setupFragment(it as CustomMealListFragment, Source.CUSTOM_MEAL)
            }
        ))
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
        viewModelInfo.items.none {
            it.submissionId == newItem.submissionId
        }
    }

    override var onClickListener: IItemClickListener<MealItem>? =
        IItemClickListener { mealItem: MealItem, _: () -> Unit ->
            PromptConfirm(
                ctx = requireContext(),
                titleId = R.string.add_restaurant_meal
            ) {
                mealItem.restaurantSubmissionId = viewModelInfo.restaurantInfo?.id!!
                viewModelInfo.addedMeal = mealItem
                viewModelInfo.addRestaurantMeal(mealItem, log::e) {
                    Toast.makeText(NutrioApp.app, R.string.meal_added, Toast.LENGTH_SHORT).show()
                    sendToDestination(requireView(), Navigation.BACK_TO_RESTAURANT_DETAIL)
                }
            }.show()
        }

    override fun onSendToDestination(bundle: Bundle) {

    }

    override fun getViewModels(): Iterable<Parcelable> = super.getViewModels().plus(viewModelInfo)
}