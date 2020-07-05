package pt.ipl.isel.leic.ps.androidclient.data.api.mapper

import pt.ipl.isel.leic.ps.androidclient.data.api.dto.output.UserRegisterOutput
import pt.ipl.isel.leic.ps.androidclient.data.model.UserRegister

class UserRegisterMapper {

    fun mapToDto(model: UserRegister): UserRegisterOutput? =
        UserRegisterOutput(
            email = model.email,
            username = model.username,
            password = model.password
        )
}