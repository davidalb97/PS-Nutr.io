package pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper.restaurant

import org.springframework.stereotype.Component
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.dto.ZomatoRestaurantDto
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.dto.here.HereResultItem
import pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper.ResponseMapper
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbRestaurantDto
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.CuisineDbRepository
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.MealDbRepository
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.RestaurantDbRepository
import pt.isel.ps.g06.httpserver.dataAccess.model.RestaurantDto
import pt.isel.ps.g06.httpserver.model.Restaurant
import pt.isel.ps.g06.httpserver.util.log

@Component
class RestaurantResponseMapper(
        private val zomatoResponseMapper: ZomatoResponseMapper,
        private val hereResponseMapper: HereResponseMapper,
        private val dbResponseMapper: DbRestaurantResponseMapper
) : ResponseMapper<RestaurantDto, Restaurant> {

    override fun mapTo(dto: RestaurantDto): Restaurant {
        return when (dto) {
            is HereResultItem -> hereResponseMapper.mapTo(dto)
            is ZomatoRestaurantDto -> zomatoResponseMapper.mapTo(dto)
            is DbRestaurantDto -> dbResponseMapper.mapTo(dto)
            else -> {
                log("ERROR: Unregistered mapper for RestaurantDto of type '${dto.javaClass.typeName}'!")
                //TODO Should a handler listen to this exception?
                throw NoSuchMethodException("There is no mapper registered for given dto type!")
            }
        }
    }
}

@Component
class HereResponseMapper(
        private val cuisineDbRepository: CuisineDbRepository,
        private val mealRepository: MealDbRepository,
        private val mealResponseMapper: MealResponseMapper
) : ResponseMapper<HereResultItem, Restaurant> {

    override fun mapTo(dto: HereResultItem): Restaurant {
        val cuisineIds = dto.foodTypes?.map { it.id }

        return Restaurant(
                identifier = dto.id,
                name = dto.name,
                latitude = dto.latitude,
                longitude = dto.longitude,
                cuisines = lazy {
                    cuisineIds
                            ?.let { id -> cuisineDbRepository.getByHereCuisineIdentifiers(id).map { it.cuisine_name } }
                            ?: emptyList()
                },
                meals = lazy {
                    cuisineIds
                            ?.let { id -> mealRepository.getByHereCuisinesIdentifiers(id).map(mealResponseMapper::mapTo) }
                            ?: emptyList()
                }
        )
    }
}

@Component
class ZomatoResponseMapper(
        private val mealRepository: MealDbRepository,
        private val mealResponseMapper: MealResponseMapper
) : ResponseMapper<ZomatoRestaurantDto, Restaurant> {

    override fun mapTo(dto: ZomatoRestaurantDto): Restaurant {
        val cuisines = lazy { dto.cuisines.split(",") }

        return Restaurant(
                identifier = dto.id,
                name = dto.name,
                latitude = dto.latitude,
                longitude = dto.longitude,
                cuisines = cuisines,
                meals = lazy {
                    mealRepository
                            .getMealsForCuisines(cuisines.value)
                            .map(mealResponseMapper::mapTo)
                }
        )
    }
}

@Component
class DbRestaurantResponseMapper(
        private val restaurantRepository: RestaurantDbRepository
) : ResponseMapper<DbRestaurantDto, Restaurant> {

    override fun mapTo(dto: DbRestaurantDto): Restaurant {
        val cuisines = lazy {
            restaurantRepository
                    .getRestaurantCuisines(dto.submission_id)
                    .map { it.cuisine_name }
        }

        return Restaurant(
                identifier = dto.id,
                name = dto.name,
                latitude = dto.latitude,
                longitude = dto.longitude,
                cuisines = cuisines,
                meals = lazy { emptyList() }
                //TODO - Handle obtaining meals for Database restaurant: Should it only get from DB? Also from API?
        )
    }
}