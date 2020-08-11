package pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper.restaurant

import org.springframework.stereotype.Component
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.RestaurantApiType
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.dto.ZomatoRestaurantDto
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.dto.here.HereResultItem
import pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper.ResponseMapper
import pt.isel.ps.g06.httpserver.dataAccess.db.ApiSubmitterMapper
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbRestaurantDto
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.*
import pt.isel.ps.g06.httpserver.dataAccess.model.RestaurantDto
import pt.isel.ps.g06.httpserver.model.Restaurant
import pt.isel.ps.g06.httpserver.model.RestaurantIdentifier
import pt.isel.ps.g06.httpserver.model.Submitter
import pt.isel.ps.g06.httpserver.model.VoteState
import pt.isel.ps.g06.httpserver.util.log
import java.time.OffsetDateTime

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
                latitude = dto.latitude,
                longitude = dto.longitude,
                cuisines = hereCuisineMapper.mapTo(cuisineIds),
                suggestedMeals = dbMealRepository
                        .getAllSuggestedMealsByCuisineApiIds(apiSubmitterId, cuisineIds)
                        .map(dbMealMapper::mapTo)
                        .asSequence(),
                meals = emptySequence(),
                //There are no votes if it's not inserted on db yet
                votes = null,
                //User has not voted yet if not inserted
                userVote = { VoteState.NOT_VOTED },
                //User has not favored yet if not inserted
                isFavorite = { false },
                //Here api does not supply image
                image = dto.image,
                //Here api does not supply creation date
                creationDate = lazy<OffsetDateTime?> { null },
                submitterInfo = lazy {
                    Submitter(
                            identifier = apiSubmitterId,
                            name = RestaurantApiType.Here.toString(),
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
        val cuisineNames = dto.cuisines.split(",").asSequence()
        val apiSubmitterId = apiSubmitterMapper.getSubmitter(RestaurantApiType.Zomato)!!
        return Restaurant(
                identifier = lazy {
                    RestaurantIdentifier(
                            apiId = dto.id,
                            submitterId = apiSubmitterId
                    )
                },
                name = dto.name,
                latitude = dto.latitude,
                longitude = dto.longitude,
                cuisines = zomatoCuisineMapper.mapTo(cuisineNames),
                suggestedMeals = dbMealRepository.getAllSuggestedMealsFromCuisineNames(cuisineNames).map(mealMapper::mapTo),
                meals = emptySequence(),
                //There are no votes if it's not inserted on db yet
                votes = null,
                //User has not voted yet if not inserted
                userVote = { VoteState.NOT_VOTED },
                //User has not favored yet if not inserted
                isFavorite = { false },
                image = dto.image,
                //Zomato api does not supply creation date
                creationDate = lazy<OffsetDateTime?> { null },
                submitterInfo = lazy {
                    Submitter(
                            identifier = apiSubmitterId,
                            name = RestaurantApiType.Zomato.toString(),
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
        private val dbMealMapper: DbMealResponseMapper,
        private val dbCuisineMapper: DbCuisineResponseMapper,
        private val dbVotesMapper: DbVotesResponseMapper,
        private val dbSubmitterMapper: DbSubmitterResponseMapper
) : ResponseMapper<DbRestaurantDto, Restaurant> {

    override fun mapTo(dto: DbRestaurantDto): Restaurant {
        val cuisines = dbCuisineRepository
                .getAllByRestaurantId(dto.submission_id)
                .map(dbCuisineMapper::mapTo)

        return Restaurant(
                identifier = lazy {
                    //A restaurant always has a submitter
                    val submitterId = dbRestaurantRepository.getSubmitterForSubmissionId(dto.submission_id)!!.submitter_id
                    val apiId = dbRestaurantRepository.getApiSubmissionById(dto.submission_id)?.apiId
                    RestaurantIdentifier(
                            submissionId = dto.submission_id,
                            apiId = apiId,
                            submitterId = submitterId
                    )
                },
                name = dto.name,
                latitude = dto.latitude,
                longitude = dto.longitude,
                cuisines = cuisines,
                meals = dbMealRepository
                        .getAllUserMealsForRestaurant(dto.submission_id)
                        .map(dbMealMapper::mapTo),

                suggestedMeals = dbMealRepository
                        .getAllSuggestedMealsFromCuisineNames(cuisines.map { it.name })
                        .map(dbMealMapper::mapTo),

                votes = dbVotesMapper.mapTo(dbRestaurantRepository.getVotes(dto.submission_id)),
                userVote = { userId -> dbRestaurantRepository.getUserVote(dto.submission_id, userId) },
                isFavorite = { userId -> dbFavoriteRepo.getFavorite(dto.submission_id, userId) },
                image = dto.image,
                creationDate = lazy { dbRestaurantRepository.getCreationDate(dto.submission_id) },
                submitterInfo = lazy {
                    //Restaurants always have a submitter, even if it's from the API
                    dbSubmitterRepo
                            .getSubmitterForSubmission(dto.submission_id)
                            ?.let { submitter -> dbSubmitterMapper.mapTo(submitter) }!!
                }
        )
    }
}