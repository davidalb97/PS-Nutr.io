package pt.ipl.isel.leic.ps.androidclient.data.api.mapper

import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.UserRegisterInput
import pt.ipl.isel.leic.ps.androidclient.data.model.UserSession

class InputUserRegisterMapper {

    fun mapToModel(dto: UserRegisterInput): UserSession =
        UserSession(
            jwt = dto.jwt
        )
}