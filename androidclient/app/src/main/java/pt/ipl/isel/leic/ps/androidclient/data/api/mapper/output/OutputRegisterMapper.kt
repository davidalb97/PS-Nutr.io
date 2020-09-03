package pt.ipl.isel.leic.ps.androidclient.data.api.mapper.output

import pt.ipl.isel.leic.ps.androidclient.data.api.dto.output.RegisterOutput
import pt.ipl.isel.leic.ps.androidclient.data.model.UserRegister


class OutputRegisterMapper {

    fun mapToOutputModel(model: UserRegister) = RegisterOutput(
        email = model.email,
        username = model.username,
        password = model.password
    )
}