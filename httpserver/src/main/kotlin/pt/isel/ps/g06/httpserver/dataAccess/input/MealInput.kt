package pt.isel.ps.g06.httpserver.dataAccess.input

import pt.isel.ps.g06.httpserver.dataAccess.api.food.FoodApiType
import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull


@ValidMeal
data class MealInput(
        val name: String?,
        val ingredients: List<IngredientInput>?,
        @field:NotNull(message = "Meal cuisines must not be null!")
        val cuisineNames: List<String>?,
        val apiId: Int?,
        val mealApiType: String?,
        @field:NotBlank(message = "Meal API type must not be empty!")
        val restaurantSubmitter: Int?,
        val restaurantIdentifier: String?
)

@Constraint(validatedBy = [MealInputValidator::class])
annotation class ValidMeal

class MealInputValidator : ConstraintValidator<ValidMeal, MealInput> {
    override fun isValid(value: MealInput?, context: ConstraintValidatorContext?): Boolean {
        return value != null
                //A meal must have apiType as an api meal or else is custom meal with api ingredients requires it
                && FoodApiType.getOrNull(value.mealApiType) != null
                //A meal must have cuisines
                && validCuisines(value)
                //A meal must either be an api meal or custom made from api ingredients
                && (isApi(value) || isCustom(value))
                //Validate restaurant association if any
                && validateRestaurantAssociation(value)
    }

    private fun isApi(value: MealInput): Boolean {
        return value.apiId != null
    }

    private fun isCustom(value: MealInput): Boolean {
        //A custom meal must have a name
        return value.name != null && value.name.isNotBlank()
                && value.ingredients != null && value.ingredients.isNotEmpty()
    }

    private fun validCuisines(value: MealInput): Boolean {
        return value.cuisineNames != null && value.cuisineNames.isNotEmpty()
    }

    private fun validateRestaurantAssociation(value: MealInput): Boolean {
        //If there is a restaurant association
        return value.restaurantSubmitter != null && value.restaurantIdentifier != null && value.restaurantIdentifier.isNotBlank()
                //If there isn't a restaurant association
                || value.restaurantSubmitter == null && value.restaurantIdentifier == null
    }
}