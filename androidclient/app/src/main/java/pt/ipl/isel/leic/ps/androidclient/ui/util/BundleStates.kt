package pt.ipl.isel.leic.ps.androidclient.ui.util

import android.os.Bundle
import pt.ipl.isel.leic.ps.androidclient.data.model.*

private const val BUNDLE_CUISINES_LIST = "BUNDLE_CUISINES_LIST"
private const val BUNDLE_ACTIONS_LIST = "BUNDLE_ACTIONS_LIST"
private const val BUNDLE_NAVIGATION = "BUNDLE_MEAL_NAVIGATION"
private const val BUNDLE_MEAL_INGREDIENT_LIST = "BUNDLE_INGREDIENT_LIST"
private const val BUNDLE_MEAL_LIST = "BUNDLE_MEAL_LIST"
private const val BUNDLE_MEAL_INFO = "BUNDLE_MEAL_INFO"
private const val BUNDLE_MEAL_INFO_EDIT = "BUNDLE_MEAL_INFO_EDIT"
private const val BUNDLE_MEAL_INGREDIENT = "BUNDLE_MEAL_INGREDIENT"
private const val BUNDLE_MEAL_ITEM = "BUNDLE_MEAL_ITEM"
private const val BUNDLE_MEAL_DB_ID = "BUNDLE_MEAL_DB_ID"
private const val BUNDLE_SUBMISSION_ID = "BUNDLE_MEAL_DB_SUBMISSION_ID"
private const val BUNDLE_RESTAURANT_SUBMISSION_ID = "BUNDLE_MEAL_DB_RESTAURANT_SUBMISSION_ID"
private const val BUNDLE_RESTAURANT_ITEM = "BUNDLE_RESTAURANT_ITEM"
private const val BUNDLE_RESTAURANT_INFO = "BUNDLE_RESTAURANT_INFO"
private const val BUNDLE_RESTAURANT_INFO_EDIT = "BUNDLE_RESTAURANT_INFO_EDIT"
private const val BUNDLE_MEAL_SOURCE = "BUNDLE_MEAL_SOURCE_ORDINAL"
private const val BUNDLE_KEY = "BUNDLE_KEY"

fun Bundle.getBundleKey() = getString(BUNDLE_KEY)

fun Bundle.putBundleKey(key: String) = putString(BUNDLE_KEY, key)

fun Bundle.getMealIngredients() =
    getParcelableArrayList<MealIngredient>(BUNDLE_MEAL_INGREDIENT_LIST)

fun Bundle.putMealIngredients(ingredients: ArrayList<MealIngredient>) =
    putParcelableArrayList(BUNDLE_MEAL_INGREDIENT_LIST, ingredients)

fun Bundle.getMealItems() = getParcelableArrayList<MealItem>(BUNDLE_MEAL_LIST)

fun Bundle.putMealItems(meals: ArrayList<MealItem>) =
    putParcelableArrayList(BUNDLE_MEAL_LIST, meals)

fun Bundle.getMealItem() = getParcelable<MealItem>(BUNDLE_MEAL_ITEM)

fun Bundle.putMealItem(mealItem: MealItem?) = putParcelable(BUNDLE_MEAL_ITEM, mealItem)

fun Bundle.getMealInfo() = getParcelable<MealInfo>(BUNDLE_MEAL_INFO)

fun Bundle.removeMealInfo() = remove(BUNDLE_MEAL_INFO)

fun Bundle.putMealInfo(mealInfo: MealInfo?) = putParcelable(BUNDLE_MEAL_INFO, mealInfo)

fun Bundle.getMealIngredient() = getParcelable<MealIngredient>(BUNDLE_MEAL_INGREDIENT)

fun Bundle.putMealIngredient(mealIngredient: MealIngredient?) =
    putParcelable(BUNDLE_MEAL_INGREDIENT, mealIngredient)

fun Bundle.getSource() = getInt(BUNDLE_MEAL_SOURCE, -1)
    .let { if (it == -1) null else Source.values()[it] }

fun Bundle.putSource(source: Source) = putInt(BUNDLE_MEAL_SOURCE, source.ordinal)

fun Bundle.getMealSubmissionId() = getSerializable(BUNDLE_SUBMISSION_ID) as Int?

fun Bundle.putMealSubmissionId(submissionId: Int?) =
    putSerializable(BUNDLE_SUBMISSION_ID, submissionId)

fun Bundle.getRestaurantSubmissionId() = getString(BUNDLE_RESTAURANT_SUBMISSION_ID)

fun Bundle.putRestaurantSubmissionId(submissionId: String?) =
    putString(BUNDLE_RESTAURANT_SUBMISSION_ID, submissionId)

fun Bundle.getRestaurantItem() = getParcelable<RestaurantItem>(BUNDLE_RESTAURANT_ITEM)

fun Bundle.putRestaurantItem(restaurantItem: RestaurantItem?) =
    putParcelable(BUNDLE_RESTAURANT_ITEM, restaurantItem)

fun Bundle.getRestaurantInfo() = getParcelable<RestaurantInfo>(BUNDLE_RESTAURANT_INFO)

//fun Bundle.getRestaurantInfoEdit() = getParcelable<RestaurantInfo>(BUNDLE_RESTAURANT_INFO_EDIT)

fun Bundle.putRestaurantInfo(restaurantInfo: RestaurantInfo?) =
    putParcelable(BUNDLE_RESTAURANT_INFO, restaurantInfo)

//fun Bundle.putRestaurantInfoEdit(restaurantInfo: RestaurantInfo?) =
//    putParcelable(BUNDLE_RESTAURANT_INFO_EDIT, restaurantInfo)

fun Bundle.getCuisines() = getParcelableArrayList<Cuisine>(BUNDLE_CUISINES_LIST)

fun Bundle.putCuisines(cuisines: ArrayList<Cuisine>) =
    putParcelableArrayList(BUNDLE_CUISINES_LIST, cuisines)

fun Bundle.getDbId() = getSerializable(BUNDLE_MEAL_DB_ID) as Long?

fun Bundle.putDbId(bdId: Long?) = putSerializable(BUNDLE_MEAL_DB_ID, bdId)

fun Bundle.getItemActions() = this.getIntegerArrayList(BUNDLE_ACTIONS_LIST)
    ?.let { indexes ->
        val actions = ItemAction.values()
        indexes.map { idx -> actions[idx] }
    }

fun Bundle.putItemActions(vararg itemActions: ItemAction) = putItemActions(itemActions.asList())

fun Bundle.putItemActions(itemActions: List<ItemAction>) =
    putIntegerArrayList(BUNDLE_ACTIONS_LIST, ArrayList(itemActions.map { it.ordinal }))

fun Bundle.getNavigation(): Navigation = getInt(BUNDLE_NAVIGATION)
    .let { idx -> Navigation.values()[idx] }

fun Bundle.putNavigation(navigation: Navigation) = putInt(BUNDLE_NAVIGATION, navigation.ordinal)