package pt.isel.ps.g06.httpserver.service

import org.springframework.stereotype.Service
import pt.isel.ps.g06.httpserver.common.exception.clientError.DuplicateMealException
import pt.isel.ps.g06.httpserver.common.exception.forbidden.NotSubmissionOwnerException
import pt.isel.ps.g06.httpserver.common.exception.notFound.PortionNotFoundException
import pt.isel.ps.g06.httpserver.common.exception.notFound.RestaurantMealNotFound
import pt.isel.ps.g06.httpserver.common.exception.notFound.RestaurantNotFoundException
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.PortionDbRepository
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.RestaurantMealDbRepository
import pt.isel.ps.g06.httpserver.model.food.Meal
import pt.isel.ps.g06.httpserver.model.restaurant.RestaurantIdentifier

@Service
class RestaurantMealService(
        private val restaurantService: RestaurantService,
        private val mealService: MealService,
        private val submissionService: SubmissionService,
        private val dbRestaurantMealRepository: RestaurantMealDbRepository,
        private val dbPortionRepository: PortionDbRepository
) {
    fun getRestaurantMeal(restaurantId: RestaurantIdentifier, mealId: Int): RestaurantMeal {
        val restaurant = restaurantService
                .getRestaurant(restaurantId.submitterId, restaurantId.submissionId, restaurantId.apiId)
                ?: throw RestaurantNotFoundException()

        val meal = mealService.getMeal(mealId) ?: throw RestaurantMealNotFound()

        //Although the meal exists, it is not associated to given Restaurant
        //Hence, it is not a RestaurantMeal
        if (!meal.isRestaurantMeal(restaurant)) {
            throw RestaurantMealNotFound()
        }

        return RestaurantMeal(restaurant, meal)
    }

    fun addRestaurantMeal(restaurantId: RestaurantIdentifier, meal: Meal, submitterId: Int? = null): Int {
        var restaurant = restaurantService.getRestaurant(restaurantId) ?: throw RestaurantNotFoundException()

        if (meal.getMealRestaurantInfo(restaurantId) != null) {
            //Given meal already is in given restaurant
            throw DuplicateMealException()
        }

        if (!restaurant.identifier.value.isPresentInDatabase()) {
            //Ensure that existing Restaurant exists in database before creating associations for it
            restaurant = restaurantService.createRestaurant(
                    submitterId = restaurantId.submitterId,
                    apiId = restaurantId.apiId,
                    restaurantName = restaurant.name,
                    cuisines = restaurant.cuisines.map { it.name }.toList(),
                    latitude = restaurant.latitude,
                    longitude = restaurant.longitude
            )
        }

        val (submission_id) = dbRestaurantMealRepository.insert(
                submitterId = submitterId,
                mealId = meal.identifier,
                //We are sure submissionIdentifier exists because we ensure database restaurant before
                restaurantId = restaurant.identifier.value.submissionId!!
        )

        return submission_id
    }


    fun addRestaurantMealPortion(restaurantId: RestaurantIdentifier, mealId: Int, submitterId: Int, quantity: Int) {
        val restaurantMeal = getRestaurantMeal(restaurantId, mealId)
        val meal = restaurantMeal.meal
        val restaurant = restaurantMeal.restaurant

        val restaurantInfo = meal.getMealRestaurantInfo(restaurantMeal.restaurant.identifier.value)

        //If given meal is a suggested Meal, make a RestaurantMeal first before adding user portion
        val restaurantMealIdentifier = restaurantInfo
                .restaurantMealIdentifier
                ?: this.addRestaurantMeal(restaurant.identifier.value, meal)

        dbPortionRepository.insert(submitterId, restaurantMealIdentifier, quantity)
    }

    fun deleteRestaurantMeal(restaurantId: RestaurantIdentifier, mealId: Int, submitterId: Int) {
        val restaurantMeal = getRestaurantMeal(restaurantId, mealId)
        val meal = restaurantMeal.meal

        if (!meal.isUserMeal() || meal.submitterInfo.value!!.identifier != submitterId) {
            throw NotSubmissionOwnerException()
        }

        val restaurantInfo = meal
                .getMealRestaurantInfo(restaurantMeal.restaurant.identifier.value)
                ?: throw IllegalStateException("Expected RestaurantInfo for given RestaurantMeal, but none was found!")

        submissionService.deleteSubmission(restaurantInfo.restaurantMealIdentifier, submitterId)
    }

    fun deleteUserPortion(restaurantId: RestaurantIdentifier, mealId: Int, submitterId: Int) {
        val restaurantMeal = getRestaurantMeal(restaurantId, mealId)
        val userPortion = restaurantMeal.getRestaurantMealInfo()
                .let { it.userPortion(submitterId) }
                ?: throw PortionNotFoundException()

        submissionService.deleteSubmission(userPortion.identifier, submitterId)
    }
}