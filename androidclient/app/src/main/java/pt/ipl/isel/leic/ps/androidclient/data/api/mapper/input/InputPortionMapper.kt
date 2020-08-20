package pt.ipl.isel.leic.ps.androidclient.data.api.mapper.input

import pt.ipl.isel.leic.ps.androidclient.data.db.entity.DbPortionEntity
import pt.ipl.isel.leic.ps.androidclient.data.model.Portion

class InputPortionMapper {

    fun mapToModel(dto: Int) = Portion(
        dbId = DbPortionEntity.DEFAULT_DB_ID,
        dbMealId = DbPortionEntity.DEFAULT_DB_ID,
        amount = dto
    )

    fun mapToListModel(dtos: Iterable<Int>) = dtos.map(::mapToModel)
}