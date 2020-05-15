package pt.isel.ps.g06.httpserver.model

import pt.isel.ps.g06.httpserver.dataAccess.api.food.FoodApiType
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbSubmitterDto

data class TestFoodApi(
        val apiType: FoodApiType,
        val submitterId: Int
)

fun DbSubmitterDto.toTestFoodApi(): TestFoodApi {
    return TestFoodApi(
            FoodApiType.valueOf(this.submitter_name),
            this.submitter_id
    )
}