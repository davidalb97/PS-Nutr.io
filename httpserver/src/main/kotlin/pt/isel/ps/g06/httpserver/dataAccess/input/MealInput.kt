package pt.isel.ps.g06.httpserver.dataAccess.input

import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import javax.validation.Payload
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty
import kotlin.reflect.KClass

@ValidMeal
data class MealInput(
        @field:NotBlank(message = "A name must be given for the meal!")
        val name: String?,
        @field:Min(value = 1, message = "A meal quantity must be given!")
        val quantity: Int?,
        @field:NotEmpty(message = "You must give at least one ingredient!")
        val ingredients: Collection<IngredientInput>?,
        @field:NotEmpty(message = "You must give at least one cuisine!")
        val cuisines: Collection<String>?,

        //Represents the ID of the API submitter under the database
        val restaurantApiSubmitterId: Int?,
        //Represents the ID of the restaurant
        val restaurantApiId: String?,
        val restaurantSubmissionId: Int?
) {
    fun isRestaurantMeal(): Boolean {
        return restaurantApiSubmitterId != null
    }
}

@Target(AnnotationTarget.CLASS)
@Constraint(validatedBy = [MealInputValidator::class])
annotation class ValidMeal(
        val message: String = "Invalid MealInput. Please check the documentation for more information.",
        val groups: Array<KClass<*>> = [],
        val payload: Array<KClass<out Payload>> = []
)

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
            meal.restaurantApiId != null && meal.restaurantApiId.isNotBlank() || meal.restaurantSubmissionId != null
        } else {
            meal.restaurantApiId == null && meal.restaurantSubmissionId == null
        }
    }
}