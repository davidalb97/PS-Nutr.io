package pt.ipl.isel.leic.ps.androidclient.ui.fragment.list

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.navigation.findNavController
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.data.model.MealInfo
import pt.ipl.isel.leic.ps.androidclient.data.model.Source
import pt.ipl.isel.leic.ps.androidclient.ui.adapter.recycler.meal.MealInfoRecyclerAdapter
import pt.ipl.isel.leic.ps.androidclient.ui.provider.CustomMealRecyclerVMProviderFactory
import pt.ipl.isel.leic.ps.androidclient.ui.util.Navigation
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list.meal.MealInfoListViewModel
import pt.ipl.isel.leic.ps.androidclient.util.TimestampWithTimeZone

class CustomMealListFragment
    : BaseListFragment<MealInfo, MealInfoListViewModel, MealInfoRecyclerAdapter>() {

    override val recyclerAdapter by lazy {
        MealInfoRecyclerAdapter(
            recyclerViewModel,
            this.requireContext()
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val addButton = view.findViewById<ImageButton>(R.id.add_meal)

        if (recyclerViewModel.navDestination != Navigation.SEND_TO_CALCULATOR) {
            addButton.visibility = View.VISIBLE
            addButton.setOnClickListener {
                view.findNavController().navigate(R.id.nav_add_custom_meal)
            }
        }
        //TODO Remove this hardcoded debug meal manual insertion
        recyclerViewModel.restoreFromList(
            listOf(
                MealInfo(
                    dbId = 0,
                    dbRestaurantId = 2,
                    name = "wutever",
                    submissionId = 2,
                    restaurantSubmissionId = "2",
                    carbs = 2,
                    amount = 3,
                    unit = "nyan",
                    imageUri = null,
                    votes = null,
                    isFavorite = false,
                    isSuggested = false,
                    source = Source.CUSTOM,
                    creationDate = TimestampWithTimeZone.now(),
                    ingredientComponents = emptyList(),
                    mealComponents = emptyList(),
                    cuisines = emptyList(),
                    portions = emptyList()
                )
            )
        )
        recyclerViewModel.update()
    }

    override fun getVMProviderFactory(
        savedInstanceState: Bundle?,
        intent: Intent
    ) = CustomMealRecyclerVMProviderFactory(
        arguments,
        savedInstanceState,
        intent
    )

    override fun getRecyclerViewModelClass() = MealInfoListViewModel::class.java

    override fun getRecyclerId() = R.id.itemList

    override fun getProgressBarId() = R.id.progressBar

    override fun getLayout() = R.layout.meal_list

    override fun getNoItemsLabelId() = R.id.no_meals_found
}