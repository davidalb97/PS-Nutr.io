package pt.isel.ps.g06.httpserver.service

import org.springframework.stereotype.Service
import pt.isel.ps.g06.httpserver.common.exception.problemJson.badRequest.InvalidInputException
import pt.isel.ps.g06.httpserver.common.exception.problemJson.conflict.DuplicateMealException
import pt.isel.ps.g06.httpserver.common.exception.problemJson.forbidden.NotSubmissionOwnerException
import pt.isel.ps.g06.httpserver.common.exception.problemJson.notFound.MealNotFoundException
import pt.isel.ps.g06.httpserver.common.exception.problemJson.notFound.PortionNotFoundException
import pt.isel.ps.g06.httpserver.common.exception.problemJson.notFound.RestaurantMealNotFound
import pt.isel.ps.g06.httpserver.common.exception.problemJson.notFound.RestaurantNotFoundException
import pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper.restaurant.DbRestaurantMealInfoResponseMapper
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.FavoriteDbRepository
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.PortionDbRepository
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.ReportDbRepository
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.RestaurantMealDbRepository
import pt.isel.ps.g06.httpserver.dataAccess.input.restaurantMeal.PortionInput
import pt.isel.ps.g06.httpserver.model.*

@Service
class RestaurantMealService(
        private val restaurantService: RestaurantService,
        private val mealService: MealService,
        private val submissionService: SubmissionService,
        private val dbRestaurantMealRepository: RestaurantMealDbRepository,
        private val dbPortionRepository: PortionDbRepository,
        private val dbFavoriteDbRepository: FavoriteDbRepository,
        private val dbReportDbRepository: ReportDbRepository,
        private val dbRestaurantMealResponseMapper: DbRestaurantMealInfoResponseMapper
) {

    /**
     * Returns a meal that is associated with a restaurant.
     * This will not insert the restaurant nor the restaurant meal, although it requires the meal to be inserted.
     * @param restaurantId The [RestaurantIdentifier] from the existing restaurant.
     * @param mealId The meal submission id.
     * @throws RestaurantNotFoundException If the restaurant does not exist.
     * @throws MealNotFoundException If the meal does not exist.
     * @throws RestaurantMealNotFound If the meal is not associated with the restaurant.
     * @return The [RestaurantMeal] that resulted from the restaurant - meal association.
     */
    fun getRestaurantMeal(restaurantId: RestaurantIdentifier, mealId: Int): RestaurantMeal {
        val restaurant = restaurantService.getRestaurant(restaurantId) ?: throw RestaurantNotFoundException()

        val meal = mealService.getMeal(mealId) ?: throw MealNotFoundException()

        //Although the meal exists, it is not associated to given Restaurant
        //Hence, it is not a RestaurantMeal
        if (!meal.isRestaurantMeal(restaurant)) {
            throw RestaurantMealNotFound()
        }

        //If the restaurant is inserted, get restaurant meal (if restaurant - meal is associated)
        val restaurantMeal = restaurantId.submissionId?.let {
            dbRestaurantMealRepository.getRestaurantMeal(it, mealId)
        }?.let(dbRestaurantMealResponseMapper::mapTo)

        return RestaurantMeal(
                info = restaurantMeal,
                restaurant = restaurant,
                meal = meal
        )
    }

    /**
     * Inserts the restaurant - meal association, returning the resulting [RestaurantMeal].
     * @param restaurantId The [RestaurantIdentifier] from the existing restaurant.
     * @param meal The existing [Meal].
     * @param submitterId The submitter of this restaurant meal.
     * @throws RestaurantNotFoundException If the restaurant does not exist.
     * @throws DuplicateMealException If restaurant meal association already exists.
     * @throws InvalidInputException If the [meal]'s submission id is not from a meal or
     * the [restaurantId]'s submission id is not from a restaurant.
     * @return The resulting [RestaurantMeal] from the association.
     */
    fun addRestaurantMeal(restaurantId: RestaurantIdentifier, meal: Meal, submitterId: Int? = null): RestaurantMeal {
        val restaurant = restaurantService.getOrInsertRestaurant(restaurantId)

        if (meal.getMealRestaurantInfo(restaurantId) != null) {
            //Given meal already is in given restaurant
            throw DuplicateMealException()
        }

        val restaurantMeal = dbRestaurantMealRepository.insert(
                submitterId = submitterId,
                mealId = meal.identifier,
                //We are sure submissionIdentifier exists because we ensure database restaurant before
                restaurantId = restaurant.identifier.value.submissionId!!,
                //Default Restaurant meal insertion is false
                verified = false
        )
        return RestaurantMeal(
                restaurant = restaurant,
                meal = meal,
                info = dbRestaurantMealResponseMapper.mapTo(restaurantMeal)
        )
    }

    /**
     * Gets or adds a new restaurant meal association.
     * @param restaurantId The existing restaurant identifier.
     * @param mealId The existing [Meal] identifier.
     * @throws RestaurantNotFoundException If the restaurant does not exist.
     * @throws MealNotFoundException If the meal does not exist.
     * @throws InvalidInputException If the [mealId] is not from a meal or
     * the [restaurantId]'s submission id is not from a restaurant.
     * @return The resulting [MealRestaurantInfo] from the (existing) association.
     */
    fun getOrAddRestaurantMeal(restaurantId: RestaurantIdentifier, mealId: Int): RestaurantMeal {
        val restaurant = restaurantService.getOrInsertRestaurant(restaurantId)

        val meal = mealService.getMeal(mealId) ?: throw MealNotFoundException()

        if (meal.isRestaurantMeal(restaurant)) {
            val restaurantMealInfo = meal.getMealRestaurantInfo(restaurant.identifier.value)

            //Suggested meals are restaurant meals but might not be inserted yet
            if (restaurantMealInfo != null) {
                return RestaurantMeal(
                        restaurant = restaurant,
                        meal = meal,
                        info = restaurantMealInfo
                )
            }
        }

        return addRestaurantMeal(restaurantId, meal, null)
    }

    /**
     * Inserts a portion a [RestaurantMeal] association. Creating the association if it does not exist.
     * @param restaurantId The [RestaurantIdentifier] from the existing restaurant.
     * @param mealId The existing [Meal]'s submission id.
     * @param submitterId The submitter of this restaurant meal portion.
     * @param quantity The quantity of this restaurant meal's portion.
     * @throws RestaurantNotFoundException If the restaurant does not exist.
     * @throws MealNotFoundException If the meal does not exist.
     * @throws InvalidInputException If the [mealId] is not from a meal or
     * the [restaurantId]'s submission id is not from a restaurant.
     */
    fun addRestaurantMealPortion(restaurantId: RestaurantIdentifier, mealId: Int, submitterId: Int, quantity: Int) {
        val restaurantMeal = getOrAddRestaurantMeal(restaurantId, mealId)

        dbPortionRepository.insert(submitterId, restaurantMeal.info!!.identifier!!, quantity)
    }

    fun addReport(submitterId: Int, restaurantIdentifier: RestaurantIdentifier, mealId: Int, report: String) {
        val restaurantMeal = getOrAddRestaurantMeal(restaurantIdentifier, mealId)

        dbReportDbRepository.insert(submitterId, restaurantMeal.info!!.identifier!!, report)
    }

    fun updateRestaurantMealVerification(restaurantId: RestaurantIdentifier, mealId: Int, verified: Boolean) {
        val restaurantMealSubmissionId = getOrAddRestaurantMeal(restaurantId, mealId).info!!.identifier!!

        dbRestaurantMealRepository.putVerification(restaurantMealSubmissionId, verified)
    }

    fun deleteRestaurantMeal(restaurantId: RestaurantIdentifier, mealId: Int, user: User) {
        val restaurantMeal = getRestaurantMeal(restaurantId, mealId)
        val meal = restaurantMeal.meal

        if (!meal.isUserMeal() || meal.submitterInfo.value!!.identifier != user.identifier) {
            throw NotSubmissionOwnerException()
        }

        val restaurantInfo = meal
                .getMealRestaurantInfo(restaurantMeal.restaurant.identifier.value)
                ?: throw IllegalStateException("Expected RestaurantInfo for given RestaurantMeal, but none was found!")

        submissionService.deleteSubmission(restaurantInfo.identifier!!, user)
    }

    fun updateUserPortion(submitterId: Int, restaurantId: RestaurantIdentifier, mealId: Int, portion: PortionInput) {
        val restaurantMeal = getRestaurantMeal(restaurantId, mealId)

        val userPortion = restaurantMeal.getRestaurantMealInfo()
                ?.let { it.userPortion(submitterId) }
                ?: throw PortionNotFoundException()

        dbPortionRepository.update(userPortion.identifier, portion.quantity!!)
    }



    fun deleteUserPortion(submitterId: Int, restaurantId: RestaurantIdentifier, mealId: Int, user: User) {
        val restaurantMeal = getRestaurantMeal(restaurantId, mealId)
        val userPortion = restaurantMeal.getRestaurantMealInfo()
                ?.let { it.userPortion(submitterId) }
                ?: throw PortionNotFoundException()

        submissionService.deleteSubmission(userPortion.identifier, user)
    }

    /**
     *
     */
    fun setFavorite(restaurantId: RestaurantIdentifier, mealId: Int, submitterId: Int, isFavorite: Boolean) {
        val restaurantMeal = getOrAddRestaurantMeal(restaurantId, mealId)

        dbFavoriteDbRepository.setFavorite(restaurantMeal.info!!.identifier!!, submitterId, isFavorite)
    }
}