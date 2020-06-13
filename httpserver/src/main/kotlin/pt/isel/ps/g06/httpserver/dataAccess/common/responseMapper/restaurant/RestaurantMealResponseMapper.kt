package pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper.restaurant

import org.springframework.stereotype.Component
import pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper.ResponseMapper
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbRestaurantMealDto
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.MealDbRepository
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.PortionDbRepository
import pt.isel.ps.g06.httpserver.model.MealRestaurantInfo

@Component
class DbRestaurantMealResponseMapper(
        private val dbPortionsMapper: DbRestaurantMealPortionsResponseMapper,
        private val dbVotesMapper: DbVotesResponseMapper,
        private val dbMealRepo: MealDbRepository,
        private val dbPortionRepo: PortionDbRepository
) : ResponseMapper<DbRestaurantMealDto, MealRestaurantInfo> {

    override fun mapTo(dto: DbRestaurantMealDto): MealRestaurantInfo {
        return MealRestaurantInfo(
                votes = lazy { dbVotesMapper.mapTo(dbMealRepo.getVotes(dto.submission_id)) },
                userVote = { userId -> dbMealRepo.getUserVote(dto.submission_id, userId) },
                portions = dbPortionRepo.getAllByRestaurantMealId(dto.submission_id).map(dbPortionsMapper::mapTo),
                userPortion = { userId ->
                    dbPortionRepo
                            .getUserPortion(dto.submission_id, userId)
                            ?.let(dbPortionsMapper::mapTo)
                }
        )
    }
    //    override fun mapTo(dto: DbRestaurantMealDto): RestaurantMeal {
//        return RestaurantMeal(
//                //A restaurant meal always has a meal
//                meal = dbMealMapper.mapTo(dbMealRepo.getById(dto.meal_submission_id)!!),
//                votes = dbVotesMapper.mapTo(dbMealRepo.getVotes(dto.submission_id)),
//                userVote = { userId -> dbMealRepo.getUserVote(dto.submission_id, userId) },
//                portions = dbPortionRepo.getAllByRestaurantMealId(dto.submission_id).map(dbPortionsMapper::mapTo),
//                userPortion = { userId ->
//                    dbPortionRepo.getUserPortion(dto.submission_id, userId)?.let { portionDto ->
//                        dbPortionsMapper.mapTo(portionDto)
//                    }
//                }
//        )
//    }
}