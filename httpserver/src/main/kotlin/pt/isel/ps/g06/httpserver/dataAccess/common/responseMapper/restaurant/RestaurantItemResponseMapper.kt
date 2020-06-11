package pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper.restaurant

import org.springframework.stereotype.Component
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.dto.ZomatoRestaurantDto
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.dto.here.HereResultItem
import pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper.UserResponseMapper
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.info.DbRestaurantInfoDto
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.info.DbRestaurantItemDto
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.CuisineDbRepository
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.MealDbRepository
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.RestaurantDbRepository
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.RestaurantMealDbRepository
import pt.isel.ps.g06.httpserver.dataAccess.model.RestaurantItemDto
import pt.isel.ps.g06.httpserver.model.RestaurantInfo
import pt.isel.ps.g06.httpserver.model.RestaurantItem
import pt.isel.ps.g06.httpserver.model.Votes
import pt.isel.ps.g06.httpserver.util.log


@Component
class RestaurantItemResponseMapper(
        private val zomatoResponseMapper: ZomatoRestaurantItemResponseMapper,
        private val hereResponseMapper: HereRestaurantItemResponseMapper,
        private val dbResponseMapper: DbRestaurantItemResponseMapper
) : UserResponseMapper<RestaurantItemDto, RestaurantItem> {

    override fun mapTo(dto: RestaurantItemDto, userId: Int): RestaurantItem {
        return when (dto) {
            is HereResultItem -> hereResponseMapper.mapTo(dto, userId)
            is ZomatoRestaurantDto -> zomatoResponseMapper.mapTo(dto, userId)
            is DbRestaurantItemDto -> dbResponseMapper.mapTo(dto, userId)
            else -> {
                log("ERROR: Unregistered mapper for RestaurantDto of type '${dto.javaClass.typeName}'!")
                //TODO Should a handler listen to this exception?
                throw NoSuchMethodException("There is no mapper registered for given dto type!")
            }
        }
    }
}

@Component
class HereRestaurantItemResponseMapper(
        private val cuisineDbRepository: CuisineDbRepository,
        private val mealRepository: MealDbRepository,
        private val restaurantMealRepository: RestaurantMealDbRepository,
        private val mealItemResponseMapper: MealItemResponseMapper
) : UserResponseMapper<HereResultItem, RestaurantItem> {

    override fun mapTo(dto: HereResultItem, userId: Int): RestaurantItem {

        return RestaurantItem(
                //TODO format submitter/submissionId/apiId
                identifier = dto.id,
                name = dto.name,
                latitude = dto.latitude,
                longitude = dto.longitude,
                //There are no votes if it's not inserted on db yet
                votes = Votes(0, 0),
                //User has not voted yet if not inserted
                userVote = null,
                //User has not favored yet if not inserted
                isFavorite = false,
                //Here does not supply image
                image = null
        )
    }
}

@Component
class ZomatoRestaurantItemResponseMapper(
        private val mealRepository: MealDbRepository,
        private val mealItemResponseMapper: MealItemResponseMapper
) : UserResponseMapper<ZomatoRestaurantDto, RestaurantItem> {

    override fun mapTo(dto: ZomatoRestaurantDto, userId: Int): RestaurantItem {

        return RestaurantItem(
                //TODO format submitter/submissionId/apiId
                identifier = dto.id,
                name = dto.name,
                latitude = dto.latitude,
                longitude = dto.longitude,
                //There are no votes if it's not inserted on db yet
                votes = Votes(0, 0),
                //User has not voted yet if not inserted
                userVote = null,
                //User has not favored yet if not inserted
                isFavorite = false,
                //Zomato does not supply image
                image = null
        )
    }
}

@Component
class DbRestaurantItemResponseMapper(
        private val restaurantRepository: RestaurantDbRepository
) : UserResponseMapper<DbRestaurantItemDto, RestaurantItem> {

    override fun mapTo(dto: DbRestaurantItemDto, userId: Int): RestaurantItem {

        return RestaurantItem(
                //TODO format submitter/submissionId/apiId
                identifier = "${dto.restaurant.submission_id}+${dto.submitterId}+${dto.apiId}",
                name = dto.restaurant.restaurant_name,
                latitude = dto.restaurant.latitude,
                longitude = dto.restaurant.longitude,
                votes = Votes(
                        dto.public.votes.positive_count,
                        dto.public.votes.negative_count
                ),
                userVote = dto.public.userVote,
                isFavorite = dto.isFavorite,
                image = dto.image
        )
    }
}
