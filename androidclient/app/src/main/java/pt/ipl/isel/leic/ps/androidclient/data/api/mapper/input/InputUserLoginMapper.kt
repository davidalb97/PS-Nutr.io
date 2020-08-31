package pt.ipl.isel.leic.ps.androidclient.data.api.mapper.input

import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.user.UserLoginInput
import pt.ipl.isel.leic.ps.androidclient.data.model.UserSession

class InputUserLoginMapper {

    fun mapToModel(dto: UserLoginInput): UserSession =
        UserSession(
            jwt = dto.jwt
        )
}