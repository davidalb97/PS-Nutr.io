package pt.ipl.isel.leic.ps.androidclient.data.api.mapper.input

import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.SimplifiedUserInput
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.info.DetailedMealInput
import pt.ipl.isel.leic.ps.androidclient.data.db.entity.DbMealInfoEntity
import pt.ipl.isel.leic.ps.androidclient.data.model.MealInfo
import pt.ipl.isel.leic.ps.androidclient.data.model.Source
import pt.ipl.isel.leic.ps.androidclient.data.model.SubmissionOwner
import pt.ipl.isel.leic.ps.androidclient.util.TimestampWithTimeZone

class InputSubmissionOwnerMapper {

    fun mapToModel(dto: SimplifiedUserInput) = SubmissionOwner(
        username = dto.name,
        id = dto.id
    )
}