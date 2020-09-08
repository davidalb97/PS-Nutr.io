package pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.here.mapper

import org.springframework.stereotype.Component
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.RestaurantApiType
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.here.dto.HereResultItem
import pt.isel.ps.g06.httpserver.dataAccess.db.ApiSubmitterMapper
import pt.isel.ps.g06.httpserver.dataAccess.common.mapper.ModelMapper
import pt.isel.ps.g06.httpserver.dataAccess.db.mapper.DbMealModelMapper
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.MealDbRepository
import pt.isel.ps.g06.httpserver.model.Submitter
import pt.isel.ps.g06.httpserver.model.VoteState
import pt.isel.ps.g06.httpserver.model.Votes
import pt.isel.ps.g06.httpserver.model.restaurant.Restaurant
import pt.isel.ps.g06.httpserver.model.restaurant.RestaurantIdentifier
import java.time.OffsetDateTime

@Component
class HereRestaurantModelMapper(
        private val dbMealRepository: MealDbRepository,
        private val apiSubmitterMapper: ApiSubmitterMapper,
        private val dbMealMapper: DbMealModelMapper,
        private val hereCuisineMapper: HereCuisineModelMapper
) : ModelMapper<HereResultItem, Restaurant> {

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
                meals = emptySequence(),
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
                            image = null,
                            isUser = false
                    )
                }
        )
    }
}