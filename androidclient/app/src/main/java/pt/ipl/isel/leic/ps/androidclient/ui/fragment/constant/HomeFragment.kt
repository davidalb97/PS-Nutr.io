package pt.ipl.isel.leic.ps.androidclient.ui.fragment.constant

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.data.model.Source
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.BaseFragment
import pt.ipl.isel.leic.ps.androidclient.ui.util.*

class HomeFragment : BaseFragment() {

    override val layout: Int = R.layout.home_fragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRestaurantsByNameBtn(view)
        setupRestaurantsByLocationBtn(view)
        setupMealsByNameBtn(view)
        setupMealsCustomBtn(view)
        setupCalculatorBtn(view)
    }

    private fun setupRestaurantsByNameBtn(view: View) {
        val button: ViewGroup = view.findViewById(R.id.firstSection_button1)
        button.setOnClickListener {
            val bundle = Bundle()
            bundle.putSource(Source.API)
            bundle.putNavigation(Navigation.SEND_TO_RESTAURANT_DETAIL)
            bundle.putItemActions(ItemAction.REPORT, ItemAction.FAVORITE)
            navigate(Navigation.SEND_TO_RESTAURANT_LIST, bundle)
        }
    }

    private fun setupRestaurantsByLocationBtn(view: View) {
        val button: ViewGroup = view.findViewById(R.id.firstSection_button2)
        button.setOnClickListener {
            val bundle = Bundle()
            bundle.putSource(Source.API)
            bundle.putNavigation(Navigation.SEND_TO_RESTAURANT_DETAIL)
            bundle.putItemActions(ItemAction.REPORT, ItemAction.FAVORITE)
            navigate(Navigation.SEND_TO_RESTAURANT_LIST_MAP, bundle)
        }
    }

    private fun setupMealsByNameBtn(view: View) {
        val button: ViewGroup = view.findViewById(R.id.secondSection_button1)
        button.setOnClickListener {
            navigate(Navigation.SEND_TO_MEAL_LIST_BY_NAME)
        }
    }

    private fun setupMealsCustomBtn(view: View) {
        val button: ViewGroup = view.findViewById(R.id.secondSection_button2)
        button.setOnClickListener {
            navigate(Navigation.SEND_TO_MEAL_LIST_CUSTOM)
        }
    }

    private fun setupCalculatorBtn(view: View) {
        val button: ViewGroup = view.findViewById(R.id.thirdSection_button1)
        button.setOnClickListener {
            navigate(Navigation.SEND_TO_CALCULATOR)
        }
    }
}
