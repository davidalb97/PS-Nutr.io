package pt.isel.ps.g06.httpserver.service

import org.springframework.stereotype.Service
import pt.isel.ps.g06.httpserver.common.exception.clientError.DuplicateMealException
import pt.isel.ps.g06.httpserver.common.exception.forbidden.NotSubmissionOwnerException
import pt.isel.ps.g06.httpserver.common.exception.notFound.PortionNotFoundException
import pt.isel.ps.g06.httpserver.common.exception.notFound.RestaurantMealNotFound
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.PortionDbRepository
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.RestaurantMealDbRepository
import pt.isel.ps.g06.httpserver.model.food.Meal
import pt.isel.ps.g06.httpserver.model.restaurant.Restaurant
import pt.isel.ps.g06.httpserver.model.restaurant.RestaurantIdentifier
import java.util.stream.Stream
import kotlin.streams.toList

@Service
class RestaurantMealService(
        private val restaurantService: RestaurantService,
        private val mealService: MealService,
        private val submissionService: SubmissionService,
        private val dbRestaurantMealRepository: RestaurantMealDbRepository,
        private val dbPortionRepository: PortionDbRepository
) {
    fun getRestaurantMeal(restaurant: Restaurant, mealId: Int): Meal {
        return findRestaurantMeal(mealId, restaurant.meals)
                ?: findRestaurantMeal(mealId, restaurant.suggestedMeals)
                ?: throw RestaurantMealNotFound()
    }

    fun addRestaurantMeal(restaurant: Restaurant, meal: Meal, submitterId: Int? = null): Int {
        var targetRestaurant = restaurant

        if (!targetRestaurant.identifier.value.isPresentInDatabase()) {
            //Ensure that existing Restaurant exists in database before creating associations for it
            targetRestaurant = restaurantService.createRestaurant(
                    submitterId = targetRestaurant.identifier.value.submitterId,
                    apiId = targetRestaurant.identifier.value.apiId,
                    restaurantName = restaurant.name,
                    cuisines = restaurant.cuisines.map { it.name }.toList(),
                    latitude = restaurant.latitude,
                    longitude = restaurant.longitude
            )
        }

        if (meal.getRestaurantMeal(targetRestaurant.identifier.value.submissionId!!) != null) {
            //Given meal already is in given restaurant
            throw DuplicateMealException()
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
                ?: addRestaurantMeal(restaurant.identifier.value, meal)

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

    private fun findRestaurantMeal(mealId: Int, meals: Stream<Meal>): Meal? {
        return meals.filter { it.identifier == mealId }.findAny().orElse(null)
    }

}