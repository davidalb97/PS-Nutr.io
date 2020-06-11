package pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper.restaurant

import org.springframework.stereotype.Component
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.dto.ZomatoRestaurantDto
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.dto.here.HereResultItem
import pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper.UserResponseMapper
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.info.DbRestaurantInfoDto
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.CuisineDbRepository
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.MealDbRepository
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.RestaurantDbRepository
import pt.isel.ps.g06.httpserver.dataAccess.model.RestaurantInfoDto
import pt.isel.ps.g06.httpserver.model.Votes
import pt.isel.ps.g06.httpserver.model.RestaurantInfo
import pt.isel.ps.g06.httpserver.model.RestaurantMealItem
import pt.isel.ps.g06.httpserver.util.log

@Component
class RestaurantInfoResponseMapper(
        private val zomatoResponseMapper: ZomatoRestaurantInfoResponseMapper,
        private val hereResponseMapper: HereRestaurantInfoResponseMapper,
        private val dbResponseMapper: DbRestaurantInfoResponseMapper
) : UserResponseMapper<RestaurantInfoDto, RestaurantInfo> {

    override fun mapTo(dto: RestaurantInfoDto, userId: Int?): RestaurantInfo {
        return when (dto) {
            is HereResultItem -> hereResponseMapper.mapTo(dto, userId)
            is ZomatoRestaurantDto -> zomatoResponseMapper.mapTo(dto, userId)
            is DbRestaurantInfoDto -> dbResponseMapper.mapTo(dto, userId)
            else -> {
                log("ERROR: Unregistered mapper for RestaurantDto of type '${dto.javaClass.typeName}'!")
                //TODO Should a handler listen to this exception?
                throw NoSuchMethodException("There is no mapper registered for given dto type!")
            }
        }
    }
}

@Component
class HereRestaurantInfoResponseMapper(
        private val cuisineDbRepository: CuisineDbRepository,
        private val dbMealRepository: MealDbRepository,
        private val mealItemResponseMapper: MealItemResponseMapper
) : UserResponseMapper<HereResultItem, RestaurantInfo> {

    override fun mapTo(dto: HereResultItem, userId: Int?): RestaurantInfo {
        val cuisineIds = dto.foodTypes?.map { it.id }

        return RestaurantInfo(
                //TODO format submitter/submissionId/apiId
                identifier = dto.id,
                name = dto.name,
                latitude = dto.latitude,
                longitude = dto.longitude,
                cuisines = lazy {
                    cuisineIds
                            ?.let { id -> cuisineDbRepository.getAllByNames(id)
                                    .map { it.cuisine_name }
                            } ?: emptyList()
                },
                suggestedMeals = lazy {
                    cuisineIds
                            ?.let { id -> dbMealRepository.getAllByCuisineNames(id, userId)
                                    .map { mealItemResponseMapper.mapTo(it)}
                            } ?: emptyList()
                },
                meals = lazy { emptyList<RestaurantMealItem>()},
                //There are no votes if it's not inserted on db yet
                votes = Votes(0, 0),
                //User has not voted yet if not inserted
                userVote = null,
                //User has not favored yet if not inserted
                isFavorite = null,
                //Here does not supply image
                image = null
        )
    }
}

@Component
class ZomatoRestaurantInfoResponseMapper(
        private val dbMealRepository: MealDbRepository,
        private val mealItemResponseMapper: MealItemResponseMapper
) : UserResponseMapper<ZomatoRestaurantDto, RestaurantInfo> {

    override fun mapTo(dto: ZomatoRestaurantDto, userId: Int?): RestaurantInfo {
        val cuisines = lazy { dto.cuisines.split(",") }

        return RestaurantInfo(
                //TODO format submitter/submissionId/apiId
                identifier = dto.id,
                name = dto.name,
                latitude = dto.latitude,
                longitude = dto.longitude,
                cuisines = cuisines,
                suggestedMeals = lazy {
                    dbMealRepository
                            .getAllByCuisineNames(cuisines.value, userId)
                            .map(mealItemResponseMapper::mapTo)
                },
                meals = lazy { emptyList<RestaurantMealItem>()},
                //There are no votes if it's not inserted on db yet
                votes = Votes(0, 0),
                //User has not voted yet if not inserted
                userVote = null,
                //User has not favored yet if not inserted
                isFavorite = null,
                //Zomato does not supply image
                image = null
        )
    }
}

@Component
class DbRestaurantInfoResponseMapper(
        private val restaurantRepository: RestaurantDbRepository,
        private val restaurantMealItemResponseMapper: RestaurantMealItemResponseMapper,
        private val mealItemResponseMapper: MealItemResponseMapper
) : UserResponseMapper<DbRestaurantInfoDto, RestaurantInfo> {

    override fun mapTo(dto: DbRestaurantInfoDto, userId: Int?): RestaurantInfo {
        val cuisines = lazy {
            restaurantRepository
                    .getRestaurantCuisines(dto.restaurantItem.restaurant.submission_id)
                    .map { it.cuisine_name }
        }

        return RestaurantInfo(
                //TODO format submitter/submissionId/apiId
                identifier = "${dto.id}+${dto.restaurantItem.submitterId}+${dto.restaurantItem.apiId}",
                name = dto.name,
                latitude = dto.latitude,
                longitude = dto.longitude,
                cuisines = cuisines,
                meals = lazy {
                    dto.restaurantMeals.map(restaurantMealItemResponseMapper::mapTo)
                },
                suggestedMeals =  lazy { dto.suggestedMeals.map(mealItemResponseMapper::mapTo) },
                //TODO - Handle obtaining meals for Database restaurant: Should it only get from DB? Also from API?
                votes = dto.votes,
                userVote = dto.userVote,
                isFavorite = dto.isFavorite,
                image = dto.restaurantItem.image
        )
    }
}