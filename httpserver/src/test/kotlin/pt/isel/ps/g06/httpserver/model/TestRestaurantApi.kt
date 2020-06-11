package pt.isel.ps.g06.httpserver.model

import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.RestaurantApiType
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbSubmitterDto

data class TestRestaurantApi(
        val apiType: RestaurantApiType,
        val submitterId: Int
)

fun DbSubmitterDto.toTestRestaurantApi(): TestRestaurantApi {
    return TestRestaurantApi(
            RestaurantApiType.valueOf(this.submitter_name),
            this.submitter_id
    )
}