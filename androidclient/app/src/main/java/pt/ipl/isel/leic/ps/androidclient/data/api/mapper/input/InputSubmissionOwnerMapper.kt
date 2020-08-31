package pt.ipl.isel.leic.ps.androidclient.data.api.mapper.input

import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.user.SimplifiedUserInput
import pt.ipl.isel.leic.ps.androidclient.data.model.SubmissionOwner

class InputSubmissionOwnerMapper {

    fun mapToModel(dto: SimplifiedUserInput) = SubmissionOwner(
        id = dto.id
    )
}