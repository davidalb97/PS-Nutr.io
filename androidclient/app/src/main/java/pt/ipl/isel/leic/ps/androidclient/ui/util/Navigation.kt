package pt.ipl.isel.leic.ps.androidclient.ui.util

import androidx.annotation.IdRes
import pt.ipl.isel.leic.ps.androidclient.R

enum class Navigation(@IdRes private val _navId: Int?) {
    IGNORE(null),
    SEND_TO_ADD_CUSTOM_MEAL(R.id.nav_add_custom_meal),
    SEND_TO_ADD_CUSTOM_RESTAURANT(null),//TODO add/edit restaurant
    SEND_TO_CALCULATOR(R.id.nav_calculator),
    SEND_TO_HISTORY(R.id.nav_history),
    SEND_TO_MEAL_DETAIL(R.id.nav_meal_detail),
    SEND_TO_MEAL_LIST_BY_NAME(R.id.nav_tab_meals),
    SEND_TO_MEAL_LIST_CUSTOM(R.id.nav_tab_your_meals),
    SEND_TO_INGREDIENT_LIST(R.id.nav_ingredients),
    SEND_TO_RESTAURANT_DETAIL(R.id.nav_restaurant_detail),
    SEND_TO_RESTAURANT_LIST(R.id.nav_tab_restaurants),
    SEND_TO_RESTAURANT_LIST_MAP(R.id.nav_map);

    val navId: Int
        get() = _navId ?: throw UnsupportedOperationException("Cannot navigate!")
}