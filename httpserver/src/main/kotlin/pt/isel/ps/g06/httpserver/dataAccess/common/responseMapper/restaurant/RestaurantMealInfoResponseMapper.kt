package pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper.restaurant

import org.springframework.stereotype.Component
import pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper.ResponseMapper
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbRestaurantMealDto
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.MealDbRepository
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.PortionDbRepository
import pt.isel.ps.g06.httpserver.model.MealRestaurantInfo
import pt.isel.ps.g06.httpserver.model.VoteState
import pt.isel.ps.g06.httpserver.model.Votes

@Component
class DbRestaurantMealInfoResponseMapper(
        private val dbPortionsMapper: DbRestaurantMealPortionsResponseMapper,
        private val dbVotesMapper: DbVotesResponseMapper,
        private val dbMealRepo: MealDbRepository,
        private val dbPortionRepo: PortionDbRepository
) : ResponseMapper<DbRestaurantMealDto, MealRestaurantInfo> {

    override fun mapTo(dto: DbRestaurantMealDto): MealRestaurantInfo {
        return MealRestaurantInfo(
                restaurantMealIdentifier = dto.submission_id,
                votes = lazy { dto.submission_id?.let { dbVotesMapper.mapTo(dbMealRepo.getVotes(it)) } ?: Votes(0, 0) },
                userVote = { userId ->
                    dto.submission_id?.let { dbMealRepo.getUserVote(it, userId) } ?: VoteState.NOT_VOTED
                },
                portions = dto.submission_id?.let { dbPortionRepo.getAllByRestaurantMealId(it) }?.map(dbPortionsMapper::mapTo)
                        ?: sequenceOf(),
                userPortion = { userId ->
                    dto.submission_id?.let {
                        dbPortionRepo
                                .getUserPortion(it, userId)
                                ?.let(dbPortionsMapper::mapTo)
                    }
                }
        )
    }
}