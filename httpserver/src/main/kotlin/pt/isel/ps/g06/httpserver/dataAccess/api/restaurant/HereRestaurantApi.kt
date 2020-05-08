package pt.isel.ps.g06.httpserver.dataAccess.api.restaurant

import org.springframework.stereotype.Repository
import pt.isel.ps.g06.httpserver.dataAccess.api.common.BaseApi
import pt.isel.ps.g06.httpserver.dataAccess.api.common.HttpApiClient
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.dto.here.HereResultContainer
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.mapper.ZomatoResponseMapper
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.uri.HereUriBuilder
import pt.isel.ps.g06.httpserver.dataAccess.model.RestaurantDto

@Repository
class HereRestaurantApi(
        httpClient: HttpApiClient,
        private val uriBuilder: HereUriBuilder,
        restaurantMapper: ZomatoResponseMapper
) : IRestaurantApi, BaseApi(httpClient, restaurantMapper) {

    override fun getRestaurantInfo(id: Int): RestaurantDto? {
        TODO("Not yet implemented")
    }

    override fun searchRestaurants(latitude: Float, longitude: Float, radiusMeters: Int): Collection<RestaurantDto> {
        val uri = uriBuilder.nearbyRestaurants(latitude, longitude, radiusMeters)
        return requestDto(uri, HereResultContainer::class.java).items
    }

    override fun restaurantDailyMeals(restaurantId: Int): List<String> {
        TODO("Not yet implemented")
    }
}