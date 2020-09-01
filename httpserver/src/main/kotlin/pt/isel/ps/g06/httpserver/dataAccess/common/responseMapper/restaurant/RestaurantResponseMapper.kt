package pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper.restaurant

import org.springframework.stereotype.Component
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.RestaurantApiType
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.dto.here.HereResultItem
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.dto.zomato.ZomatoRestaurantDto
import pt.isel.ps.g06.httpserver.dataAccess.common.dto.RestaurantDto
import pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper.ResponseMapper
import pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper.submitter.DbSubmitterResponseMapper
import pt.isel.ps.g06.httpserver.dataAccess.db.ApiSubmitterMapper
import pt.isel.ps.g06.httpserver.dataAccess.db.SubmissionContractType
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbRestaurantDto
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.*
import pt.isel.ps.g06.httpserver.model.Submitter
import pt.isel.ps.g06.httpserver.model.VoteState
import pt.isel.ps.g06.httpserver.model.Votes
import pt.isel.ps.g06.httpserver.model.modular.toUserPredicate
import pt.isel.ps.g06.httpserver.model.modular.toUserVote
import pt.isel.ps.g06.httpserver.model.restaurant.Restaurant
import pt.isel.ps.g06.httpserver.model.restaurant.RestaurantIdentifier
import pt.isel.ps.g06.httpserver.util.log
import java.time.OffsetDateTime
import java.util.stream.Stream

@Component
class RestaurantResponseMapper(
        private val zomatoResponseMapper: ZomatoRestaurantResponseMapper,
        private val hereResponseMapper: HereRestaurantResponseMapper,
        private val dbResponseMapper: DbRestaurantResponseMapper
) : ResponseMapper<RestaurantDto, Restaurant> {

    override fun mapTo(dto: RestaurantDto): Restaurant {
        return when (dto) {
            is HereResultItem -> hereResponseMapper.mapTo(dto)
            is ZomatoRestaurantDto -> zomatoResponseMapper.mapTo(dto)
            is DbRestaurantDto -> dbResponseMapper.mapTo(dto)
            else -> {
                log("ERROR: Unregistered mapper for RestaurantDto of type '${dto.javaClass.typeName}'!")
                throw NoSuchMethodException("There is no mapper registered for given dto type!")
            }
        }
    }
}

@Component
class HereRestaurantResponseMapper(
        private val dbMealRepository: MealDbRepository,
        private val apiSubmitterMapper: ApiSubmitterMapper,
        private val dbMealMapper: DbMealResponseMapper,
        private val hereCuisineMapper: HereCuisineResponseMapper
) : ResponseMapper<HereResultItem, Restaurant> {

    override fun mapTo(dto: HereResultItem): Restaurant {
        val cuisineIds = dto.foodTypes?.map { it.id }?.asSequence() ?: emptySequence()
        val apiSubmitterId = apiSubmitterMapper.getSubmitter(RestaurantApiType.Here)!!
        return Restaurant(
                identifier = lazy { RestaurantIdentifier(apiId = dto.id, submitterId = apiSubmitterId) },
                name = dto.name,
                ownerId = null,
                latitude = dto.latitude,
                longitude = dto.longitude,
                cuisines = hereCuisineMapper.mapTo(cuisineIds),
                suggestedMeals = dbMealRepository
                        .getAllSuggestedMealsByCuisineApiIds(apiSubmitterId, cuisineIds)
                        .map(dbMealMapper::mapTo),
                meals = Stream.empty(),
                //There are no votes if it's not inserted on db yet
                votes = lazy { Votes(0, 0) },
                //An API restaurant is not reportable
                isVotable = { false },
                //An api restaurant is always favorable
                isFavorable = { true },
                //User has not voted yet if not inserted
                userVote = { VoteState.NOT_VOTED },
                //User has not favored yet if not inserted
                isFavorite = { false },
                //An API restaurant is always reportable as it is not inserted
                isReportable = { true },
                //Here api does not supply image
                image = dto.image,
                //Here api does not supply creation date
                creationDate = lazy<OffsetDateTime?> { null },
                submitterInfo = lazy {
                    Submitter(
                            identifier = apiSubmitterId,
                            creationDate = null,
                            //TODO return const image for Zomato api icon
                            image = null,
                            isUser = false
                    )
                }
        )
    }
}

@Component
class ZomatoRestaurantResponseMapper(
        private val dbMealRepository: MealDbRepository,
        private val mealMapper: DbMealResponseMapper,
        private val zomatoCuisineMapper: ZomatoCuisineResponseMapper,
        private val apiSubmitterMapper: ApiSubmitterMapper
) : ResponseMapper<ZomatoRestaurantDto, Restaurant> {

    override fun mapTo(dto: ZomatoRestaurantDto): Restaurant {
        val cuisineNames = dto.cuisines.split(",").stream()
        val apiSubmitterId = apiSubmitterMapper.getSubmitter(RestaurantApiType.Zomato)!!
        return Restaurant(
                identifier = lazy {
                    RestaurantIdentifier(
                            apiId = dto.id,
                            submitterId = apiSubmitterId
                    )
                },
                name = dto.name,
                ownerId = null,
                latitude = dto.latitude,
                longitude = dto.longitude,
                cuisines = zomatoCuisineMapper.mapTo(cuisineNames),
                suggestedMeals = dbMealRepository.getAllSuggestedMealsFromCuisineNames(cuisineNames).map(mealMapper::mapTo),
                meals = Stream.empty(),
                //There are no votes if it's not inserted on db yet
                votes = lazy { Votes(0, 0) },
                //User has not voted yet if not inserted
                userVote = { VoteState.NOT_VOTED },
                //An API restaurant is not reportable
                isVotable = { false },
                //An api restaurant is always favorable
                isFavorable = { true },
                //User has not favored yet if not inserted
                isFavorite = { false },
                //An API restaurant is always reportable as it is not inserted
                isReportable = { true },
                image = dto.image,
                //Zomato api does not supply creation date
                creationDate = lazy<OffsetDateTime?> { null },
                submitterInfo = lazy {
                    Submitter(
                            identifier = apiSubmitterId,
                            creationDate = null,
                            //TODO return const image for Zomato api icon
                            image = null,
                            isUser = false
                    )
                }
        )
    }
}

@Component
class DbRestaurantResponseMapper(
        private val dbRestaurantRepository: RestaurantDbRepository,
        private val dbMealRepository: MealDbRepository,
        private val dbCuisineRepository: CuisineDbRepository,
        private val dbFavoriteRepo: FavoriteDbRepository,
        private val dbSubmitterRepo: SubmitterDbRepository,
        private val dbReportRepo: ReportDbRepository,
        private val dbMealMapper: DbMealResponseMapper,
        private val dbCuisineMapper: DbCuisineResponseMapper,
        private val dbVotesMapper: DbVotesResponseMapper,
        private val dbVoteRepository: VoteDbRepository,
        private val dbSubmitterMapper: DbSubmitterResponseMapper,
        private val submissionDbRepository: SubmissionDbRepository
) : ResponseMapper<DbRestaurantDto, Restaurant> {

    override fun mapTo(dto: DbRestaurantDto): Restaurant {
        val cuisines = dbCuisineRepository
                .getAllByRestaurantId(dto.submission_id)
                .map(dbCuisineMapper::mapTo)
        val submitterInfo = lazy {
            //Restaurants always have a submitter, even if it's from the API
            dbSubmitterRepo
                    .getSubmitterForSubmission(dto.submission_id)
                    ?.let { submitter -> dbSubmitterMapper.mapTo(submitter) }!!
        }
        val isReportable = lazy {
            dto.submission_id.let {
                //Restaurant meal requires contract
                submissionDbRepository.hasContract(it, SubmissionContractType.REPORTABLE)
            }
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
                            && dbReportRepo.getReportFromSubmitter(userId, userId) == null
                },
                image = dto.image,
                creationDate = lazy { submissionDbRepository.getCreationDate(dto.submission_id) },
                submitterInfo = submitterInfo
        )
    }
}