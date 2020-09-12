package pt.ipl.isel.leic.ps.androidclient.data.api.mapper.output

import pt.ipl.isel.leic.ps.androidclient.data.api.dto.output.LoginOutput
import pt.ipl.isel.leic.ps.androidclient.data.model.UserLogin


class OutputLoginMapper {

    fun mapToOutputModel(model: UserLogin) = LoginOutput(
        email = model.email,
        password = model.password
    )
}