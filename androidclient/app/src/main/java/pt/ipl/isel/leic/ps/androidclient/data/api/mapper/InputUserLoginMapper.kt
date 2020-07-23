package pt.ipl.isel.leic.ps.androidclient.data.api.mapper

import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.UserLoginInput
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.output.UserLoginOutput
import pt.ipl.isel.leic.ps.androidclient.data.model.UserLogin
import pt.ipl.isel.leic.ps.androidclient.data.model.UserSession

class InputUserLoginMapper {

    fun mapToModel(dto: UserLoginInput): UserSession =
        UserSession(
            jwt = dto.jwt
        )
}