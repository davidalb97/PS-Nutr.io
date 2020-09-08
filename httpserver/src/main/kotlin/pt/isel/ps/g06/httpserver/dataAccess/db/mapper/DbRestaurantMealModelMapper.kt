package pt.isel.ps.g06.httpserver.dataAccess.db.mapper

import org.springframework.stereotype.Component
import pt.isel.ps.g06.httpserver.dataAccess.common.mapper.ModelMapper
import pt.isel.ps.g06.httpserver.dataAccess.common.mapper.RestaurantModelMapper
import pt.isel.ps.g06.httpserver.dataAccess.db.MEAL_TYPE_CUSTOM
import pt.isel.ps.g06.httpserver.dataAccess.db.SubmissionContractType
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbRestaurantMealDto
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.*
import pt.isel.ps.g06.httpserver.model.MealRestaurantInfo
import pt.isel.ps.g06.httpserver.model.RestaurantMeal
import pt.isel.ps.g06.httpserver.model.VoteState
import pt.isel.ps.g06.httpserver.model.Votes
import pt.isel.ps.g06.httpserver.model.modular.toUserPortion
import pt.isel.ps.g06.httpserver.model.modular.toUserPredicate
import pt.isel.ps.g06.httpserver.model.modular.toUserVote

@Component
class DbRestaurantMealModelMapper(
        private val dbMealModelMapper: DbMealModelMapper,
        private val dbMealRepo: MealDbRepository,
        private val dbRestaurantModelMapper: RestaurantModelMapper,
        private val dbRestaurantDbRepository: RestaurantDbRepository,
        private val dbRestaurantMealInfoModelMapper: DbRestaurantMealInfoModelMapper
) : ModelMapper<DbRestaurantMealDto, RestaurantMeal> {

    override fun mapTo(dto: DbRestaurantMealDto): RestaurantMeal {
        return RestaurantMeal(
                restaurant = dbRestaurantDbRepository.getById(dto.restaurant_submission_id)!!
                        .let(dbRestaurantModelMapper::mapTo),
                meal = dbMealRepo.getById(dto.meal_submission_id)!!
                        .let(dbMealModelMapper::mapTo),
                info = dbRestaurantMealInfoModelMapper.mapTo(dto)
        )
    }
}