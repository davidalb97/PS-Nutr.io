package pt.isel.ps.g06.httpserver.model

import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbSubmitterDto

data class TestFoodApi(
//        val apiType: FoodApiType,
        val apiType: String,
        val submitterId: Int
)

fun DbSubmitterDto.toTestFoodApi(): TestFoodApi {
    return TestFoodApi(
            TODO(),
            //FoodApiType.valueOf(this.submitter_name),
            this.submitter_id
    )
}