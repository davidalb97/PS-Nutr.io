package pt.isel.ps.g06.httpserver.service

import org.springframework.stereotype.Service
import pt.isel.ps.g06.httpserver.common.exception.clientError.DuplicateMealException
import pt.isel.ps.g06.httpserver.common.exception.forbidden.NotSubmissionOwnerException
import pt.isel.ps.g06.httpserver.common.exception.notFound.MealNotFound
import pt.isel.ps.g06.httpserver.common.exception.notFound.PortionNotFoundException
import pt.isel.ps.g06.httpserver.common.exception.notFound.RestaurantMealNotFound
import pt.isel.ps.g06.httpserver.common.exception.notFound.RestaurantNotFoundException
import pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper.restaurant.DbRestaurantMealInfoResponseMapper
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.FavoriteDbRepository
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.PortionDbRepository
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.RestaurantMealDbRepository
import pt.isel.ps.g06.httpserver.exception.InvalidInputException
import pt.isel.ps.g06.httpserver.model.Meal
import pt.isel.ps.g06.httpserver.model.MealRestaurantInfo
import pt.isel.ps.g06.httpserver.model.RestaurantIdentifier
import pt.isel.ps.g06.httpserver.model.RestaurantMeal

@Service
class RestaurantMealService(
        private val restaurantService: RestaurantService,
        private val mealService: MealService,
        private val submissionService: SubmissionService,
        private val dbRestaurantMealRepository: RestaurantMealDbRepository,
        private val dbPortionRepository: PortionDbRepository,
        private val dbFavoriteDbRepository: FavoriteDbRepository,
        private val dbRestaurantMealResponseMapper: DbRestaurantMealInfoResponseMapper
) {

    /**
     * Returns a meal that is associated with a restaurant.
     * @param restaurantId The [RestaurantIdentifier] from the existing restaurant.
     * @param mealId The meal submission id.
     * @throws RestaurantNotFoundException If the restaurant does not exist.
     * @throws MealNotFound If the meal does not exist.
     * @throws RestaurantMealNotFound If the meal does not exist or the meal is not associated with the restaurant.
     * @return The [RestaurantMeal] that resulted from the restaurant - meal association.
     */
    fun getRestaurantMeal(restaurantId: RestaurantIdentifier, mealId: Int): RestaurantMeal {
        val restaurant = restaurantService
                .getRestaurant(restaurantId.submitterId, restaurantId.submissionId, restaurantId.apiId)
                ?: throw RestaurantNotFoundException()

        val meal = mealService.getMeal(mealId) ?: throw MealNotFound()

        //Although the meal exists, it is not associated to given Restaurant
        //Hence, it is not a RestaurantMeal
        if (!meal.isRestaurantMeal(restaurant)) {
            throw RestaurantMealNotFound()
        }

        val restaurantMeal = dbRestaurantMealRepository.getRestaurantMeal(restaurantId.submissionId!!, mealId)

        return RestaurantMeal(restaurantMeal!!.submission_id, restaurant, meal, restaurantMeal.verified)
    }

    /**
     * Inserts the restaurant - meal association, returning the resulting submission id.
     * @param restaurantId The [RestaurantIdentifier] from the existing restaurant.
     * @param meal The existing [Meal].
     * @param submitterId The submitter of this restaurant meal.
     * @throws RestaurantNotFoundException If the restaurant does not exist.
     * @throws DuplicateMealException If restaurant meal association already exists.
     * @throws InvalidInputException If the [meal]'s submission id is not from a meal or
     * the [restaurantId]'s submission id is not from a restaurant.
     * @return The resulting [MealRestaurantInfo] from the association.
     */
    fun addRestaurantMeal(restaurantId: RestaurantIdentifier, meal: Meal, submitterId: Int? = null): MealRestaurantInfo {
        val restaurant = restaurantService.getOrCreateRestaurant(restaurantId)

        if (meal.getMealRestaurantInfo(restaurantId) != null) {
            //Given meal already is in given restaurant
            throw DuplicateMealException()
        }

        val restaurantMeal = dbRestaurantMealRepository.insert(
                submitterId = submitterId,
                mealId = meal.identifier,
                //We are sure submissionIdentifier exists because we ensure database restaurant before
                restaurantId = restaurant.identifier.value.submissionId!!,
                //Default Restaurant meal insertion is false, TODO check with group!
                verified = false
        )
        return dbRestaurantMealResponseMapper.mapTo(restaurantMeal)
    }

    /**
     * Gets or adds a new restaurant meal association.
     * @param restaurantId The existing restaurant identifier.
     * @param mealId The existing [Meal] identifier.
     * @param submitterId The submitter of this restaurant meal.
     * @throws RestaurantNotFoundException If the restaurant does not exist.
     * @throws MealNotFound If the meal does not exist.
     * @throws InvalidInputException If the [mealId] is not from a meal or
     * the [restaurantId]'s submission id is not from a restaurant.
     * @return The resulting [MealRestaurantInfo] from the (existing) association.
     */
    fun getOrAddRestaurantMeal(restaurantId: RestaurantIdentifier, mealId: Int, submitterId: Int): MealRestaurantInfo {
        val restaurant = restaurantService.getOrCreateRestaurant(restaurantId)

        val meal = mealService.getMeal(mealId) ?: throw MealNotFound()

        if (meal.isRestaurantMeal(restaurant)) {
            val restaurantMeal = meal.getMealRestaurantInfo(restaurant.identifier.value)

            //Suggested meals are restaurant meals but might not be inserted yet
            if(restaurantMeal != null) {
                return restaurantMeal
            }
        }

        return addRestaurantMeal(restaurantId, meal, submitterId)
    }

    /**
     * Inserts a portion a [RestaurantMeal] association. Creating the association if it does not exist.
     * @param restaurantId The [RestaurantIdentifier] from the existing restaurant.
     * @param mealId The existing [Meal]'s submission id.
     * @param submitterId The submitter of this restaurant meal portion.
     * @param quantity The quantity of this restaurant meal's portion.
     * @throws RestaurantNotFoundException If the restaurant does not exist.
     * @throws MealNotFound If the meal does not exist.
     * @throws InvalidInputException If the [mealId] is not from a meal or
     * the [restaurantId]'s submission id is not from a restaurant.
     */
    fun addRestaurantMealPortion(restaurantId: RestaurantIdentifier, mealId: Int, submitterId: Int, quantity: Int) {
        val restaurantMeal = getOrAddRestaurantMeal(restaurantId, mealId, submitterId)

        dbPortionRepository.insert(submitterId, restaurantMeal.restaurantMealIdentifier, quantity)
    }


    fun updateRestaurantMealVerification(restaurantId: RestaurantIdentifier, mealId: Int, verified: Boolean) {
        val restaurantMealSubmissionId = getRestaurantMeal(restaurantId, mealId).submissionId

        dbRestaurantMealRepository.putVerification(restaurantMealSubmissionId, verified)
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
                ?.let { it.userPortion(submitterId) }
                ?: throw PortionNotFoundException()

        submissionService.deleteSubmission(userPortion.identifier, submitterId)
    }

    /**
     *
     */
    fun setFavorite(restaurantId: RestaurantIdentifier, mealId: Int, submitterId: Int, isFavorite: Boolean) {
        val restaurantMeal = getOrAddRestaurantMeal(restaurantId, mealId, submitterId)

        dbFavoriteDbRepository.setFavorite(restaurantMeal.restaurantMealIdentifier, submitterId, isFavorite)
    }
}