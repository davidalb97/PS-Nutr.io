package pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper.restaurant

import org.springframework.stereotype.Component
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.dto.ZomatoRestaurantDto
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.dto.here.HereResultItem
import pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper.ResponseMapper
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.CuisineDbRepository
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.MealCuisineDbRepository
import pt.isel.ps.g06.httpserver.dataAccess.model.RestaurantDto
import pt.isel.ps.g06.httpserver.model.Restaurant
import pt.isel.ps.g06.httpserver.util.log

@Component
class RestaurantResponseMapper(
        private val zomatoResponseMapper: ZomatoResponseMapper,
        private val hereResponseMapper: HereResponseMapper
) : ResponseMapper<RestaurantDto, Restaurant> {
    override fun mapTo(dto: RestaurantDto): Restaurant {
        return when (dto) {
            is HereResultItem -> hereResponseMapper.mapTo(dto)
            is ZomatoRestaurantDto -> zomatoResponseMapper.mapTo(dto)
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
        private val cuisineMealRepository: MealCuisineDbRepository,
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
                            ?.let { id -> cuisineMealRepository.getByHereCuisinesIdentifiers(id).map(mealResponseMapper::mapTo) }
                            ?: emptyList()
                }
        )
    }
}

@Component
class ZomatoResponseMapper(
        private val mealCuisineDbRepository: MealCuisineDbRepository,
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
                    mealCuisineDbRepository
                            .getMealsForCuisines(cuisines.value)
                            .map(mealResponseMapper::mapTo)
                }
        )
    }
}