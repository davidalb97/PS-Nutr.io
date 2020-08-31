package pt.ipl.isel.leic.ps.androidclient.data.api.mapper.input

import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.user.UserRegisterInput
import pt.ipl.isel.leic.ps.androidclient.data.model.UserSession

class InputUserRegisterMapper {

    fun mapToModel(dto: UserRegisterInput): UserSession =
        UserSession(
            jwt = dto.jwt
        )
}