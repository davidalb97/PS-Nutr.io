package pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper.food

import org.springframework.stereotype.Component
import pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper.ResponseMapper
import pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper.restaurant.DbVotesResponseMapper
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbRestaurantMealDto
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.FavoriteDbRepository
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.MealDbRepository
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.PortionDbRepository
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.VoteDbRepository
import pt.isel.ps.g06.httpserver.model.food.RestaurantMeal
import pt.isel.ps.g06.httpserver.model.submission.Favorable
import pt.isel.ps.g06.httpserver.model.submission.Votable

@Component
class RestaurantMealResponseMapper(
        private val dbMealDbRepository: MealDbRepository,
        private val dbMealResponseMapper: DbMealResponseMapper,
        private val dbPortionDbRepository: PortionDbRepository,
        private val portionResponseMapper: DbRestaurantMealPortionResponseMapper,
        private val favoriteDbRepository: FavoriteDbRepository,
        private val voteDbRepository: VoteDbRepository,
        private val votesResponseMapper: DbVotesResponseMapper
) : ResponseMapper<DbRestaurantMealDto, RestaurantMeal> {

    override fun mapTo(dto: DbRestaurantMealDto): RestaurantMeal {
        val restaurantMealIdentifier = dto.submission_id

        val meal = dbMealDbRepository
                .getById(dto.meal_submission_id)!!
                .let(dbMealResponseMapper::mapTo)

        return RestaurantMeal(
                mealIdentifier = meal.identifier,
                restaurantMealIdentifier = restaurantMealIdentifier,
                name = meal.name,
                nutritionalValues = meal.nutritionalValues,
                composedBy = meal.composedBy,
                cuisines = meal.cuisines,
                submitterInfo = meal.submitterInfo,
                creationDate = meal.creationDate,
                imageUri = meal.imageUri,
                portions = dbPortionDbRepository
                        .getAllByRestaurantMealId(restaurantMealIdentifier)
                        .map(portionResponseMapper::mapTo),

                userPortion = {
                    dbPortionDbRepository
                            .getUserPortion(restaurantMealIdentifier, it.identifier)
                            ?.let(portionResponseMapper::mapTo)
                },

                votable = Votable(voteDbRepository
                        .getVotes(restaurantMealIdentifier)
                        .let(votesResponseMapper::mapTo)
                ) { voteDbRepository.getUserVote(restaurantMealIdentifier, it.identifier) },

                favorable = Favorable { favoriteDbRepository.getFavorite(restaurantMealIdentifier, it.identifier) }
        )
    }
}