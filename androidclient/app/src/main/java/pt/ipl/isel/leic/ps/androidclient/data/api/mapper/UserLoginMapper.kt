package pt.ipl.isel.leic.ps.androidclient.data.api.mapper

import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.UserLoginInput
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.output.UserLoginOutput
import pt.ipl.isel.leic.ps.androidclient.data.model.UserLogin
import pt.ipl.isel.leic.ps.androidclient.data.model.UserSession

class UserLoginMapper {

    fun mapToDto(model: UserLogin): UserLoginOutput? =
        UserLoginOutput(
            username = model.username,
            password = model.password
        )

    fun mapToModel(dto: UserLoginInput): UserSession =
        UserSession(
            jwt = dto.jwt,
            submitterId = dto.submitterId
        )
}