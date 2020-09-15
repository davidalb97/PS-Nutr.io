package pt.ipl.isel.leic.ps.androidclient.ui.util

import androidx.annotation.IdRes
import pt.ipl.isel.leic.ps.androidclient.R

enum class Navigation(@IdRes private val _navId: Int?) {
    IGNORE(null),
    SEND_TO_ADD_CUSTOM_MEAL(R.id.nav_custom_meal_nested),
    SEND_TO_PICK_CUSTOM_MEAL_INGREDIENTS(R.id.nav_action_add_custom_meal_to_pick_ingredients),
    BACK_TO_CUSTOM_MEAL(R.id.nav_action_back_to_custom_meal),
    SEND_TO_ADD_RESTAURANT(R.id.nav_add_restaurant),
    SEND_TO_HOME(R.id.nav_home),
    SEND_TO_MEAL_DETAIL(R.id.nav_meal_detail),
    SEND_TO_MEAL_LIST_BY_NAME(R.id.nav_tab_meals),
    SEND_TO_MEAL_LIST_CUSTOM(R.id.nav_tab_your_meals),
    SEND_TO_INGREDIENT_LIST(R.id.nav_ingredients),
    SEND_TO_RESTAURANT_LIST(R.id.nav_restaurants),
    SEND_TO_RESTAURANT_LIST_MAP(R.id.nav_map),

    //Calculator nested
    SEND_TO_CALCULATOR(R.id.nav_calculator_nested),
    SEND_TO_PICK_CALCULATOR_INGREDIENTS(R.id.nav_action_calculator_to_pick_meals),
    BACK_TO_CALCULATOR_FROM_MEALS(R.id.nav_action_back_to_calculator_from_add_meals),
    SEND_TO_ADD_INSULIN_PROFILE_FROM_CALCULATOR(R.id.nav_action_calculator_to_add_insulin),
    BACK_TO_CALCULATOR_FROM_ADD_PROFILE(R.id.nav_action_back_to_calculator_from_add_profile),

    //Insulin Profiles nested
    SEND_TO_INSULIN_PROFILES(R.id.nav_insulin_profiles_nested),
    SEND_TO_ADD_INSULIN_PROFILE_FROM_PROFILES(R.id.nav_action_insulin_profiles_to_add_insulin),
    BACK_TO_INSULIN_PROFILES_FROM_ADD_PROFILE(R.id.nav_action_back_to_insulin_profiles_from_add_profile),

    //Restaurant Detail nested
    SEND_TO_RESTAURANT_DETAIL(R.id.nav_restaurant_info_nested),
    SEND_TO_PICK_RESTAURANT_MEAL(R.id.nav_action_restaurant_info_to_pick_meals),
    BACK_TO_RESTAURANT_DETAIL(R.id.nav_action_back_to_restaurant_info_from_add_meal);

    val navId: Int
        get() = _navId ?: throw UnsupportedOperationException("Cannot navigate!")
}