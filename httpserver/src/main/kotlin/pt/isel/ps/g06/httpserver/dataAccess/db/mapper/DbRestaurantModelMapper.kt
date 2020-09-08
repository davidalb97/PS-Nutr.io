package pt.isel.ps.g06.httpserver.dataAccess.db.mapper

import org.springframework.stereotype.Component
import pt.isel.ps.g06.httpserver.dataAccess.common.mapper.ModelMapper
import pt.isel.ps.g06.httpserver.dataAccess.db.SubmissionContractType
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbRestaurantDto
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.*
import pt.isel.ps.g06.httpserver.model.modular.toUserPredicate
import pt.isel.ps.g06.httpserver.model.modular.toUserVote
import pt.isel.ps.g06.httpserver.model.restaurant.Restaurant
import pt.isel.ps.g06.httpserver.model.restaurant.RestaurantIdentifier

@Component
class DbRestaurantModelMapper(
        private val dbMealRepository: MealDbRepository,
        private val dbCuisineRepository: CuisineDbRepository,
        private val dbFavoriteRepo: FavoriteDbRepository,
        private val dbSubmitterRepo: SubmitterDbRepository,
        private val dbReportRepo: ReportDbRepository,
        private val dbMealMapper: DbMealModelMapper,
        private val dbCuisineMapper: DbCuisineModelMapper,
        private val dbVotesMapper: DbVotesModelMapper,
        private val dbVoteRepository: VoteDbRepository,
        private val dbSubmitterMapper: DbSubmitterModelMapper,
        private val submissionDbRepository: SubmissionDbRepository
) : ModelMapper<DbRestaurantDto, Restaurant> {

    override fun mapTo(dto: DbRestaurantDto): Restaurant {
        val cuisines = dbCuisineRepository
                .getAllByRestaurantId(dto.submission_id)
                .map(dbCuisineMapper::mapTo)
        val submitterInfo = lazy {
            //Restaurants always have a submitter, even if it's from the API
            dbSubmitterRepo.getSubmitterForSubmission(dto.submission_id)
                    ?.let(dbSubmitterMapper::mapTo)!!
        }
        //Restaurant meal requires contract
        val isReportable = lazy {
            submissionDbRepository.hasContract(dto.submission_id, SubmissionContractType.REPORTABLE)
        }
        return Restaurant(
                identifier = lazy {
                    val apiId = submissionDbRepository.getApiSubmissionById(dto.submission_id)?.apiId
                    RestaurantIdentifier(
                            submissionId = dto.submission_id,
                            apiId = apiId,
                            submitterId = submitterInfo.value.identifier
                    )
                },
                name = dto.name,
                ownerId = dto.ownerId,
                latitude = dto.latitude,
                longitude = dto.longitude,
                cuisines = cuisines,
                meals = dbMealRepository
                        .getAllUserMealsForRestaurant(dto.submission_id)
                        .map(dbMealMapper::mapTo),
                suggestedMeals = dbMealRepository
                        .getAllSuggestedMealsFromCuisineNames(cuisines.map { it.name })
                        .map(dbMealMapper::mapTo),
                votes = lazy { dbVotesMapper.mapTo(dbVoteRepository.getVotes(dto.submission_id)) },
                userVote = toUserVote { userId -> dbVoteRepository.getUserVote(dto.submission_id, userId) },
                isVotable = { userId ->
                    //A user cannot favorite on it's own submission
                    submitterInfo.value.identifier != userId
                },
                isFavorite = toUserPredicate({ false }) { userId ->
                    dbFavoriteRepo.getFavorite(dto.submission_id, userId)
                },
                isFavorable = { userId -> //A user cannot favorite on it's own submission
                    submitterInfo.value.identifier != userId
                },
                isReportable = toUserPredicate({ isReportable.value }) { userId ->
                    isReportable.value
                            //Submitter cannot report it's own submission
                            && submitterInfo.value.identifier != userId
                            //It is only reportable once per user
                            && !dbReportRepo.userHasReported(userId, dto.submission_id)
                },
                image = dto.image,
                creationDate = lazy { submissionDbRepository.getCreationDate(dto.submission_id) },
                submitterInfo = submitterInfo
        )
    }
}