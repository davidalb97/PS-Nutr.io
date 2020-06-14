package pt.isel.ps.g06.httpserver.service

import org.springframework.stereotype.Service
import pt.isel.ps.g06.httpserver.common.exception.ForbiddenInsertionException
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.PortionDbRepository
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.RestaurantMealDbRepository
import pt.isel.ps.g06.httpserver.model.Meal
import pt.isel.ps.g06.httpserver.model.Restaurant

@Service
class RestaurantMealService(
        private val restaurantService: RestaurantService,
        private val dbRestaurantMealRepository: RestaurantMealDbRepository,
        private val dbPortionRepository: PortionDbRepository
) {
    fun addRestaurantMeal(restaurant: Restaurant, meal: Meal, submitterId: Int? = null): Int {
        val databaseRestaurant = if (!restaurant.identifier.value.isPresentInDatabase()) {
            //Ensure that existing Restaurant exists in database before creating associations for it
            restaurantService.createRestaurant(
                    submitterId = restaurant.identifier.value.submitterId,
                    apiId = restaurant.identifier.value.apiId,
                    restaurantName = restaurant.name,
                    cuisines = restaurant.cuisines.map { it.name }.toList(),
                    latitude = restaurant.latitude,
                    longitude = restaurant.longitude
            )
        } else restaurant

        val (submission_id) = dbRestaurantMealRepository.insert(
                submitterId = submitterId,
                mealId = meal.identifier,
                //We are sure submissionIdentifier exists because we ensure database restaurant before
                restaurantId = databaseRestaurant.identifier.value.submissionId!!
        )

        return submission_id
    }

    fun addRestaurantMealPortion(restaurant: Restaurant, meal: Meal, submitterId: Int, quantity: Int) {
        if (!meal.isRestaurantMeal(restaurant)) {
            throw ForbiddenInsertionException("Given Meal is not in this Restaurant!")
        }

        val restaurantInfo = meal.restaurantInfo(restaurant.identifier.value)

        //If given meal is a suggested Meal, make a RestaurantMeal first before adding user portion
        val restaurantMealIdentifier = restaurantInfo
                ?.restaurantMealIdentifier
                ?: this.addRestaurantMeal(restaurant, meal)

        dbPortionRepository.insert(submitterId, restaurantMealIdentifier, quantity)
    }
}

