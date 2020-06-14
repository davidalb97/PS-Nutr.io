package pt.isel.ps.g06.httpserver.service

import org.springframework.stereotype.Service
import pt.isel.ps.g06.httpserver.common.exception.clientError.ForbiddenInsertionException
import pt.isel.ps.g06.httpserver.common.exception.clientError.NotYetVotedException
import pt.isel.ps.g06.httpserver.common.exception.notFound.RestaurantMealNotFound
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.PortionDbRepository
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.RestaurantMealDbRepository
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.VoteDbRepository
import pt.isel.ps.g06.httpserver.model.Meal
import pt.isel.ps.g06.httpserver.model.Restaurant
import pt.isel.ps.g06.httpserver.model.VoteState

@Service
class RestaurantMealService(
        private val restaurantService: RestaurantService,
        private val mealService: MealService,
        private val dbRestaurantMealRepository: RestaurantMealDbRepository,
        private val dbPortionRepository: PortionDbRepository,
        private val dbVoteRepository: VoteDbRepository
) {
    fun getRestaurantMeal(restaurant: Restaurant, mealId: Int): Meal {
        val meal = mealService.getMeal(mealId) ?: throw RestaurantMealNotFound()

        //Although the meal exists, it is not associated to given Restaurant
        //Hence, it is not a RestaurantMeal
        if (!meal.isRestaurantMeal(restaurant)) {
            throw RestaurantMealNotFound()
        }

        return meal
    }

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


    //TODO Add or alter
    fun addRestaurantMealPortion(restaurant: Restaurant, mealId: Int, submitterId: Int, quantity: Int) {
        val meal = getRestaurantMeal(restaurant, mealId)
        val restaurantInfo = meal.restaurantInfoSupplier(restaurant.identifier.value)

        //If given meal is a suggested Meal, make a RestaurantMeal first before adding user portion
        val restaurantMealIdentifier = restaurantInfo
                ?.restaurantMealIdentifier
                ?: this.addRestaurantMeal(restaurant, meal)

        dbPortionRepository.insert(submitterId, restaurantMealIdentifier, quantity)
    }

    fun alterRestaurantMealVote(restaurant: Restaurant, mealId: Int, submitterId: Int, vote: Boolean) {
        val restaurantMeal = getRestaurantMeal(restaurant, mealId)

        if (!restaurantMeal.isUserMeal()) {
            throw ForbiddenInsertionException("Only restaurant meals created by users can be voted!")
        }

        val restaurantInfo = restaurantMeal
                .restaurantInfoSupplier(restaurant.identifier.value)
                ?: throw IllegalStateException("Expected RestaurantInfo for given RestaurantMeal, but none was found!")

        dbVoteRepository.insertOrUpdate(submitterId, restaurantInfo.restaurantMealIdentifier, vote)
    }

    fun deleteRestaurantMealVote(restaurant: Restaurant, mealId: Int, submitterId: Int) {
        val restaurantMeal = getRestaurantMeal(restaurant, mealId)

        if (!restaurantMeal.isUserMeal()) {
            throw ForbiddenInsertionException("Only restaurant meals created by users can be voted!")
        }

        val restaurantInfo = restaurantMeal
                .restaurantInfoSupplier(restaurant.identifier.value)
                ?: throw IllegalStateException("Expected RestaurantInfo for given RestaurantMeal, but none was found!")

        if (restaurantInfo.userVote(submitterId) == VoteState.NOT_VOTED) {
            throw NotYetVotedException("You have not yet voted for this Restaurant Meal!")
        }

        dbVoteRepository.delete(submitterId, restaurantInfo.restaurantMealIdentifier)
    }
}

