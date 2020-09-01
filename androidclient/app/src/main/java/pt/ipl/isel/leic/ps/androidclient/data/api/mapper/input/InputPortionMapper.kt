package pt.ipl.isel.leic.ps.androidclient.data.api.mapper.input

import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.portion.PortionsInput
import pt.ipl.isel.leic.ps.androidclient.data.db.entity.DbPortionEntity
import pt.ipl.isel.leic.ps.androidclient.data.model.Portions

class InputPortionMapper {

    fun mapToModel(dto: PortionsInput) = Portions(
        dbId = DbPortionEntity.DEFAULT_DB_ID,
        dbMealId = DbPortionEntity.DEFAULT_DB_ID,
        userPortion = dto.userPortion?.toFloat(),
        allPortions = dto.portions.map { it.toFloat() }.toList()
    )
}