package pt.isel.ps.g06.httpserver.service

import org.springframework.stereotype.Service
import pt.isel.ps.g06.httpserver.common.exception.clientError.ForbiddenInsertionResponseStatusException
import pt.isel.ps.g06.httpserver.common.exception.clientError.NotYetVotedResponseStatusException
import pt.isel.ps.g06.httpserver.common.exception.forbidden.NotSubmissionOwnerException
import pt.isel.ps.g06.httpserver.common.exception.notFound.PortionNotFoundException
import pt.isel.ps.g06.httpserver.common.exception.notFound.RestaurantMealNotFound
import pt.isel.ps.g06.httpserver.common.exception.notFound.RestaurantNotFoundException
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.PortionDbRepository
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.RestaurantMealDbRepository
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.VoteDbRepository
import pt.isel.ps.g06.httpserver.model.*

@Service
class RestaurantMealService(
        private val restaurantService: RestaurantService,
        private val mealService: MealService,
        private val submissionService: SubmissionService,
        private val dbRestaurantMealRepository: RestaurantMealDbRepository,
        private val dbPortionRepository: PortionDbRepository,
        private val dbVoteRepository: VoteDbRepository
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
        //TODO Check if given meal already is in restaurant
        var restaurant = restaurantService.getRestaurant(restaurantId) ?: throw RestaurantNotFoundException()

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
                ?.restaurantMealIdentifier
                ?: this.addRestaurantMeal(restaurant.identifier.value, meal)

        dbPortionRepository.insert(submitterId, restaurantMealIdentifier, quantity)
    }

    fun alterRestaurantMealVote(restaurantId: RestaurantIdentifier, mealId: Int, submitterId: Int, vote: Boolean) {
        val restaurantMealInfo = getRestaurantMealInfo(restaurantId, mealId)

        dbVoteRepository.insertOrUpdate(submitterId, restaurantMealInfo.restaurantMealIdentifier, vote)
    }

    fun deleteRestaurantMealVote(restaurantId: RestaurantIdentifier, mealId: Int, submitterId: Int) {
        val restaurantMealInfo = getRestaurantMealInfo(restaurantId, mealId)

        if (restaurantMealInfo.userVote(submitterId) == VoteState.NOT_VOTED) {
            throw NotYetVotedResponseStatusException("You have not yet voted for this Restaurant Meal!")
        }

        dbVoteRepository.delete(submitterId, restaurantMealInfo.restaurantMealIdentifier)
    }

    fun deleteRestaurantMeal(restaurantId: RestaurantIdentifier, mealId: Int, submitterId: Int) {
        val restaurantMeal = getRestaurantMeal(restaurantId, mealId)
        val meal = restaurantMeal.meal

        if (!meal.isUserMeal() || meal.creatorInfo.value!!.identifier != submitterId) {
            throw NotSubmissionOwnerException()
        }

        val restaurantInfo = meal
                .getMealRestaurantInfo(restaurantMeal.restaurant.identifier.value)
                ?: throw IllegalStateException("Expected RestaurantInfo for given RestaurantMeal, but none was found!")

        submissionService.deleteSubmission(restaurantInfo.restaurantMealIdentifier, submitterId)
    }

    fun deleteUserPortion(restaurantId: RestaurantIdentifier, mealId: Int, submitterId: Int) {
        val userPortion = getUserPortion(restaurantId, mealId, submitterId) ?: throw PortionNotFoundException()
        submissionService.deleteSubmission(userPortion.identifier, submitterId)
    }

    private fun getUserPortion(restaurantId: RestaurantIdentifier, mealId: Int, submitterId: Int): Portion? {
        val restaurantMealInfo = getRestaurantMealInfo(restaurantId, mealId)
        return restaurantMealInfo.userPortion(submitterId)
    }

    private fun getRestaurantMealInfo(restaurantId: RestaurantIdentifier, mealId: Int): MealRestaurantInfo {
        val restaurantMeal = getRestaurantMeal(restaurantId, mealId)
        val meal = restaurantMeal.meal

        if (!meal.isUserMeal()) {
            throw ForbiddenInsertionResponseStatusException("Only restaurant meals created by users can be voted!")
        }

        return meal
                .getMealRestaurantInfo(restaurantMeal.restaurant.identifier.value)
                ?: throw IllegalStateException("Expected RestaurantInfo for given RestaurantMeal, but none was found!")
    }
}