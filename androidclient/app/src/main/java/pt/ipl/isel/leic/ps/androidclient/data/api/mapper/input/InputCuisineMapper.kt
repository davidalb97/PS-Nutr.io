package pt.ipl.isel.leic.ps.androidclient.data.api.mapper.input

import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.cuisine.CuisinesInput
import pt.ipl.isel.leic.ps.androidclient.data.db.entity.DbCuisineEntity
import pt.ipl.isel.leic.ps.androidclient.data.model.Cuisine

class InputCuisineMapper {

    fun mapToModel(dto: String) = Cuisine(
        dbId = DbCuisineEntity.DEFAULT_DB_ID,
        dbMealId = DbCuisineEntity.DEFAULT_DB_ID,
        dbRestaurantId = DbCuisineEntity.DEFAULT_DB_ID,
        name = dto
    )

    fun mapToListModel(dtos: Iterable<String>) = dtos.map(::mapToModel)

    fun mapToListModel(dto: CuisinesInput) = dto.cuisines.map(::mapToModel)
}