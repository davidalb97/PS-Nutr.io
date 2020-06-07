package pt.isel.ps.g06.httpserver.dataAccess.input

import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@ValidMeal
data class MealInput(
        @field:NotBlank(message = "A name must be given for the meal!")
        val name: String?,
        @field:NotBlank(message = "You must give at least one ingredient!")
        val ingredients: Collection<IngredientInput>?,
        @field:NotNull(message = "You must give at least one cuisine!")
        val cuisines: Collection<String>?,

        //Represents the ID of the API submitter under the database
        val restaurantApiSubmitterId: Int?,
        //Represents the ID of the restaurant
        val restaurantApiId: String?,
        val submissionId: Int?
) {
    fun isRestaurantMeal(): Boolean {
        return restaurantApiSubmitterId != null
    }
}

@Constraint(validatedBy = [MealInputValidator::class])
annotation class ValidMeal

class MealInputValidator : ConstraintValidator<ValidMeal, MealInput> {
    /**
     *  For an inserted Meal to be valid, it must:
     *  - Have a name;
     *  - Have at least one ingredient;
     *  - Have at least one cuisine;
     *  - Either have no restaurant identifiers (if no linkage is done between restaurant and Meal), or
     *  a valid pair. See [validateRestaurantAssociation] for more information.
     */
    override fun isValid(value: MealInput?, context: ConstraintValidatorContext?): Boolean {
        return value != null && validateRestaurantAssociation(value)
    }

    private fun validateRestaurantAssociation(meal: MealInput): Boolean {
        return if (meal.isRestaurantMeal()) {
            meal.restaurantApiId != null && meal.restaurantApiId.isNotBlank() || meal.submissionId != null
        } else {
            meal.restaurantApiId == null && meal.submissionId == null
        }
    }
}